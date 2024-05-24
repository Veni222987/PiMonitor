package entity

type Point struct {
	Tag   map[string]string      `json:"tag"`
	Field map[string]interface{} `json:"field"`
	Time  int64                  `json:"time"`
}

type Measurement struct {
	Measurement string  `json:"measurement"`
	Precision   string  `json:"precision"`
	Points      []Point `json:"points"`
}
