package org.pi.server.model.dto;

import com.influxdb.client.domain.WritePrecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfluxDBPoints {
    String measurement; // 测量点名称
    WritePrecision precision; // 写入精度(NS、US、MS、S)
    List<Point> points; // 测量点数据
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Point {
        Map<String, String> tags; // 标签
        Map<String, Object> fields; // 字段
        Long timestamp; // 时间戳
    }
}
