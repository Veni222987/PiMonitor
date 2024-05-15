package metric

import (
	"fmt"
	"io"
	"net/http"
	"testing"
)

func TestGetService(t *testing.T) {
	url := "http://localhost:8080/actuator/metrics" // 替换为实际的Spring Boot应用程序的URL

	resp, err := http.Get(url)
	if err != nil {
		fmt.Println("Request failed:", err)
		return
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		fmt.Println("Failed to read response body:", err)
		return
	}

	fmt.Println("Response Body:", string(body))
}
