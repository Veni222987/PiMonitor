package main

import (
	"fmt"
	"github.com/shirou/gopsutil/cpu"
	"github.com/shirou/gopsutil/disk"
	"github.com/shirou/gopsutil/mem"
	"time"
)

func main() {
	for {
		// 获取CPU使用率
		cpuPercent, _ := cpu.Percent(time.Second, false)
		fmt.Printf("CPU 使用率: %.2f%%\n", cpuPercent[0])

		// 获取磁盘使用情况
		diskUsage, _ := disk.Usage("/")
		fmt.Printf("磁盘使用情况: 已使用 %.2f GB，总共 %.2f GB，可用 %.2f GB\n",
			float64(diskUsage.Used)/(1024*1024*1024),
			float64(diskUsage.Total)/(1024*1024*1024),
			float64(diskUsage.Free)/(1024*1024*1024))

		// 获取内存使用情况
		memInfo, _ := mem.VirtualMemory()
		fmt.Printf("内存使用情况: 已使用 %.2f GB，总共 %.2f GB，可用 %.2f GB\n",
			float64(memInfo.Used)/(1024*1024*1024),
			float64(memInfo.Total)/(1024*1024*1024),
			float64(memInfo.Available)/(1024*1024*1024))

		time.Sleep(5 * time.Second) // 每隔5秒更新一次信息
	}
}
