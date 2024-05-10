package kafkaclient

import "testing"

func TestProducer(t *testing.T) {
	InitProducer()
	defer CloseProducer()
	p, o := SendMessage("test", []byte("test"), []byte("test"))
	t.Log("partition和偏移量：", p, o)
}
