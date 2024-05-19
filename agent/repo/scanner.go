package repo

import (
	"Agent/parautil"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"strconv"
	"sync"
	"time"
)

func Scan(pArr []int) []map[string]interface{} {
	totalMetrics := make([]map[string]interface{}, 0)
	lock := &sync.Mutex{}
	wg := &sync.WaitGroup{}
	// 设置等待的任务数量
	for _, port := range pArr {
		wg.Add(1)
		go func(p int) {
			defer wg.Done()
			url := "http://127.0.0.1:" + strconv.Itoa(p) + "/metrics"
			method := "GET"
			client := &http.Client{}
			req, err := http.NewRequest(method, url, nil)
			if err != nil {
				log.Println(err)
				return
			}
			res, err := client.Do(req)
			if err != nil {
				log.Println(err)
				return
			}
			// 返回结果不是200
			if res.StatusCode != http.StatusOK {
				log.Printf("Get %s,Non-200 status code: %v", url, res.StatusCode)
				res.Body.Close()
				return
			}
			// 读取返回结果
			body, err := io.ReadAll(res.Body)
			if err != nil {
				log.Printf("read body error: %v", err)
				return
			}
			// 解析返回结果为map
			var result map[string]interface{}
			err = json.Unmarshal(body, &result)
			if err != nil {
				log.Printf("unmarshal body error: %v", err)
				return
			}
			lock.Lock()
			defer lock.Unlock()
			totalMetrics = append(totalMetrics, result)
		}(port)
	}
	// 防止网络请求超时，设置1秒timeout
	parautil.WaitGroupWithTimeout(wg, time.Second)
	log.Printf("totalMetrics: %+v", totalMetrics)
	return totalMetrics
}
