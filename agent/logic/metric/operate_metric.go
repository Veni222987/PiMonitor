package metric

import (
	"Agent/entity"
	"Agent/repo"
	"context"
	"log"
	"time"

	"github.com/shirou/gopsutil/net"
)

const (
	DURATION time.Duration = 15 * time.Second
)

func MonitorMetric(ctx context.Context) error {
	for {
		select {
		case <-ctx.Done():
			return nil
		default:
			{
				// 监控指标，上报服务器
				portArr := make([]int, 0)
				connections, err := net.Connections("tcp")
				for _, conn := range connections {
					if conn.Status == "LISTEN" {
						portArr = append(portArr, int(conn.Laddr.Port))
					}
				}
				if err != nil {
					log.Println(err)
					continue
				}

				data := &entity.Metrics{
					Timestamp:  time.Now().Unix(),
					MetricsMap: repo.Scan(portArr),
				}
				// 发送metrics数据
				repo.UploadMetrics(8888888, data)
				log.Printf("Metrics data: %+v", data)
			}
		}
		time.Sleep(DURATION)
	}
}
