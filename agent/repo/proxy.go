package repo

import (
	"Agent/entity"
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"time"

	"github.com/Veni222987/pimetric"
	"github.com/Veni222987/pimetric/pimstore"
)

var (
	url string = "http://120.77.76.40:8000/api/v1/agents/services/info"
)

var PIM_AGENT_ID = os.Getenv("PIM_AGENT_ID")
var PIM_AGENT_TOKEN = os.Getenv("PIM_AGENT_TOKEN")

// 这里不共用client是为了能够实现真正的并发
func sendBytes(bts []byte) error {
	start := time.Now()
	defer func() {
		pimstore.HistogramOf("send_data_duration").AddPoint(float64(time.Since(start).Milliseconds()))
	}()
	reader := bytes.NewReader(bts)
	client := &http.Client{}
	req, err := http.NewRequest("POST", url, reader)
	if err != nil {
		log.Println(err)
		return err
	}
	req.Header.Add("Content-Type", "application/json")
	req.Header.Add("Authorization", PIM_AGENT_TOKEN)
	res, err := client.Do(req)
	if err != nil {
		log.Println(err)
		return err
	}
	defer res.Body.Close()
	if res.StatusCode != 200 {
		log.Println("fail to upload performance data")
		return err
	}
	log.Println("Measurement send successfully")
	return nil
}

// UploadPerformance 上传性能数据
func UploadPerformance(pfmc *entity.Performance) error {
	// 组装measurement数据
	data := entity.Measurement{
		Measurement: "performance_" + PIM_AGENT_ID,
		Precision:   "S",
		Points: []entity.Point{
			{
				Tag: map[string]string{
					"id": PIM_AGENT_ID,
				},
				Field: map[string]interface{}{
					"cpu_percent":    pfmc.CPUPercent,
					"mem_percent":    pfmc.MemPercent,
					"disk_percent":   pfmc.DiskPercent,
					"tcp_connection": pfmc.TCPConnection,
					"network_rate":   pfmc.NetworkRate,
				},
				Timestamp: time.Now().Unix(),
			},
		},
	}
	log.Printf("数据：%v", data)
	jsonBytes, err := json.Marshal([]entity.Measurement{data})
	if err != nil {
		log.Printf("fail to encode data:%v", err)
		return err
	}
	if err := sendBytes(jsonBytes); err != nil {
		log.Printf("fail to send data,%v", err)
		return err
	}
	return nil
}

func UploadMetrics(metrics []*pimetric.Metricx) error {
	data := make([]entity.Measurement, 0)
	for _, appMetrics := range metrics {
		m := entity.Measurement{
			Measurement: appMetrics.AppName + "_" + PIM_AGENT_ID,
			Precision:   "S",
			Points:      make([]entity.Point, 0),
		}
		// TODO 这里可以改并行
		// 处理counter
		for k, v := range appMetrics.CounterMap {
			m.Points = append(m.Points, entity.Point{
				Tag:       map[string]string{"metric_type": "counter"},
				Field:     map[string]interface{}{k: v.GetValue()},
				Timestamp: v.GetTimestamp(),
			})
		}
		// 处理gauge
		for k, v := range appMetrics.GaugeMap {
			m.Points = append(m.Points, entity.Point{
				Tag:       map[string]string{"metric_type": "gauge"},
				Field:     map[string]interface{}{k: v.GetValue()},
				Timestamp: v.GetTimestamp(),
			})
		}
		// 处理histogram
		for k, v := range appMetrics.HistogramMap {
			for _, histoPoint := range v.Value {
				m.Points = append(m.Points, entity.Point{
					Tag:       map[string]string{"metric_type": "histogram"},
					Field:     map[string]interface{}{k: histoPoint.Number},
					Timestamp: histoPoint.Timestamp,
				})
			}
		}
		data = append(data, m)
	}

	log.Printf("Measurements: %+v", data)
	jsonBytes, err := json.Marshal(data)
	if err != nil {
		log.Printf("fail to encode data:%v", err)
		return err
	}
	if err := sendBytes(jsonBytes); err != nil {
		log.Printf("fail to send data,%v", err)
		return err
	}
	return nil
}

// Register 注册机器，上传基础信息
func Register(info entity.ComputerInfo) error {
	log.Println("AGENT_ID:" + PIM_AGENT_ID)
	log.Println("AGENT_TOKEN:" + PIM_AGENT_TOKEN)
	jsonBytes, err := json.Marshal(info)
	if err != nil {
		log.Printf("fail to encode data:%v", err)
		return err
	}
	if err := sendBytes(jsonBytes); err != nil {
		log.Printf("fail to send data,%v", err)
		return err
	}
	return nil
}
