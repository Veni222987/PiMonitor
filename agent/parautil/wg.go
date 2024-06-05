package parautil

import (
	"sync"
	"time"
)

// WaitGroupWithTimeout 创建一个带有timeout的wg，返回值：
// true：wg正常完成，false：wg超时
func WaitGroupWithTimeout(wg *sync.WaitGroup, timeout time.Duration) bool {
	c := make(chan struct{})
	go func() {
		defer close(c)
		wg.Wait()
	}()

	select {
	case <-c:
		return true // completed normally
	case <-time.After(timeout):
		return false // timed out
	}
}
