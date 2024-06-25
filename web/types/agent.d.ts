interface CpuInfo {
    cpuName: string
    core: string
    frequency: string
}

interface RecordInfo {
    time: number
    value: number
}

interface AgentInfo {
    id: number
    mac: string
    cpu: CpuInfo
    memory: number
    disk: number
    networkCard: string[]
    os: string
    lastTime: string
    status: string
    hostname: string
}

export type {CpuInfo, RecordInfo, AgentInfo}
