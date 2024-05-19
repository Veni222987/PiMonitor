package parautil

import (
	"sync"
	"time"
)

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
