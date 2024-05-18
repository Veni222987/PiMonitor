package performance

import (
	"Agent/entity"
	"context"
	"log"
	"time"

	"github.com/Veni222987/pimetric/counter"
	"github.com/shirou/gopsutil/net"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/disk"
	"github.com/shirou/gopsutil/v3/mem"
)

const (
	DURATION time.Duration = 15 * time.Second
)

// MonitorPerformance 监控性能指标
func MonitorPerformance(ctx context.Context) error {
	counter.RegisterCounter(&counter.Counter{
		Name:  "agent.send_message_counter",
		Value: 0,
	})
	for {
		select {
		case <-ctx.Done():
			{
				log.Println("被通知终止")
				return nil
			}
		default:
			{
				pfm, err := getDetail()
				if err != nil {
					return err
				}
				log.Printf("%+v", *pfm)
				if c := counter.GetCounter("agent.send_message_counter"); c != nil {
					c.Incr()
				}
				time.Sleep(DURATION)
			}
		}
	}
}

func getDetail() (*entity.Performance, error) {
	pfm := &entity.Performance{}
	// 获取CPU使用率
	cpuPercent, err := cpu.Percent(time.Second, false)
	if err != nil {
		log.Println("Failed to get CPU usage:", err)
		return nil, err
	}
	pfm.CPUPercent = avg(cpuPercent)

	// 获取内存使用率
	memStat, err := mem.VirtualMemory()
	if err != nil {
		log.Println("Failed to get memory usage:", err)
		return nil, err
	}
	pfm.MemPercent = memStat.UsedPercent

	// 获取磁盘使用率
	du, err := disk.Usage("/")
	if err != nil {
		return nil, err
	}
	pfm.DiskPercent = du.UsedPercent

	// 获取 TCP 连接数量

	connections, err := net.Connections("tcp")
	if err != nil {
		log.Fatal(err)
	}
	pfm.TCPConnection = len(connections)

	// 获取网卡传输速率
	netIO, err := net.IOCounters(true)
	if err != nil {
		log.Fatal(err)
	}
	totalBytes := uint64(0)
	for _, io := range netIO {
		totalBytes += io.BytesSent + io.BytesRecv
	}

	pfm.NetworkRate = float64(totalBytes) / float64(DURATION) // 字节/秒

	return pfm, nil
}

func avg(arr []float64) float64 {
	sum := 0.0
	for _, v := range arr {
		sum += v
	}
	return sum / float64(len(arr))
}
