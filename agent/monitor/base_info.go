package monitor

import (
	"fmt"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/mem"
	"github.com/shirou/gopsutil/v3/net"
	"log"
	"runtime"
	"sync"
)

var (
	computerInfo *ComputerInfo
	once         sync.Once
)

type ComputerInfo struct {
	CPU         *CPUInfo
	Memory      uint64   // 内存，单位为GB
	NetworkCard []string // 网卡名称，有需要可以获取网卡的MAC地址
	OS          string   // 操作系统
}

type CPUInfo struct {
	CPUName   string  // CPU名称
	Core      int     // CPU核心数
	Frequency float64 // 频率 MHZ
}

func getCPUInfo() (*CPUInfo, error) {
	cpus, err := cpu.Info()
	if err != nil {
		return nil, fmt.Errorf("get cpu info err: %s", err)
	}
	if len(cpus) == 0 {
		return nil, fmt.Errorf("no cpu found")
	}
	return &CPUInfo{CPUName: cpus[0].ModelName, Core: int(cpus[0].Cores) * len(cpus), Frequency: cpus[0].Mhz}, nil
}

func getMemory() (uint64, error) {
	memStat, err := mem.VirtualMemory()
	if err != nil {
		return 0, fmt.Errorf("get memory info error: %s", err)
	}
	return memStat.Total, nil
}

func getNetCard() ([]string, error) {
	interfaces, err := net.Interfaces()
	if err != nil {
		return nil, fmt.Errorf("get net card error: %s", err)
	}
	ret := make([]string, 0)
	for _, interf := range interfaces {
		ret = append(ret, interf.Name)
	}
	return ret, nil
}

// GetComputerInfo 获取计算机基础信息，代码只执行一次
func GetComputerInfo() (*ComputerInfo, error) {
	once.Do(func() {
		cpuInfo, err := getCPUInfo()
		if err != nil {
			log.Fatal(err)
		}

		mem, err := getMemory()
		if err != nil {
			log.Fatal(err)
		}

		netcard, err := getNetCard()
		if err != nil {
			log.Fatal(err)
		}

		computerInfo = &ComputerInfo{
			CPU:         cpuInfo,
			NetworkCard: netcard,
			Memory:      mem,
			OS:          runtime.GOOS,
		}
	})

	return computerInfo, nil
}
