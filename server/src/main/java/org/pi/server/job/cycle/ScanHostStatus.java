package org.pi.server.job.cycle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.mapper.AlarmMapper;
import org.pi.server.mapper.HostMapper;
import org.pi.server.model.entity.Alarm;
import org.pi.server.model.entity.Host;
import org.pi.server.model.enums.AlarmTypeEnum;
import org.pi.server.model.enums.HostStatusEnum;
import org.pi.server.repo.InfluxDBRepo;
import org.pi.server.service.AliyunEmailService;
import org.pi.server.service.AliyunOssService;
import org.pi.server.utils.FluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hu1hu
 * @date 2024/6/6 12:17
 * @description 扫描主机状态
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScanHostStatus {
    private final HostMapper hostMapper;
    private final AliyunEmailService aliyunEmailService;
    private final AlarmMapper alarmMapper;
    private final InfluxDBRepo influxDBRepo;
    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        List<Host> hosts = hostMapper.selectList(queryWrapper);
        LocalDateTime now = LocalDateTime.now();
        // AlarmTypeEnum 与 performance_表中的字段名的映射
        Map<String, String> map = Map.of("CPU","cpu_percent", "MEM", "mem_percent",
                "DISK", "disk_percent", "NETWORK", "network_rate",
                "TCP", "tcp_connection");
        // 遍历所有主机
        for (Host host : hosts) {
            LocalDateTime lastTime = host.getLastTime();
            QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
            alarmQueryWrapper.eq("host_id", host.getId());
            // 查询所有告警
            List<Alarm> alarms = alarmMapper.selectList(alarmQueryWrapper);
            // 以alarm_type为 key ， alarm 为 value 转成 map
            Map<AlarmTypeEnum, Alarm> alarmMap = alarms.stream().collect(Collectors.toMap(Alarm::getAlarmType, alarm -> alarm));

            // 处理主机状态
            if (host.getStatus() == HostStatusEnum.UNMONITORED) {
                // 未监控状态，检查是否有告警
                if (lastTime.plusMinutes(2).isAfter(now)) {
                    // 2 分钟内更新状态，设置为监控中
                    host.setStatus(HostStatusEnum.MONITORING);
                }
            } else if (host.getStatus() == HostStatusEnum.MONITORING) {
                // 监控中状态，检查是否有告警
                if (lastTime.plusMinutes(2).isBefore(now)) {
                    // 超过 2 分钟未更新状态，设置为离线
                    host.setStatus(HostStatusEnum.UNMONITORED);
                    log.error(alarmMap.toString());
                    log.error(AlarmTypeEnum.HOST.toString());
                    // 查询是否有离线告警
                    if (alarmMap.containsKey(AlarmTypeEnum.HOST)) {
                        System.out.println(alarmMap.toString());
                        System.out.println(AlarmTypeEnum.HOST);
                        Alarm alarm = alarmMap.get(AlarmTypeEnum.HOST);
                        try {
                            aliyunEmailService.send("alarm", alarm.getNotificationAccount(),
                                Map.of("alarmType", alarm.getAlarmType().toString(),
                                        "alarmTime", lastTime.toString(),
                                        "alarmDetails", "您的主机" + host.getHostname() + "已离线，请及时处理"));
                        } catch (Exception e) {
                            log.warn("发送邮件失败", e);
                        }
                    }

                } else {
                    // 检查主机使用情况
                    // todo 检查主机使用情况
                    alarmMap.remove(AlarmTypeEnum.HOST);
                    alarmMap.forEach(
                        (alarmType, alarm) -> {
                            Long end = Instant.now().toEpochMilli();
                            Long start = end - alarm.getDuration();
                            FluxQueryBuilder fluxQueryBuilder = new FluxQueryBuilder();;
                            List<FluxTable> tables = influxDBRepo.query(fluxQueryBuilder.filterMeasurement("performance_" + host.getId()).range(start, end)
                                    .filterField(map.get(alarm.getAlarmType().toString())).build());
                            Double thresholdValue = alarm.getThresholdValue();
                            double sum = 0L;
                            long count = 0L;
                            for (FluxTable table : tables) {
                                List<FluxRecord> records = table.getRecords();
                                for (FluxRecord record : records) {
                                    // 获取一条记录
                                    sum += (Double) record.getValue();
                                    count++;
                                }
                            }
                            double avg = sum / count;
                            if (avg > thresholdValue) {
                                try {
                                    aliyunEmailService.send("alarm", alarm.getNotificationAccount(),
                                        Map.of("alarmType", alarm.getAlarmType().toString(),
                                                "alarmTime", lastTime.toString(),
                                                "alarmDetails", "您的主机" + host.getHostname() + "的" + alarm.getAlarmType().toString() + "使用率超过阈值，请及时处理"));
                                } catch (Exception e) {
                                    log.warn("发送邮件失败", e);
                                }
                            }
                        }
                    );
                }
            }
            hostMapper.updateById(host);
        }
    }
}
