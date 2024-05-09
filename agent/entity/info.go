package entity

// ComputerInfo 计算机信息结构体
type ComputerInfo struct {
	CPU         *CPUInfo // CPU	信息
	Memory      uint64   // 内存，单位为B
	Disk        uint64   // 磁盘大小， 单位为B
	NetworkCard []string // 网卡名称，有需要可以获取网卡的MAC地址
	OS          string   // 操作系统
}

// CPUInfo CPU信息结构体
type CPUInfo struct {
	CPUName   string  // CPU名称
	Core      int     // CPU核心数
	Frequency float64 // 频率 MHZ
}
