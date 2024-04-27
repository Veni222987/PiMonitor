package monitor

import (
	"context"
	"fmt"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/mem"
	"log"
	"time"
)

const (
	DURATION time.Duration = 1 * time.Second
)

func GetPerformance(ctx context.Context) {
	for {
		select {
		case <-ctx.Done():
			{
				log.Println("被通知终止")
				return
			}
		default:
			{
				// Retrieve CPU usage percentage
				cpuPercent, err := cpu.Percent(time.Second, false)
				if err != nil {
					fmt.Println("Failed to get CPU usage:", err)
					return
				}
				fmt.Printf("CPU Usage: %.2f%%\n", cpuPercent[0])

				// Retrieve memory usage statistics
				memStat, err := mem.VirtualMemory()
				if err != nil {
					fmt.Println("Failed to get memory usage:", err)
					return
				}
				fmt.Printf("Memory Usage: %.2f%%\n", memStat.UsedPercent)

				time.Sleep(DURATION)
			}
		}
	}
}
