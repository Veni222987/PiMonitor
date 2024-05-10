package config

import (
	"io"
	"log"
	"os"
)

var logFile os.File

func InitLog() {
	// 日志同时输出到控制台和文本文件
	logFile, err := os.OpenFile("./agent.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0644)
	if err != nil {
		log.Fatal("open log file failed,err:", err)
	}

	multiWriter := io.MultiWriter(os.Stdout, logFile)
	log.SetOutput(multiWriter)
}

func CloseLogFile() {
	logFile.Close()
}
