package monitor

import (
	"fmt"
	"github.com/shirou/gopsutil/cpu"
	"golang.org/x/sys/unix"
	"runtime"
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
	switch os := runtime.GOOS; os {
	case "windows":
		return getDefaultCPUInfo()
		// 在Windows系统下执行特定代码
	case "darwin":
		return getMacCPUInfo()
	case "linux":
		return getDefaultCPUInfo()
	default:
		return nil, fmt.Errorf("unsupported OS")
	}
}

func getMacCPUInfo() (*CPUInfo, error) {
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

func getDefaultCPUInfo() (*CPUInfo, error) {
	cpus, err := cpu.Info()
	if err != nil {
		return nil, fmt.Errorf("get cpu info err %s", err)
	}
	if len(cpus) == 0 {
		return nil, fmt.Errorf("no cpu found")
	}
	return &CPUInfo{CPUName: cpus[0].ModelName, LogicCore: int(cpus[0].Cores), PhysicalCore: int(cpus[0].Cores * 2)}, nil

}
