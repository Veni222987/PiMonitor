package monitor

import (
	"Agent/entity"
	"Agent/logic/pilog"
	"context"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/disk"
	"github.com/shirou/gopsutil/v3/mem"
	"time"
)

const (
	DURATION time.Duration = 1 * time.Second
)

func GetPerformance(ctx context.Context) error {
	for {
		select {
		case <-ctx.Done():
			{
				pilog.Println("被通知终止")
				return nil
			}
		default:
			{
				pfm := entity.Performance{}
				// 获取CPU使用率
				cpuPercent, err := cpu.Percent(time.Second, false)
				if err != nil {
					pilog.Println("Failed to get CPU usage:", err)
					return err
				}
				pfm.CPUPercent = avg(cpuPercent)

				// 获取内存使用率
				memStat, err := mem.VirtualMemory()
				if err != nil {
					pilog.Println("Failed to get memory usage:", err)
					return err
				}
				pfm.MemPercent = memStat.UsedPercent

				// 获取磁盘使用率
				du, err := disk.Usage("/")
				if err != nil {
					return err
				}
				pfm.DiskPercent = du.UsedPercent

				time.Sleep(DURATION)

				// TODO 这里不要return了，直接调用接口传出去就行
			}
		}
	}
}

func avg(arr []float64) float64 {
	sum := 0.0
	for _, v := range arr {
		sum += v
	}
	return sum / float64(len(arr))
}
