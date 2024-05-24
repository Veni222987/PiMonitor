package entity

// Metrics 每次上报的监控数据结构体
type Metrics struct {
	// 监控时间戳
	Timestamp int64 `json:"timestamp"`
	// 监控指标，key是app name
	MetricsMap map[string]interface{} `json:"metrics"`
}
