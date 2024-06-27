"use client";
import React, {useEffect, useState} from 'react';
import {RecordInfoType} from "@/types/agent";
import * as echarts from "echarts";

type MetricInfoProps = {
    metricInfo: {
        metric: string
        metric_type: string
        data: RecordInfoType[]
    }[],
    selectedServices: string[]
}

const MetricInfo: React.FC<MetricInfoProps> = ({metricInfo, selectedServices}) => {
    // 选择渲染的metricInfo
    const [selectedMetricInfo, setSelectedMetricInfo] = useState([] as any);
    const drawServiceMetricChart = (chartId: string, metricInfo: {
        metric: string
        data: RecordInfoType[]
        metric_type: string
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
                text: metricInfo.metric
            },
            xAxis: {
                type: 'time',
                boundaryGap: false
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
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
                name: metricInfo.metric,
                type: 'line',
                data: metricInfo?.data.map((record) => {
                    return [record.time, record.value.toFixed(2)];
                }),
                smooth: true,
                symbol: 'none',
                areaStyle: {},
            }
            // series: {
            //     name: '11',
            //     type: 'line',
            //     data: metricInfo[0].data.map((record) => {
            //         return [record.time, record.value];
            //     }),
            //     smooth: true,
            //     symbol: 'none',
            //     areaStyle: {},
            // }
        };

        myChart.setOption(option);
    };

    useEffect(() => {
        console.log('metricInfo:', metricInfo);
        // drawServiceMetricChart(metricInfo[selectedServices]);
        const counter = metricInfo.find((item) => item.metric_type === 'counter' && selectedServices.includes(item.metric));
        const gauge = metricInfo.find((item) => item.metric_type === 'gauge' && selectedServices.includes(item.metric));
        const histogram = metricInfo.find((item) => item.metric_type === 'histogram' && selectedServices.includes(item.metric));
        console.log(counter, gauge, histogram)
        if (counter) {
            drawServiceMetricChart('ServiceCounterChart', counter);
        }
        if (gauge) {
            drawServiceMetricChart('ServiceGaugeChart', gauge);
        }
        if (histogram) {
            drawServiceMetricChart('ServiceHistogramChart', histogram);
        }
    }, [selectedServices]);

    // 排序
    const sortData = (data: RecordInfoType[]) => {
        return data.sort((a, b) => a.value - b.value);
    };

    // 计算p99和p95
    const metricsFunctions = {
        p99: (sortedData: RecordInfoType[]) => {
            const p99Index = Math.ceil(0.99 * sortedData.length) - 1;
            return sortedData[p99Index].value;
        },
        p95: (sortedData: RecordInfoType[]) => {
            const p95Index = Math.ceil(0.95 * sortedData.length) - 1;
            return sortedData[p95Index].value;
        },
    };

    return (
        <div className="w-full flex flex-col items-center">
            <div className="w-full h-full flex my-2">
                {
                    // 如果metricInfo中存在counter类型的数据，则展示counter图表
                    metricInfo.find((item) => item.metric_type === 'counter' && selectedServices.includes(item.metric)) && (
                        <div className="w-1/3">
                            <h2 className="text-2xl">Counter：</h2>
                            <div id="ServiceCounterChart" style={{width: '100%', aspectRatio: '1.5'}}/>
                        </div>
                    )
                }
                {
                    // 如果metricInfo中存在gauge类型的数据，则展示gauge图表
                    metricInfo.find((item) => item.metric_type === 'gauge' && selectedServices.includes(item.metric)) && (
                        <div className="w-1/3">
                            <h2 className="text-2xl">Gauge：</h2>
                            <div id="ServiceGaugeChart" style={{width: '100%', aspectRatio: '1.5'}}/>
                        </div>
                    )
                }
                {
                    // 如果metricInfo中存在histogram类型的数据，则展示histogram图表
                    metricInfo.find((item) => item.metric_type === 'histogram' && selectedServices.includes(item.metric)) && (
                        <div className="w-1/3">
                            <h2 className="text-2xl">Histogram：</h2>
                            <div id="ServiceHistogramChart" style={{width: '100%', aspectRatio: '1.5'}}/>
                        </div>
                    )
                }
            </div>
            {metricInfo.find(item => item.metric_type === 'histogram' && selectedServices.includes(item.metric)) && (
                <div className="w-[30%] text-2xl font-mono flex flex-col gap-2 mt-8">
                    <div className="w-full flex pb-2 border-b-2">
                        <div className="w-1/2">metric</div>
                        <div className="w-1/2 flex justify-around"><span>p99</span><span>p95</span></div>
                    </div>
                    {selectedServices && metricInfo.filter((item: any) => item.metric_type === 'histogram').map((data: any, index: number) => {
                        return (
                            selectedServices.includes(data.metric) && (
                                <div className="w-full flex pb-2 border-b-2" key={index}>
                                    <div className="w-1/2">{data.metric}</div>
                                    <div className="w-1/2 flex justify-around">
                                        <span>{metricsFunctions['p99'](sortData(data.data))}</span><span>{metricsFunctions['p95'](sortData(data.data))}</span>
                                    </div>
                                </div>
                            )
                        );
                    })}
                </div>
            )}
        </div>
    );
}

export default MetricInfo;
