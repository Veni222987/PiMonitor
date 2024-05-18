package performance

import (
	"Agent/entity"
	"testing"
)

func TestGetDetail(t *testing.T) {
	testcases := []struct {
		name    string
		want    *entity.Performance
		wantErr bool
	}{
		{
			name:    "test",
			want:    &entity.Performance{},
			wantErr: false,
		},
	}
	for _, tt := range testcases {
		t.Run(tt.name, func(t *testing.T) {
			got, err := getDetail()
			if (err != nil) != tt.wantErr {
				t.Errorf("GetDetail() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if got == nil {
				t.Errorf("GetDetail() got= %v, want %v", got, tt.want)
			}
			t.Logf("%#v", got)
		})
	}
}
