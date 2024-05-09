package main

import (
	pimonitor "Agent/logic/monitor"
	"Agent/logic/pilog"
	"context"
	"fmt"
	"log"
	"time"

	"github.com/IBM/sarama"
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

	go func() {
		err := pimonitor.GetPerformance(ctx)
		if err != nil {
			pilog.Println("get performance error")
		}
	}()

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

func test() {
	config := sarama.NewConfig()
	config.Producer.Return.Successes = true
	config.Net.SASL.Enable = true
	config.Net.SASL.User = "user"
	config.Net.SASL.Password = "ZAQ1@wsx"
	config.Net.SASL.Mechanism = sarama.SASLTypePlaintext

	producer, err := sarama.NewSyncProducer([]string{"120.77.76.40:9093"}, config)
	if err != nil {
		log.Fatalln("Failed to create producer:", err)
	}
	defer producer.Close()

	message := &sarama.ProducerMessage{
		Topic: "test",
		Value: sarama.StringEncoder("Hello, Kafka!"),
	}

	partition, offset, err := producer.SendMessage(message)
	if err != nil {
		log.Println("Failed to send message:", err)
	} else {
		fmt.Printf("Message sent successfully! Partition: %d, Offset: %d\n", partition, offset)
	}
}
