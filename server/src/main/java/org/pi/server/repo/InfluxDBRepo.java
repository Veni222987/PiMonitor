package org.pi.server.repo;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * InfluxDB 操作
 */
@Repository
@Slf4j
public class InfluxDBRepo {

    @Autowired
    private InfluxDBClient influxDB;

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

    public boolean write(String measurement, Map<String, String> tags, Map<String, Object> fields, long timestamp, WritePrecision precision) {
        try {
            Point point = Point.measurement(measurement)
                    .addTags(tags)
                    .addFields(fields)
                    .time(timestamp, precision);
            WriteApiBlocking writeApi = influxDB.getWriteApiBlocking();
            writeApi.writePoint(point);
            return true;
        } catch (Exception e) {
            log.error("InfluxDB 写入数据失败", e);
            return false;
        }
    }

    // 通用查询
    public List<FluxTable> query(String flux) {
        return influxDB.getQueryApi().query(flux);
    }

    /**
     * 解析查询结果
     * @param tables
     * @return
     */
    public Map<String, List<Map<String, Object>>> parse(List<FluxTable> tables) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        tables.forEach(table -> {
            table.getRecords().forEach(record -> {
                String field = record.getField();
                Instant time = record.getTime();
                Object value = record.getValue();
                if (!result.containsKey(field)) {
                    result.put(field, new LinkedList<>());
                } else {
                    assert time != null;
                    result.get(field).add(Map.of("time", time.getLong(ChronoField.INSTANT_SECONDS), "value", value));
                }
            });
        });
        return result;
    }
}
