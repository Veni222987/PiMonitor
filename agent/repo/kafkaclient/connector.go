package kafkaclient

import (
	"fmt"
	"github.com/IBM/sarama"
	"log"
	"os"
)

type KafkaProxy struct {
	Config sarama.Config
}

func NewKafkaProxy(uname string, passwd string) *KafkaProxy {
	kproxy := &KafkaProxy{*sarama.NewConfig()}
	kproxy.Config.Producer.Return.Successes = true
	//kproxy.Config.Producer.Return.Successes = true
	//kproxy.Config.Producer.RequiredAcks = sarama.WaitForAll
	//kproxy.Config.Producer.Retry.Max = 5
	//kproxy.Config.Producer.Partitioner = sarama.NewRandomPartitioner
	kproxy.Config.Net.SASL.Enable = true
	kproxy.Config.Net.SASL.User = "user"
	kproxy.Config.Net.SASL.Password = "ZAQ1@wsx"
	kproxy.Config.Net.SASL.Mechanism = sarama.SASLTypePlaintext

	return kproxy
}

func (kproxy *KafkaProxy) CreateProducer() {
	producer, err := sarama.NewSyncProducer([]string{"127.77.76.40:9093"}, &kproxy.Config)
	if err != nil {
		fmt.Println("Failed to create producer:", err)
		os.Exit(1)
	}
	defer producer.Close()

	message := &sarama.ProducerMessage{
		Topic: "my_topic",                            // 要写入的主题名称
		Value: sarama.StringEncoder("Hello, Kafka!"), // 消息内容
	}

	// 发送消息
	partition, offset, err := producer.SendMessage(message)
	if err != nil {
		log.Fatalf("Failed to send message: %s", err)
	}

	log.Printf("Message sent, partition: %d, offset: %d", partition, offset)
}
