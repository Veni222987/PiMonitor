package main

import (
	pimonitor "Agent/logic/monitor"
	"Agent/logic/pilog"
	"context"
	"time"
)

func main() {

	// 主函数创建一个context，传入三个协程中
	ctx, cancel := context.WithCancel(context.Background())

	go func() {
		info, err := pimonitor.GetComputerInfoWithContext(ctx)
		if err != nil {
			cancel()
		}
		pilog.Printf("CPU: %v\n", *info.CPU)
		pilog.Printf("Memory: %d GB\n", info.Memory/(1<<30))
		pilog.Printf("Disk: %d GB\n", info.Disk/(1<<30))
		pilog.Printf("Net card: %v\n", info.NetworkCard)
		pilog.Printf("Operating system: %s", info.OS)
	}()

	go pimonitor.GetPerformance(ctx)

	for range time.Tick(time.Second) {
		select {
		case <-ctx.Done():
			pilog.Println("Context done. Exiting...")
			return
		default:
			pilog.Println("monitoring")
			time.Sleep(time.Second)
		}
	}
}
