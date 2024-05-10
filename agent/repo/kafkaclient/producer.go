package kafkaclient

import (
	"Agent/config"
	"log"

	"github.com/IBM/sarama"
)

var producer sarama.SyncProducer

func InitProducer() {
	var err error
	producer, err = sarama.NewSyncProducer([]string{config.BROKER}, config.GetDefaultKafkaConfig())
	if err != nil {
		log.Fatal(err)
	}
}

func CloseProducer() {
	producer.Close()
}

func SendMessage(topic string, key []byte, message []byte) (int32, int64) {
	msg := &sarama.ProducerMessage{
		Topic: topic,
		Key:   sarama.ByteEncoder(key),
		Value: sarama.ByteEncoder(message),
	}
	p, o, err := producer.SendMessage(msg)
	if err != nil {
		log.Fatal(err)
	}
	return p, o
}
