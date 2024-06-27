"use client";
import React, {useEffect} from 'react';
import {RecordInfoType} from "@/types/agent";
import * as echarts from "echarts";

type HostUtilizationProps = {
    hostUtilization: {
        metric: string,
        data: RecordInfoType[]
    }[]
}

const HostUtilization: React.FC<HostUtilizationProps> = ({hostUtilization}) => {

    useEffect(() => {
        const drawChart = (chartId: string, utilization: {
            metric: string,
            data: RecordInfoType[]
        }) => {
            const chartDom = document.getElementById(chartId);
            const myChart = echarts.init(chartDom);
            myChart.clear();
            const option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt: any[]) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: utilization.metric
                },
                xAxis: {
                    type: 'time',
                    boundaryGap: false
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: utilization.metric === 'tcp连接数（个）' ? '{value}' : '{value}%',
                    },
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 100
                    }
                ],
                series: {
                    name: utilization.metric,
                    type: 'line',
                    data: utilization.data?.map((record) => {
                        return [record.time, record.value.toFixed(2)];
                    }),
                    smooth: true,
                    symbol: 'none',
                    areaStyle: {},
                }
            };

            myChart.setOption(option);
        }

        const cpuUtilization = hostUtilization.find((item) => item.metric === 'cpu_percent');
        const memUtilization = hostUtilization.find((item) => item.metric === 'mem_percent');
        const diskUtilization = hostUtilization.find((item) => item.metric === 'disk_percent');
        const networkRate = hostUtilization.find((item) => item.metric === 'network_rate');
        const tcpConnection = hostUtilization.find((item) => item.metric === 'tcp_connection');

        if (cpuUtilization) {
            drawChart('CpuUtilizationChart', cpuUtilization);
        }
        if (memUtilization) {
            drawChart('MemoryUtilizationChart', memUtilization);
        }
        if (diskUtilization) {
            drawChart('DiskUtilizationChart', diskUtilization);
        }
        if (networkRate) {
            drawChart('NetworkRateChart', networkRate);
        }
        if (tcpConnection) {
            drawChart('TcpConnectionChart', tcpConnection);
        }
    }, [hostUtilization]);

    return (
        <div className="flex flex-wrap justify-around">
            <div id="CpuUtilizationChart" style={{width: '48%', aspectRatio: '1.5', marginBottom: '20px'}}/>
            <div id="MemoryUtilizationChart" style={{width: '48%', aspectRatio: '1.5', marginBottom: '20px'}}/>
            <div id="DiskUtilizationChart" style={{width: '48%', aspectRatio: '1.5', marginBottom: '20px'}}/>
            <div id="NetworkRateChart" style={{width: '48%', aspectRatio: '1.5', marginBottom: '20px'}}/>
            <div id="TcpConnectionChart" style={{width: '48%', aspectRatio: '1.5', marginBottom: '20px'}}/>
        </div>
    );
}

export default HostUtilization
