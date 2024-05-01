//go:build unix || linux

package multios

import (
	"fmt"
	"syscall"
)

func GetTotalDisk() (uint64, error) {
	var totalSize uint64 = 0
	var statfs syscall.Statfs_t

	err := syscall.Statfs("/", &statfs)
	if err != nil {
		fmt.Println("Error:", err)
		return 0, err
	}

	totalSize = statfs.Blocks * uint64(statfs.Bsize)

	return totalSize, nil
}
