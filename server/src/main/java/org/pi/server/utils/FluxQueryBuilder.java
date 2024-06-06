package org.pi.server.utils;

/**
 * @author hu1hu
 */
public class FluxQueryBuilder {
    private final StringBuilder builder;

    /**
     * Create a new FluxQueryBuilder
     */
    public FluxQueryBuilder() {
        builder = new StringBuilder();
    }

    /**
     * 设置bucket
     * @param bucket the bucket to query
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder fromBucket(String bucket) {
        builder.append("from(bucket:\"").append(bucket).append("\")");
        return this;
    }

    /**
     * 添加时间范围
     * @param start the start time
     * @param stop the stop time
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder range(Long start, Long stop) {
        builder.append(" |> range(start:").append(start).append(", stop:").append(stop).append(")");
        return this;
    }

    /**
     * 设置measurement
     * @param measurement the measurement to query
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder filterMeasurement(String measurement) {
        builder.append(" |> filter(fn: (r) => r._measurement == \"").append(measurement).append("\")");
        return this;
    }

    /**
     * 设置measurement通过正则表达式
     * @param measurement the measurement to query
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder filterMeasurementByRegex(String measurement) {
        builder.append(" |> filter(fn: (r) => r._measurement =~ /").append(measurement).append("/)");
        return this;
    }

    /**
     * 设置field
     * @param fields the fields to query
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder filterField(String... fields) {
        if (fields.length == 0) {
            return this;
        }
        builder.append(" |> filter(fn: (r) => ");
        for (int i = 0; i < fields.length - 1; i++) {
            builder.append("r._field == \"").append(fields[i]).append("\" or ");
        }
        builder.append("r._field == \"").append(fields[fields.length - 1]).append("\")");
        return this;
    }

    /**
     * 设置tag通过正则表达式
     * @param tag the tag to filter
     * @param value the value to filter
     * @return a new FluxQueryBuilder
     */
    public FluxQueryBuilder filterTag(String tag, String value) {
        builder.append(" |> filter(fn: (r) => r.").append(tag).append(" == \"").append(value).append("\")");
        return this;
    }

    /**
     * 转换为String
     * @return the built Flux query
     */
    public String build() {
        return builder.toString();
    }
}
