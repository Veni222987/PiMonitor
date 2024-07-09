package repo

import (
	"Agent/parautil"
	"log"
	"net/http"
	"sync"
	"time"

	"github.com/Veni222987/pimetric"
)

func Scan(pArr []int) []*pimetric.Metricx {
	rsp := make([]*pimetric.Metricx, 0)
	wg := &sync.WaitGroup{}
	mutex := &sync.Mutex{}
	// 设置等待的任务数量
	for _, port := range pArr {
		wg.Add(1)
		go func(p int) {
			defer wg.Done()
			// ... 处理部分代码
			req, err := http.NewRequest(method, url, nil)
			if err != nil {
				log.Println(err)
				return
			}
			res, err := client.Do(req)
			// ......省略部分代码
			mutex.Lock()
			defer mutex.Unlock()
			rsp = append(rsp, result)
		}(port)
	}
	// 防止网络请求超时，设置1秒timeout
	parautil.WaitGroupWithTimeout(wg, time.Second)
	return rsp
}
