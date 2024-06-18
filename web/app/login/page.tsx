"use client";
import React from "react";
import {Button} from "antd";
import HeaderNav from "@/components/HeaderNav";
import {CreateUser} from "@/types/response.type";
import LoginBox from "@/app/login/components/LoginBox";

export default function Login() {
    let user_info: CreateUser = {
        id: '1',
        email: '1826942933@qq.com',
        phone: '1826942933',
        avatar_url: 'https://avatars.githubusercontent.com/u/19998011?s=70&v=4',
        username: 'PiMonitor',
    };
    return (
        <main
            className="flex flex-col justify-between items-center h-full w-full p-[2px]">
            <HeaderNav user_info={user_info}></HeaderNav>
            <section className="flex flex-col justify-center items-center w-full h-full">
                <div
                    className="fixed w-full h-full bg-bg-radial overflow-hidden">
                    <video className="absolute w-full h-full object-cover" src="/3.mp4" autoPlay={true} muted={true}
                           loop={true}/>
                </div>
                <div className="absolute top-0 right-0 w-full h-full bg-fade-left-to-right">
                </div>
                <div
                    className="fixed left-40 flex flex-col items-start tracking-widest gap-2 h-96 p-4 text-4xl text-white rounded-xl drop-shadow-xl">
                    <h1 className="text-3xl mb-2 tracking-widest">实时监控</h1>
                    <h1>24小时全天候</h1>
                    <h1 className="text-[#1a89ee] text-5xl my-1">守护<span className="text-white text-4xl">, 你的主机儿们</span></h1>
                    <h1>值得信赖的助手</h1>
                    <p className="text-[10px] leading-4 w-full opacity-85 mt-4 tracking-wide">Agent部署后可以自动扫描、发现和识别此主机上安装的所有服务和组件。</p>
                    <div className="flex gap-5 w-full mt-4">
                        <button className="text-[12px] leading-4 w-32 aspect-[8] p-2 border-[1px] rounded-2xl">关于我们</button>
                        <button className="text-[12px] leading-4 w-32 aspect-[8] bg-blue-600 p-2 rounded-2xl">开始探索</button>
                    </div>
                </div>
                <LoginBox/>
                <div className="fixed bottom-6 flex-1 flex items-center text-white/80">Copyright © Team200PI</div>
            </section>
        </main>
    );
}
