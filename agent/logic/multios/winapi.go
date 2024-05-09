//go:build windows

package multios

func GetTotalDisk() (uint64, error) {
	var totalSize uint64
	drive := os.Getenv("SystemDrive")
	h := syscall.MustLoadDLL("kernel32.dll")
	c := h.MustFindProc("GetDiskFreeSpaceExW")

	freeBytes := int64(0)
	totalBytes := int64(0)
	_, _, _ = c.Call(
		uintptr(unsafe.Pointer(syscall.StringToUTF16Ptr(drive))),
		uintptr(unsafe.Pointer(&freeBytes)),
		uintptr(unsafe.Pointer(&totalBytes)),
		uintptr(unsafe.Pointer(nil)),
	)

	totalSize = totalBytes
	return totalSize, nil
}
