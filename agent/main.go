package main

import (
	"Agent/monitor"
	"context"
	"fmt"
	"log"
	"time"
)

func main() {
	// 主函数创建一个context，传入三个协程中
	ctx, cancel := context.WithCancel(context.Background())

	go func() {
		_, err := monitor.GetComputerInfoWithContext(ctx)
		if err != nil {
			cancel()
		}
	}()

	go monitor.GetPerformance(ctx)

	for range time.Tick(time.Second) {
		select {
		case <-ctx.Done():
			fmt.Println("Context done. Exiting...")
			return
		default:
			log.Println("monitoring, time:" + time.Now().String())
			time.Sleep(time.Second)
		}
	}
}
