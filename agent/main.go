package main

import (
	"Agent/config"
	"Agent/logic/baseinfo"
	"Agent/logic/performance"
	"context"
	"log"
	"time"

	"github.com/Veni222987/pimetric"
)

func main() {
	config.InitLog()
	defer config.CloseLogFile()
	// 主函数创建一个context，传入三个协程中
	ctx, cancel := context.WithCancel(context.Background())

	go func() {
		info, err := baseinfo.GetComputerInfoWithContext(ctx)
		if err != nil {
			cancel()
		}
		log.Printf("computer base info: %#v", info)
	}()

	go func() {
		err := performance.MonitorPerformance(ctx)
		if err != nil {
			log.Println("get performance error")
		}
	}()

	go func() {
		pimetric.ExportMetrics()
	}()

	for range time.Tick(time.Second) {
		select {
		case <-ctx.Done():
			log.Println("Context done. Exiting...")
			return
		default:
			time.Sleep(time.Second)
		}
	}
}
