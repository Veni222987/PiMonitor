package entity

// ComputerInfo 计算机信息结构体
type ComputerInfo struct {
	CPU         *CPUInfo `json:"cpu"`          // CPU	信息
	Memory      uint64   `json:"memory"`       // 内存，单位为B
	Disk        uint64   `json:"disk"`         // 磁盘大小， 单位为B
	NetworkCard []string `json:"network_card"` // 网卡名称，有需要可以获取网卡的MAC地址
	OS          string   `json:"os"`           // 操作系统
}

// CPUInfo CPU信息结构体
type CPUInfo struct {
	CPUName   string  `json:"cpu_name"`  // CPU名称
	Core      int     `json:"core"`      // CPU核心数
	Frequency float64 `json:"frequency"` // 频率 MHZ
}
