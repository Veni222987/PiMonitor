package monitor

import (
	"fmt"
	"github.com/shirou/gopsutil/v3/cpu"
)

type ComputerInfo struct {
	CPU         CPUInfo
	Memory      int      // 内存，单位为GB
	NetworkCard []string // 网卡名称，有需要可以获取网卡的MAC地址
	OS          string   // 操作系统
}

type CPUInfo struct {
	CPUName   string  // CPU名称
	Core      int     // CPU核心数
	Frequency float64 // 频率 MHZ
}

func GetCPUInfo() (*CPUInfo, error) {
	cpus, err := cpu.Info()
	if err != nil {
		return nil, fmt.Errorf("get cpu info err: %s", err)
	}
	if len(cpus) == 0 {
		return nil, fmt.Errorf("no cpu found")
	}
	return &CPUInfo{CPUName: cpus[0].ModelName, Core: int(cpus[0].Cores), Frequency: cpus[0].Mhz}, nil

}
