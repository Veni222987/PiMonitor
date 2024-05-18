package metric

import (
	"fmt"

	"github.com/veni222987/pimetric/counter"
)

// 如何采集监控指标：遍历端口访问/metric路由，查看是否实现
func test() {
	c := counter.Counter{
		MetricName: "test",
		Value:      1,
	}
	fmt.Println(c)
}
