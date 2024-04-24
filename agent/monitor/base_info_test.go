package monitor

import (
	"testing"
)

func TestGetCPUInfo(t *testing.T) {
	cpuInfo, err := GetCPUInfo()
	if err != nil {
		t.Errorf("get CPU info err%s", err)
	}
	t.Logf("cpuInfo:%v", cpuInfo)
}
