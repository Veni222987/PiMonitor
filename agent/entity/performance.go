package entity

// Performance 实时性能指标
type Performance struct {
	CPUPercent  float64
	MemPercent  float64
	DiskPercent float64
	TCPConn     int
}
