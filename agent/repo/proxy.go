package repo

import (
	"Agent/entity"
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"strconv"
)

var (
	url string = "http://120.77.76.40:8000/api/v1/agents/services/info"
)

// 这里不共用client是为了能够实现真正的并发

// UploadPerformance 上传性能数据
func UploadPerformance(id int, pfmc *entity.Performance) error {
	// 组装measurement数据
	data := entity.Measurement{
		Measurement: "performance_" + strconv.Itoa(id),
		Precision:   "s",
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
			},
		},
	}
	jsonBytes, err := json.Marshal(data)
	if err != nil {
		log.Printf("fail to encode data:", err)
		return err
	}
	// 将字节切片转换为io.Reader接口
	reader := bytes.NewReader(jsonBytes)
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
	log.Println("performance send successfully")
	return nil
}
