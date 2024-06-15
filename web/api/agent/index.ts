import {authGet} from "@/utils/AuthUtils";
import {CpuInfo, RecordInfo} from "@/types/agent";

/**
 * 获取主机基本信息
 * @param {string} agentID
 */
export const GetAgentInfo = authGet< {
    agentID: string
},{
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
}>('v1/agents/info')

/**
 * 查看主机利用率
 * @param {string} agentID
 * @param {number} startTime
 * @param {number} endTime
 */
export const GetHostUtilization = authGet<{
    agentID: string,
    startTime: string,
    endTime: string
},{
    disk_percent: RecordInfo[],
    network_rate: RecordInfo[],
    tcp_connection: RecordInfo[],
    mem_percent: RecordInfo[],
    cpu_percent: RecordInfo[]
}>("v1/agents/usage")


