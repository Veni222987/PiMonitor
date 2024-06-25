import {authGet, authPut} from "@/utils/AuthUtils";
import {CpuInfo, RecordInfo} from "@/types/agent";

/**
 * 获取主机基本信息
 * @param {string} agentID
 */
export const GetAgentInfo = authGet<{
    agentID: string
}, {
    id: number,
    mac: string
    cpu: CpuInfo,
    memory: number,
    disk: number,
    networkCard: string[],
    os: string,
    lastTime: string,
    status: string,
    hostname: string
}>('/v1/agents/info')

/**
 * 查看主机利用率
 * @param {string} agentID
 * @param {number} startTime
 * @param {number} endTime
 */
export const GetHostUtilization = authGet<{
    agentID: string,
    startTime: number,
    endTime: number
}, {
    disk_percent: RecordInfo[],
    network_rate: RecordInfo[],
    tcp_connection: RecordInfo[],
    mem_percent: RecordInfo[],
    cpu_percent: RecordInfo[]
}>("/v1/agents/usage")

/**
 * 获取主机列表
 * @param {string} teamID
 * @param {string} page
 * @param {string} size
 */
export const GetAgentList = authGet<{
    teamID: string,
    page?: string,
    size?: string
}, {
    records: {
        id: number,
        mac: string,
        cpu: CpuInfo,
        memory: number,
        disk: number,
        networkCard: string[],
        os: string,
        lastTime: string,
        status: string,
        hostname: string
    }[],
    total: number
}>("/v1/agents/list")

/**
 * 查看metric服务信息
 * @param {string} agentID
 * @param {number} startTime
 * @param {number} endTime
 */
export const GetMetricInfo = authGet<{
    agentID: string,
    startTime: number,
    endTime: number
}, {
    metric: string,
    data: RecordInfo[]
}[]
>("/v1/agents/services/info")
