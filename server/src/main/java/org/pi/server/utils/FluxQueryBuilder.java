package org.pi.server.utils;

public class FluxQueryBuilder {
    private final StringBuilder builder;

    public FluxQueryBuilder() {
        builder = new StringBuilder();
    }

    public FluxQueryBuilder fromBucket(String bucket) {
        builder.append("from(bucket:\"").append(bucket).append("\")");
        return this;
    }

    public FluxQueryBuilder range(Long start, Long stop) {
        builder.append(" |> range(start:").append(start).append(", stop:").append(stop).append(")");
        return this;
    }

    public FluxQueryBuilder filterMeasurement(String measurement) {
        builder.append(" |> filter(fn: (r) => r._measurement == \"").append(measurement).append("\")");
        return this;
    }

    public FluxQueryBuilder filterMeasurementByRegex(String measurement) {
        builder.append(" |> filter(fn: (r) => r._measurement =~ /").append(measurement).append("/)");
        return this;
    }

    public FluxQueryBuilder filterField(String... fields) {
        if (fields.length == 0) return this;
        builder.append(" |> filter(fn: (r) => ");
        for (int i = 0; i < fields.length - 1; i++) {
            builder.append("r._field == \"").append(fields[i]).append("\" or ");
        }
        builder.append("r._field == \"").append(fields[fields.length - 1]).append("\")");
        return this;
    }

    public FluxQueryBuilder filterTag(String tag, String value) {
        builder.append(" |> filter(fn: (r) => r.").append(tag).append(" == \"").append(value).append("\")");
        return this;
    }

    public String build() {
        return builder.toString();
    }
}
