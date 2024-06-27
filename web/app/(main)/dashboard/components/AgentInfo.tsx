"use client";
import React, {useEffect} from 'react';
import {AgentInfoType} from "@/types/agent";
import { Descriptions } from 'antd';
import type { DescriptionsProps } from 'antd';

type AgentInfoProps = {
    agentInfo: AgentInfoType
}

const AgentInfo: React.FC<AgentInfoProps> = ({agentInfo}) => {
    // 将字节转换为GB
    function bytesToGB(bytes: number) {
        if (bytes === 0) return "0 GB";
        const gb = bytes / (1024 * 1024 * 1024);
        return gb.toFixed(2) + " GB";
    }
    // 用于存储Descriptions组件的数据
    const [items, setItems] = React.useState<DescriptionsProps['items']>([]);
    // 构造符合Descriptions组件的数据结构
    useEffect(() => {
        console.log('agentInfo:', agentInfo);
        if (agentInfo) {
            const data = [
                {
                    key: '1',
                    label: 'agentID',
                    children: agentInfo.id,
                },
                {
                    key: '2',
                    label: '主机名',
                    children: agentInfo.hostname,
                },
                {
                    key: '3',
                    label: 'cpu型号',
                    children: `${agentInfo.cpu?.cpuName} ${agentInfo.cpu?.core}核 ${agentInfo.cpu?.frequency}`,
                },
                {
                    key: '4',
                    label: '内存空间',
                    children: bytesToGB(agentInfo.memory),
                },
                {
                    key: '5',
                    label: '磁盘空间',
                    children: bytesToGB(agentInfo.disk),
                },
                {
                    key: '6',
                    label: 'mac地址',
                    children: agentInfo.mac,
                },
                {
                    key: '7',
                    label: '操作系统',
                    children: agentInfo.os,
                },
                {
                    key: '8',
                    label: '上次在线时间',
                    children: agentInfo.lastTime,
                },
                {
                    key: '9',
                    label: '状态',
                    children: agentInfo.status,
                },
                {
                    key: '10',
                    label: '网卡',
                    children: agentInfo.networkCard?.map((card) => <span key={card}>{card} </span>),
                }
            ];
            setItems(data)
        }
    }, [agentInfo]);
    return (
        <div className="w-full h-full flex items-center justify-start my-2">
            <Descriptions bordered items={items}/>
        </div>
    );
}

export default AgentInfo
