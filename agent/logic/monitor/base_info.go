package monitor

import (
	"Agent/entity"
	"Agent/logic/multios"
	"context"
	"fmt"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/mem"
	"github.com/shirou/gopsutil/v3/net"
	"runtime"
	"sync"
)

var (
	computerInfo *entity.ComputerInfo
	once         sync.Once
)

func getCPUInfo() (*entity.CPUInfo, error) {
	cpus, err := cpu.Info()
	if err != nil {
		return nil, fmt.Errorf("get cpu info err: %s", err)
	}
	if len(cpus) == 0 {
		return nil, fmt.Errorf("no cpu found")
	}
	return &entity.CPUInfo{CPUName: cpus[0].ModelName, Core: int(cpus[0].Cores) * len(cpus), Frequency: cpus[0].Mhz}, nil
}

func getMemory() (uint64, error) {
	memStat, err := mem.VirtualMemory()
	if err != nil {
		return 0, fmt.Errorf("get memory info error: %s", err)
	}
	return memStat.Total, nil
}

func getDisk() (uint64, error) {
	size, err := multios.GetTotalDisk()
	if err != nil {
		return 0, err
	}
	return size, nil
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

// GetComputerInfoWithContext 获取计算机基础信息，如果基础信息都获取不到，由context终止其他协程
func GetComputerInfoWithContext(ctx context.Context) (*entity.ComputerInfo, error) {
	ret, err := GetComputerInfo()
	if err != nil {
		return nil, err
	}
	return ret, nil
}

func GetComputerInfo() (*entity.ComputerInfo, error) {
	cpuInfo, err := getCPUInfo()
	if err != nil {
		return nil, err
	}

	memory, err := getMemory()
	if err != nil {
		return nil, err
	}

	diskTotal, err := getDisk()
	if err != nil {
		return nil, err
	}

	netcard, err := getNetCard()
	if err != nil {
		return nil, err
	}

	computerInfo = &entity.ComputerInfo{
		CPU:         cpuInfo,
		NetworkCard: netcard,
		Disk:        diskTotal,
		Memory:      memory,
		OS:          runtime.GOOS,
	}

	return computerInfo, nil
}
