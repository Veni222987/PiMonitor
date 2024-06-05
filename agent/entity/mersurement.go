package entity

type Point struct {
	Tag       map[string]string      `json:"tags"`
	Field     map[string]interface{} `json:"fields"`
	Timestamp int64                  `json:"timestamp"`
}

type Measurement struct {
	Measurement string  `json:"measurement"`
	Precision   string  `json:"precision"`
	Points      []Point `json:"points"`
}
