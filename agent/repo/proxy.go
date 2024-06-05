package repo

import (
	"Agent/entity"
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"
	"strconv"
	"time"

	"github.com/Veni222987/pimetric"
	"github.com/Veni222987/pimetric/pimstore"
)

var (
	DATA_URL     string = "http://120.77.76.40:8000/api/v1/agents/services/info"
	REGISTER_URL string = "http://120.77.76.40:8000/api/v1/agents/info"
	AGENT_ID     uint64
)

var PIM_AGENT_TOKEN = os.Getenv("PIM_AGENT_TOKEN")

// UploadPerformance 上传性能数据
func UploadPerformance(pfmc *entity.Performance) error {
	// 组装measurement数据
	data := entity.Measurement{
		Measurement: "performance_" + strconv.Itoa(int(AGENT_ID)),
		Precision:   "S",
		Points: []entity.Point{
			{
				Tag: map[string]string{
					"id": strconv.Itoa(int(AGENT_ID)),
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
			Measurement: appMetrics.AppName + "_" + strconv.Itoa(int(AGENT_ID)),
			Precision:   "MS",
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
	log.Println("AGENT_TOKEN:" + PIM_AGENT_TOKEN)
	jsonBytes, err := json.Marshal(info)
	if err != nil {
		log.Println(err)
		return err
	}
	reader := bytes.NewReader(jsonBytes)
	client := &http.Client{}
	req, err := http.NewRequest("POST", REGISTER_URL, reader)
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

	// 读取响应体
	body, err := io.ReadAll(res.Body)
	if err != nil {
		log.Fatal(err)
	}

	type registerResBody struct {
		Code    uint64            `json:"code"`
		Message string            `json:"message"`
		Data    map[string]uint64 `json:"data"`
	}
	// 反序列化为JSON
	var responseBody registerResBody
	err = json.Unmarshal(body, &responseBody)
	if err != nil {
		log.Fatal(err)
	}
	AGENT_ID = responseBody.Data["id"]
	log.Println("Register successfully")
	return nil
}

// 这里不共用client是为了能够实现真正的并发
func sendBytes(bts []byte) error {
	start := time.Now()
	defer func() {
		pimstore.HistogramOf("send_data_duration").AddPoint(float64(time.Since(start).Milliseconds()))
	}()
	// 创建一个URL对象，并指定基础URL
	u, err := url.Parse(DATA_URL)
	if err != nil {
		return err
	}
	// 查询参数map
	queryParams := url.Values{}
	queryParams.Set("agentID", strconv.Itoa(int(AGENT_ID)))
	// 将查询参数添加到URL对象中
	u.RawQuery = queryParams.Encode()
	reader := bytes.NewReader(bts)
	client := &http.Client{}
	req, err := http.NewRequest("POST", u.String(), reader)
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
