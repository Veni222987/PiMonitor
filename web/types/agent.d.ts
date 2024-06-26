interface CpuInfoType {
    cpuName: string
    core: string
    frequency: string
}

interface RecordInfoType {
    time: number
    value: number
}

interface AgentInfoType {
    id: number
    mac: string
    cpu: CpuInfoType
    memory: number
    disk: number
    networkCard: string[]
    os: string
    lastTime: string
    status: string
    hostname: string
}

export type {CpuInfoType, RecordInfoType, AgentInfoType}
