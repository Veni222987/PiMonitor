package kafkaclient

import "testing"

func TestNewKafkaProxy(t *testing.T) {
	p := NewKafkaProxy("", "")
	p.CreateProducer()
}
