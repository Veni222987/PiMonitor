"use client";
import React from "react";
import {Button} from "antd";

export default function Login() {

    return (
        <main
            className="flex flex-col justify-between items-center h-full w-full p-[2px]">
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
                <div
                    className="fixed right-24 flex flex-col items-start gap-2 w-80 h-80 mt-10 rounded-xl drop-shadow-xl">
                    <h1 className="text-3xl text-white mb-4">登录畅享更多权益</h1>
                    <input className="w-full aspect-[8] rounded-lg text-[12px] p-2 bg-[#333645]/50"
                           placeholder={"请输入邮箱/手机号（国际号码加区号）"}/>
                    <input className="w-full aspect-[8] rounded-lg text-[12px] p-2 bg-[#333645]/50"
                           placeholder={"请输入验证码"}/>
                    <Button className="w-full my-3" type="primary">登录 / 注册</Button>
                    <span className="text-white text-[10px] w-full text-center">其他方式</span>
                    <div className="w-full h-0 border-[1px] border-white/10 border-dashed bg-white/30"></div>
                    <div className="w-full flex gap-2 justify-center">
                        <a className="hover:scale-110" href='https://github.com/ApiKnight' target='_blank'>
                            <img src="/gitub.svg"/>
                        </a>
                        <a className="hover:scale-110" href='https://github.com/ApiKnight' target='_blank'>
                            <img src="/gitee.svg"/>
                        </a>
                    </div>
                </div>
                <div className="fixed bottom-6 flex-1 flex items-center text-white/80">Copyright © Team200PI</div>
            </section>
        </main>
    );
}
