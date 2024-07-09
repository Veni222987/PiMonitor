package baseinfo

import (
	"context"
	"testing"
)

func TestGetCPUInfo(t *testing.T) {
	cpuInfo, err := getCPUInfo()
	if err != nil {
		t.Errorf("get CPU info err%s", err)
	}
	t.Logf("cpuInfo:%v", cpuInfo)
}

func TestGetComputerInfo(t *testing.T) {
	computerInfo, err := GetComputerInfo()
	if err != nil {
		t.Errorf("get computer info err%s", err)
	}
	t.Logf("cpu:%+v", computerInfo.CPU)
	t.Logf("computer:%+v", computerInfo)
}

func TestGetComputerInfoWithContext(t *testing.T) {
	testCases := []struct {
		name    string
		wantErr bool
	}{
		{
			name:    "windows",
			wantErr: false,
		},
		{
			name:    "darwin",
			wantErr: false,
		},
		{
			name:    "linux",
			wantErr: false,
		},
	}

	computerInfo, err := GetComputerInfoWithContext(context.Background())
	if err != nil {
		t.Errorf("get computer info err%s", err)
	}
	t.Logf("cpu:%+v", computerInfo.CPU)
	t.Logf("computer:%+v", computerInfo)
}
