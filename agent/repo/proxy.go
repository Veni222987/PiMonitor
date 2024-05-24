package repo

import (
	"Agent/entity"
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"strconv"
	"time"
)

var (
	url string = "http://120.77.76.40:8000/api/v1/agents/services/info"
)

// 这里不共用client是为了能够实现真正的并发
func sendBytes(reader io.Reader) error {
	client := &http.Client{}
	req, err := http.NewRequest("POST", url, reader)
	if err != nil {
		log.Println(err)
		return err
	}
	req.Header.Add("Content-Type", "application/json")
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
func UploadPerformance(id int, pfmc *entity.Performance) error {
	// 组装measurement数据
	data := entity.Measurement{
		Measurement: "performance_" + strconv.Itoa(id),
		Precision:   "S",
		Points: []entity.Point{
			{
				Tag: map[string]string{
					"id": strconv.Itoa(id),
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
	// 将字节切片转换为io.Reader接口
	reader := bytes.NewReader(jsonBytes)
	if err := sendBytes(reader); err != nil {
		log.Printf("fail to send data,%v", err)
		return err
	}
	return nil
}

func UploadMetrics(id int, metrics *entity.Metrics) error {
	data := make([]entity.Measurement, 0)
	for appname, metric := range metrics.MetricsMap {
		if metricMap, ok := metric.(map[string]any); ok {
			measurement := entity.Measurement{
				Measurement: appname + "_" + strconv.Itoa(id),
				Precision:   "S",
				Points:      make([]entity.Point, 0),
			}
			for tag, detailMap := range metricMap {
				if detailMap, ok := detailMap.(map[string]any); ok {
					measurement.Points = append(measurement.Points, entity.Point{
						Tag:       map[string]string{"metric_type": tag},
						Field:     detailMap,
						Timestamp: metrics.Timestamp,
					})
				} else {
					println("detailMap type assertion error")
				}
			}
			data = append(data, measurement)
		} else {
			log.Println("metricMap type assertion error")
		}
	}
	log.Printf("Measurements: %v", data)
	jsonBytes, err := json.Marshal(data)
	if err != nil {
		log.Printf("fail to encode data:%v", err)
		return err
	}
	reader := bytes.NewReader(jsonBytes)
	if err := sendBytes(reader); err != nil {
		log.Printf("fail to send data,%v", err)
		return err
	}
	return nil
}
