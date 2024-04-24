package monitor

// 电脑数据结构
import (
	"fmt"
	"github.com/shirou/gopsutil/cpu"
)

type ComputerInfo struct {
	CPU         CPUInfo
	Memory      int      // 内存，单位为GB
	NetworkCard []string //网卡名称，有需要可以获取网卡的MAC地址
	OS          string   //操作系统
}

type CPUInfo struct {
	CPUName   string //CPU名称
	CPUCore   int    // CPU核心数
	CPUThread int    // CPU线程数
}

func GetCPUInfo() (CPUInfo, error) {
	info, err := cpu.Info()
	fmt.Println(info)
	cpuCount, err := cpu.Counts(true)
	if err != nil {
		return CPUInfo{"1", cpuCount, 1}, fmt.Errorf("get cpuInfo error:" + err.Error())
	}
	return CPUInfo{
		"nil",
		cpuCount,
		cpuCount * 2,
	}, nil
}
