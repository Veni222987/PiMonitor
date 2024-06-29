"use client";
import React, {useEffect, useState} from "react";
import {GetAgentInfo, GetAgentList, GetHostUtilization, GetMetricInfo} from "@/api/agent";
import {GetTeamLists} from "@/api/user";
import {TeamListType} from "@/types/team";
import {Select} from "antd";
import {AgentInfoType, RecordInfoType} from "@/types/agent";
import AgentInfo from "@/app/(main)/dashboard/components/AgentInfo";
import MetricInfo from "@/app/(main)/dashboard/components/MetricInfo";
import HostUtilization from "@/app/(main)/dashboard/components/HostUtilization";

export default function Dashboard() {
    // 团队列表
    const [teamList, setTeamList] = useState([] as TeamListType[]);
    // 主机信息
    const [agentInfo, setAgentInfo] = useState<AgentInfoType>({} as AgentInfoType);
    // 主机列表
    const [hostList, setHostList] = useState([] as AgentInfoType[]);
    // 主机利用率
    const [hostUtilization, setHostUtilization] = useState([] as any);
    // metric服务信息
    const [metricInfo, setMetricInfo] = useState([] as any);
    // 主机options
    const [hostOptions, setHostOptions] = useState([] as { value: string, label: string }[]);
    // agentOptions
    const [agentOptions, setAgentOptions] = useState([] as { value: string, label: string }[]);
    // 选中的AgentID
    const [selectedAgentID, setSelectedAgentID] = useState('');
    // 选中的teamID
    const [selectedTeamID, setSelectedTeamID] = useState('');
    // 用户选择的 metrics
    const [selectedMetrics] = useState(["p99", "p95"]);
    // 当前agent包含的服务数组
    const [services, setServices] = useState([] as string[]);
    // servicesOptions
    const [servicesOptions, setServicesOptions] = useState([] as { value: string, label: string }[]);
    // 选中的服务
    const [selectedServices, setSelectedServices] = useState<string[]>([]);
    // 选择的监控时间段
    const [selectedTimeRange, setSelectedTimeRange] = useState<string>("1");

    // 获取团队列表
    useEffect(() => {
        const fetchTeamInfo = async () => {
            try {
                const res = await GetTeamLists();
                setTeamList(res.records);
                console.log(res);
                // 构造符合Select组件的数据结构
                setHostOptions(res.records.map((team) => ({
                    value: team.id + '',
                    label: team.name,
                })));
            } catch (error) {
                console.error('Error fetching team info: ', error);
            }
        };

        fetchTeamInfo().then(r => {
        });
    }, []);

    // 获取主机列表
    useEffect(() => {
        const fetchHostInfo = async () => {
            try {
                const res = await GetAgentList({teamID: selectedTeamID});
                console.log(res);
                // setHostList(res.records)
                // 构造符合Select组件的数据结构
                setAgentOptions(res.records.map((host) => ({
                    value: host.id + '',
                    label: host.hostname,
                })));
            } catch (error) {
                console.error('Error fetching host info: ', error);
            }
        };

        fetchHostInfo().then(r => {
        });
    }, [selectedTeamID]);

    // 获取主机信息
    useEffect(() => {
        const fetchHostInfo = async () => {
            try {
                const res = await GetAgentInfo({agentID: selectedAgentID});
                console.log(res);
                setAgentInfo(res);
            } catch (error) {
                console.error('Error fetching host info: ', error);
            }
        };

        fetchHostInfo().then(r => {
        });
    }, [selectedAgentID]);

    // 获取主机利用率
    useEffect(() => {
        const fetchHostUtilization = async () => {
            try {
                let startTime = Math.floor(new Date().getTime() / 1000);
                if (selectedTimeRange === '1') {
                    startTime = startTime - 3600;
                } else if (selectedTimeRange === '2') {
                    startTime = startTime - 43200;
                } else if (selectedTimeRange === '3') {
                    startTime = startTime - 86400;
                } else if (selectedTimeRange === '4') {
                    startTime = startTime - 604800;
                } else {
                    startTime = startTime - 2592000;
                }
                const res = await GetHostUtilization({
                    agentID: selectedAgentID,
                    startTime: startTime,
                    endTime: Math.floor(new Date().getTime() / 1000)
                });
                console.log(res);
                setHostUtilization(res);
            } catch (error) {
                console.error('Error fetching host utilization: ', error);
            }
        };

        fetchHostUtilization().then(r => {
        });
    }, [selectedAgentID,selectedTimeRange]);

    // 获取metric服务信息
    useEffect(() => {
        const fetchMetricInfo = async () => {
            try {
                let startTime = Math.floor(new Date().getTime() / 1000);
                if (selectedTimeRange === '1') {
                    startTime = startTime - 3600;
                } else if (selectedTimeRange === '2') {
                    startTime = startTime - 43200;
                } else if (selectedTimeRange === '3') {
                    startTime = startTime - 86400;
                } else if (selectedTimeRange === '4') {
                    startTime = startTime - 604800;
                } else {
                    startTime = startTime - 2592000;
                }
                const res = await GetMetricInfo({agentID: selectedAgentID, startTime, endTime: Math.floor(new Date().getTime() / 1000)});
                console.log(res);
                setMetricInfo(res);
                // 遍历metric服务信息，从[{metric: '', data[]}]中获取服务数组
                const services = res.map((item) => item.metric);
                setServices(services);
                // 构造符合Select组件的数据结构
                setServicesOptions(services.map((service) => ({
                    value: service,
                    label: service,
                })));
            } catch (error) {
                console.error('Error fetching metric info: ', error);
            }
        };

        fetchMetricInfo().then(r => {
        });
    }, [selectedAgentID]);

    // 选中团队
    const onTeamChange = (value: string) => {
        console.log(`selected ${value}`);
        setSelectedTeamID(value);
    };

    // 选中agent
    const onAgentChange = (value: string) => {
        console.log(`selected ${value}`);
        setSelectedAgentID(value);
    };

    // 选中services
    const onServicesChange = (value: string[]) => {
        console.log(`selected ${value}`);
        setSelectedServices(value);
    };

    // 搜索
    const onSearch = (value: string) => {
        console.log('search:', value);
    };

    const timeRangeOptions = [
        {
            value: '1',
            label: '最近1小时'
        },
        {
            value: '2',
            label: '最近12小时'
        },
        {
            value: '3',
            label: '最近24小时'
        },
        {
            value: '4',
            label: '最近7天'
        },
        {
            value: '5',
            label: '最近30天'
        }
    ];

    // 选中监控时间段
    const onTimeRangeChange = (value: string) => {
        console.log(`selected ${value}`);
        setSelectedTimeRange(value);
    };

    return (
        <main
            className="flex flex-col justify-between h-full w-full p-[2px]">
            <div className="flex items-center text-2xl mr-auto mb-5">
                <div className="w-2 h-6 bg-blue-600 inline-block mr-2"></div>
                <h2 className="">选择agent</h2>
            </div>
            <div className="flex gap-4 mb-2">
                <div className="flex items-center">
                    <h2>当前团队：</h2>
                    <Select
                        className="w-32"
                        showSearch
                        placeholder="选择团队"
                        optionFilterProp="label"
                        onChange={onTeamChange}
                        onSearch={onSearch}
                        options={hostOptions}
                    />
                </div>
                <div className="flex items-center">
                    <h2>agent：</h2>
                    <Select
                        className="w-32"
                        showSearch
                        placeholder="选择agent"
                        optionFilterProp="label"
                        onChange={onAgentChange}
                        onSearch={onSearch}
                        options={agentOptions}
                    />
                </div>
            </div>
            <div className="flex items-center text-2xl mr-auto my-4">
                <div className="w-2 h-6 bg-blue-600 inline-block mr-2"></div>
                <h2 className="">主机信息</h2>
            </div>
            {JSON.stringify(agentInfo) !== '{}' && (
                <AgentInfo agentInfo={agentInfo}/>
            )}
            <div className="flex items-center text-2xl mr-auto my-4">
                <div className="w-2 h-6 bg-blue-600 inline-block mr-2"></div>
                <h2 className="">主机利用率</h2>
            </div>
            {selectedAgentID !== '' && (
                <Select
                    className="w-48 ml-auto mb-8"
                    showSearch
                    placeholder="选择监控时间段"
                    optionFilterProp="label"
                    onChange={onTimeRangeChange}
                    onSearch={onSearch}
                    options={timeRangeOptions}
                />
            )}
            {hostUtilization.length > 0 && (
                <>
                    <HostUtilization hostUtilization={hostUtilization}/>
                </>
            )}
            <div className="flex items-center text-2xl mr-auto my-4">
                <div className="w-2 h-6 bg-blue-600 inline-block mr-2"></div>
                <h2 className="">服务信息</h2>
            </div>
            {selectedAgentID !== '' && (
                <Select
                    className="w-80 ml-auto mb-8"
                    mode="multiple"
                    showSearch
                    placeholder="选择服务"
                    optionFilterProp="label"
                    onChange={onServicesChange}
                    onSearch={onSearch}
                    options={servicesOptions}
                />
            )}
            {selectedServices.length !== 0 && (
                <MetricInfo metricInfo={metricInfo} selectedServices={selectedServices}/>
            )}
        </main>
    );
}
