package config

import "github.com/IBM/sarama"

const (
	SASL_USER     = "user"
	SASL_PASSWORD = "ZAQ1@wsx"
	BROKER        = "120.77.76.40:9093"
)

func GetDefaultKafkaConfig() *sarama.Config {
	config := sarama.NewConfig()
	config.Producer.Return.Successes = true
	config.Net.SASL.Enable = true
	config.Net.SASL.User = SASL_USER
	config.Net.SASL.Password = SASL_PASSWORD
	config.Net.SASL.Mechanism = sarama.SASLTypePlaintext
	return config
}
