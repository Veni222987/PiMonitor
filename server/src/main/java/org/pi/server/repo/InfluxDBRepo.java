package org.pi.server.repo;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * InfluxDB 操作
 * 
 * @author hu1hu
 */
@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InfluxDBRepo {
    private final InfluxDBClient influxDB;

    /**
     * 写入数据
     * 
     * @param data 数据
     * @return 是否成功
     */
    public boolean write(String data) {
        try {
            WriteApiBlocking writeApi = influxDB.getWriteApiBlocking();
            writeApi.writeRecord(WritePrecision.NS, data);
            return true;
        } catch (Exception e) {
            log.error("InfluxDB 写入数据失败", e);
            return false;
        }
    }

    // ......
}

    /**
     * 写入数据
     * 
     * @param measurement 表名
     * @param tags        标签
     * @param fields      字段
     * @param timestamp   时间戳
     * @param precision   时间精度
     * @return 是否成功
     */
    public boolean write(String measurement, Map<String, String> tags, Map<String, Object> fields, long timestamp,
            WritePrecision precision) {
        try {
            Point point = Point.measurement(measurement)
                    .addTags(tags)
                    .addFields(fields)
                    .time(timestamp, precision);
            WriteApiBlocking writeApi = influxDB.getWriteApiBlocking();
            try {
                writeApi.writePoint(point);
                return true;
            } catch (Exception e) {
                log.warn("数据异常", e);
                return true;
            }
        } catch (Exception e) {
            log.error("InfluxDB 写入数据失败", e);
            return false;
        }
    }

    /**
     * 查询数据
     * 
     * @param flux Flux 查询语句
     * @return 查询结果
     */
    public List<FluxTable> query(String flux) {
        return influxDB.getQueryApi().query(flux);
    }

    /**
     * 解析查询结果
     * 
     * @param tables 查询结果
     * @return 解析后的结果
     */
    public Map<String, Map<String, Object>> parse(@NotNull List<FluxTable> tables) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        tables.forEach(table -> {
            table.getRecords().forEach(record -> {
                // 获取一条记录
                String field = record.getField();
                Instant time = record.getTime();
                Object value = record.getValue();

                if (!result.containsKey(field)) {
                    Map<String, Object> values = record.getValues();
                    Map<String, Object> map = new HashMap<>();
                    if (values.containsKey("metric_type")) {
                        map.put("metric_type", values.get("metric_type"));
                    }
                    map.put("data", new ArrayList<>());
                    result.put(field, map);
                } else {
                    ((List) result.get(field).get("data")).add(Map.of("time",
                            time.getLong(ChronoField.INSTANT_SECONDS) * 1000 + time.get(ChronoField.MILLI_OF_SECOND),
                            "value", value));
                }
            });
        });
        return result;
    }
}
