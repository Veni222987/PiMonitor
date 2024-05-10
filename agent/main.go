package main

import (

	"Agent/config"
	pimonitor "Agent/logic/monitor"
	"context"
	"log"

	"time"
)

func main() {

	config.InitLog()
	defer config.CloseLogFile()


	// 主函数创建一个context，传入三个协程中
	ctx, cancel := context.WithCancel(context.Background())

	go func() {
		info, err := pimonitor.GetComputerInfoWithContext(ctx)
		if err != nil {
			cancel()
		}

		log.Printf("CPU: %v\n", *info.CPU)
		log.Printf("Memory: %d GB\n", info.Memory/(1<<30))
		log.Printf("Disk: %d GB\n", info.Disk/(1<<30))
		log.Printf("Net card: %v\n", info.NetworkCard)
		log.Printf("Operating system: %s", info.OS)
	}()

	go func() {
		err := pimonitor.GetPerformance(ctx)
		if err != nil {
			log.Println("get performance error")
		}
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
