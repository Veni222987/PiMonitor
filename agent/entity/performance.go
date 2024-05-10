package entity

// Performance 实时性能指标
type Performance struct {
	CPUPercent    float64 `json:"cpu_percent"`    // CPU使用率
	MemPercent    float64 `json:"mem_percent"`    // 内存使用率
	DiskPercent   float64 `json:"disk_percent"`   // 磁盘使用率
	TCPConnection int     `json:"tcp_connection"` // TCP连接数量
	NetworkRate   float64 `json:"network_rate"`   // 网络速率，KB/S
}
