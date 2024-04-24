package monitor

import (
	"fmt"
	"github.com/shirou/gopsutil/cpu"
	"golang.org/x/sys/unix"
)

type ComputerInfo struct {
	CPU         CPUInfo
	Memory      int      // 内存，单位为GB
	NetworkCard []string //网卡名称，有需要可以获取网卡的MAC地址
	OS          string   //操作系统
}

type CPUInfo struct {
	CPUName      string //CPU名称
	LogicCore    int    // CPU核心数
	PhysicalCore int    // CPU线程数
}

func GetCPUInfo() (*CPUInfo, error) {
	// gopsutil无法实现全兼容，这里需要手动实现
	cpuName, err := unix.Sysctl("machdep.cpu.brand_string")
	if err != nil {
		return nil, fmt.Errorf("fail to get cpu name: %s", err)
	}

	logicCore, err := cpu.Counts(true)
	if err != nil {
		return nil, fmt.Errorf("fail to get cpu logic core: %s", err)
	}

	physicalCore, err := cpu.Counts(false)
	if err != nil {
		return nil, fmt.Errorf("fail to get cpu physical core: %s", err)
	}

	return &CPUInfo{CPUName: cpuName, LogicCore: logicCore, PhysicalCore: physicalCore}, nil
}
