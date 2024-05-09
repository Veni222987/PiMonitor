package pilog

import (
	"log"
	"os"
	"sync"
)

var (
	once sync.Once
	l    *log.Logger
)

// getLogger 返回重定向log对象的单例
func getLogger() *log.Logger {
	once.Do(func() {
		file, err := os.OpenFile("log.txt", os.O_CREATE|os.O_WRONLY|os.O_TRUNC, 0666)
		if err != nil {
			log.Fatal(err)
		}

		l = log.New(file, "", log.Ltime)
	})

	return l
}

// Println 打印行
func Println(strs ...any) {
	getLogger().Print(strs...)
}

// Printf 格式化打印
func Printf(str string, args ...any) {
	getLogger().Printf(str, args...)
}
