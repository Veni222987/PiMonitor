package org.pi.server.model.dto;

import com.influxdb.client.domain.WritePrecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author hu1hu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfluxDBPoints {
    // 测量点名称
    String measurement;
    // 写入精度(NS、US、MS、S)
    WritePrecision precision;
    // 测量点数据
    List<Point> points;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Point {
        Map<String, String> tags; // 标签
        Map<String, Object> fields; // 字段
        Long timestamp; // 时间戳
    }
}
