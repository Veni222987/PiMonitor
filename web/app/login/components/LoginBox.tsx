"use client";

import {Button} from "antd";
import React, {useEffect, useRef, useState} from "react";
import CaptchaModal from "@/app/login/components/CaptchaModal";
import {useDebounce} from "@/hooks/useDebounce";
import {SendCode, VerifyLoginCode} from "@/api/general";
import {GetUserInfo, Login} from "@/api/user";
import {setupToken} from "@/utils/AuthUtils";
import {ThirdPartyCallback, ThirdPartyLogin} from "@/api/thirdParty";
import {useRouter} from "next/navigation";
import {UserInfo} from "@/types/user";
import {getLocalStorage, removeLocalStorage} from "@/utils/StorageUtils";

enum codeType {

}

export default function LoginBox() {
    // 记录是否已经发送验证码
    const [isSendCode, setIsSendCode] = useState<boolean>(false);
    // 记录倒计时时长
    const [time, setTime] = useState<number>(120);
    // 记录定时器
    const timer = useRef(null);
    // 记录邮箱/手机号
    const [account, setAccount] = useState<string>("");
    // 记录验证码
    const [code, setCode] = useState<string>("");
    // 记录密码
    const [password, setPassword] = useState<string>("");
    // 判断是否是密码登录
    const [isPasswordLogin, setIsPasswordLogin] = useState<boolean>(false);
    // 判断是否显示登录框
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    // 判断验证是否成功
    const [isVerify, setIsVerify] = useState<boolean>(false);
    // 判断手机号码是否有效
    const [isPhoneValid, setIsPhoneValid] = useState<boolean>(true);
    // 判断验证码是否正确
    const [isCodeRight, setIsCodeRight] = useState<boolean>(false);

    const router = useRouter();

    // 显示登录框
    const showModal = () => {
        setIsModalOpen(true);
    };

    // 点击取消
    const handleCancel = () => {
        setIsModalOpen(false);
    };

    // 初始化倒计时时长
    const initTime = 120

    // 使用手写防抖函数包裹 onChange 事件
    const debouncedOnPhoneChange = useDebounce((e: any) => {
        const phoneRegex = /^1((34[0-8])|(8\d{2})|(([35][0-35-9]|4[579]|66|7[35678]|9[1389])\d{1}))\d{7}$/; // 手机号码正则
        // 判断手机号是否正确
        if (!phoneRegex.test(e.target.value) && e.target.value.trim() !== "") {
            setIsPhoneValid(false)
        } else {
            setIsPhoneValid(true)
        }
    }, 500);

    // 监听邮箱/手机号码输入框
    const handleAccountChange = (e: any) => {
        setAccount(e.target.value)
        // 调用防抖函数
        debouncedOnPhoneChange(e);
    };

    // 监听验证码输入框
    const handleCodeChange = (e: any) => {
        if (e.target.value.length) {
            setIsCodeRight(true)
        }
        setCode(e.target.value)
    };

    // 监听密码输入框
    const handlePasswordChange = (e: any) => {
        setPassword(e.target.value)
    };

    // 获取验证码
    const handleGetCode = async (account: string) => {
        try {
            await SendCode({account})
        } catch (e) {
            console.error("LoginBox failed:", e)
        } finally {
            setIsSendCode(true)
        }
    }

    // 将用户信息存储到localstorage
    const saveUserInfo = (user: UserInfo) => {
        localStorage.setItem("userInfo", JSON.stringify(user))
    }

    // 验证码登录
    const handleCodeLogin = async () => {
        try {
            const {jwt} = await VerifyLoginCode({account, code})
            setupToken(jwt)
            // 获取用户信息
            const {user} = await GetUserInfo()
            // 将用户信息存储到localstorage
            saveUserInfo(user)
            // 路由跳转
            router.push("/")
        } catch (e) {
            console.error("LoginBox failed:", e)
        } finally {
            // 清除输入框
            setAccount("")
            setCode("")
        }
    }

    // 密码登录
    const handlePasswordLogin = async () => {
        try {
            const {jwt, user} = await Login({account, password})
            setupToken(jwt)
            // 将用户信息存储到localstorage
            saveUserInfo(user)
            // 路由跳转
            router.push("/")
        } catch (e) {
            console.error("LoginBox failed:", e)
        }
    }

    let authWindow: any = null;

    // 第三方登录
    const handleThirdPartyLogin = async (type: string) => {
        try {
            const {redirectURL} = await ThirdPartyLogin({type})
            console.log("redirectURL:", redirectURL)
            try {
                authWindow = window.open(
                    redirectURL,
                    "_blank",
                    "toolbar=no,width=800, height=600"
                );
                window.addEventListener("storage",() => handleThirdPartyCallback(type))
            } catch (e) {
                console.error("LoginBox failed:", e)
            }
        } catch (e) {
            console.error("LoginBox failed:", e)
        }
    }

    // 处理第三方登录回调
    const handleThirdPartyCallback = async (type: string) => {
        const code = getLocalStorage("authCode")
        removeLocalStorage("authCode")
        const state = getLocalStorage("authState")
        removeLocalStorage("authState")
        window.removeEventListener("storage", () => {
            console.log("removeEventListener")
        })
        if (code && state) {
            const res = await ThirdPartyCallback({type, code, state})
            console.log("res:", res)
            if (res) {
                // setupToken(res.jwt)
                // // 获取用户信息
                // const {user} = await GetUserInfo()
                // // 将用户信息存储到localstorage
                // saveUserInfo(user)
                // 路由跳转
                router.push("/")
            }
            return;
        }
        throw new Error("授权码获取失败")
    }

    const handleCloseWindow = () => {
        if (authWindow) {
            authWindow.close();
        }
    };

    useEffect(() => {
        if (isVerify) {
            handleGetCode(account).then(()=>{
                setIsVerify(false)
            })
        }
    }, [isVerify]);

    // 倒计时刷新获取验证码
    useEffect(() => {
        if (time <= 0) {
            timer.current && clearInterval(timer.current)
            setIsSendCode(false)
            setTime(initTime)
        } else if (time === initTime && isSendCode) {
            // @ts-ignore
            timer.current = setInterval(() => {
                setTime(time => --time)
            }, 1000)
        }
    }, [isSendCode, time])

    // github点击授权登录
    const handleGithubLogin = async () => {
        try {
            const {redirectURL} = await ThirdPartyLogin({type: "github"})
        } catch (e) {
            console.error("LoginBox failed:", e)
        }
    }

    return (
        <div
            className="fixed right-[5%] flex flex-col items-start gap-3 w-80 h-80 mt-10 rounded-xl drop-shadow-xl">
            <h1 className="text-3xl text-white mb-4">登录畅享更多权益</h1>
            {/*<button onClick={handleCloseWindow}>close window</button>*/}
            {
                isPasswordLogin ?
                    <div className="flex relative w-full aspect-[8]">
                        <input className="w-full rounded-lg text-white text-[12px] p-2 bg-[#333645]/50"
                               placeholder={"请输入邮箱/手机号（国际号码加区号）"}/>
                    </div>
                    :
                    <div className="flex relative w-full aspect-[8]">
                        <input
                            onChange={handleAccountChange}
                            className="w-full rounded-lg text-[12px] p-2 bg-[#333645]/50 text-white"
                            placeholder={"请输入邮箱/手机号（国际号码加区号）"}/>
                        <button
                            className={`absolute right-0 h-full text-white text-[12px] px-3 ${isSendCode ? "disabled" : ""}`}
                            onClick={() => showModal()}
                            disabled={isSendCode}
                        >{!isSendCode ? "获取验证码" : `${time}S`}</button>
                    </div>
            }
            {
                isPasswordLogin ?
                    <div className="flex relative w-full aspect-[8]">
                        <input
                            onChange={handlePasswordChange}
                            className="w-full rounded-lg text-white text-[12px] p-2 bg-[#333645]/50"
                            placeholder={"请输入密码"}/>
                    </div>
                    :
                    <div className="flex relative w-full aspect-[8]">
                        <input
                            onChange={handleCodeChange}
                            maxLength={6}
                            className="w-full rounded-lg text-white text-[12px] p-2 bg-[#333645]/50"
                            placeholder={"请输入验证码"}/>
                    </div>
            }
            <Button
                className="w-full my-3"
                type="primary"
                onClick={
                    isPasswordLogin ? handlePasswordLogin : handleCodeLogin
                }>登录 / 注册</Button>
            <div className="w-full flex items-center">
                <span className="text-gray-300 text-[12px] text-center">其他登录：</span>
                <div className="flex gap-2 justify-center">
                    <button className="hover:scale-110" onClick={()=>handleThirdPartyLogin("github")}>
                        <img src="/gitub.svg"/>
                    </button>
                    <button className="hover:scale-110" onClick={()=>handleThirdPartyLogin("gitee")}>
                        <img src="/gitee.svg"/>
                    </button>
                </div>
                <button
                    onClick={() => {
                        setIsPasswordLogin(!isPasswordLogin)
                        setAccount("")
                        setCode("")
                        setPassword("")
                    }}
                    className="text-white/80 text-[12px] ml-auto hover:text-white">
                    {isPasswordLogin ? "验证码登录" : "密码登录"}
                </button>
            </div>
            <CaptchaModal isOpen={isModalOpen} onClose={handleCancel} setVerify={setIsVerify}/>
        </div>
    )
}
