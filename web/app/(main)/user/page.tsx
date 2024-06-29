"use client";
import React, {useEffect, useState} from 'react';
import {GetUserInfo, ModifyUserInfo} from "@/api/user";
import {AuthInfo, UserInfo} from "@/types/user";
import {Avatar, Button, Input} from "antd";
import AvatarUploader from "@/app/(main)/user/components/AvatarUploader";
import {removeToken} from "@/utils/AuthUtils";

export default function User() {
    // 第三方授权信息
    const [thirdPartyAuth, setThirdPartyAuth] = useState([] as AuthInfo[]);
    // 用户信息
    const [userInfo, setUserInfo] = useState({} as UserInfo);
    // 修改后的用户信息
    const [newUserInfo, setNewUserInfo] = useState({} as UserInfo);
    // 是否开启账号信息编辑模式
    const [isUserInfoEdit, setUserInfoEdit] = useState(false);
    // 是否开启账号设置编辑模式
    const [isAccountEdit, setAccountEdit] = useState(false);
    // 是否开启第三方账号编辑模式
    const [isThirdPartyEdit, setThirdPartyEdit] = useState(false);

    // 获取用户信息
    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const {auths, user} = await GetUserInfo();
                setThirdPartyAuth(auths);
                setUserInfo(user);
                setNewUserInfo(user);
            } catch (error) {
                console.error('Error fetching user info: ', error);
            }
        };

        fetchUserInfo().then(r => {

        });
    }, [])

    // handleUsernameChange
    const handleUsernameChange = (e: any) => {
        setNewUserInfo({...newUserInfo, username: e.target.value});
    }

    // 保存用户信息
    const saveUserInfo = async () => {
        try {
            await ModifyUserInfo(newUserInfo);
            setUserInfo(newUserInfo)
            setUserInfoEdit(false)
        } catch (error) {
            console.error('Error saving user info: ', error);
        }
    }

    // 退出登录
    const logout = async () => {
        try {
            removeToken()
            localStorage.removeItem('userInfo');
            localStorage.removeItem('auths');
            window.location.href = '/login';
        } catch (error) {
            console.error('Error logging out: ', error);
        }
    }

    return (
        <div className="flex flex-col gap-4 items-start">
            <div className="w-full">
                <h1 className="text-2xl mr-auto mb-4">用户信息</h1>
                <div className="w-3/5">
                    <button
                        className="absolute right-12 rounded-xl bg-blue-400 py-1 px-2 text-white text-[12px]"
                        onClick={() => setUserInfoEdit(!isUserInfoEdit)}
                    >
                        编辑
                    </button>
                    <div className={`flex w-1/2 flex-col items-start gap-4 ${isUserInfoEdit ? 'hidden' : 'block'}`}>
                        <div className="w-full flex items-center gap-6">
                            <span className="w-1/6 text-right text-[#555]">头像</span>
                            <Avatar
                                size={64}
                                src={userInfo.avatar !== ('' || null) ? "https://img0.baidu.com/it/u=1403557207,620046077&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1718730000&t=0d376f0a88576629157efa9d0bed788e" : userInfo.avatar}
                            />
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">用户id</span>
                            <span className="">
                                {userInfo.id}
                            </span>
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">用户名</span>
                            <span className="">
                                {userInfo.username}
                            </span>
                        </div>
                    </div>
                    <div className={`flex w-3/5 flex-col items-start gap-4 ${!isUserInfoEdit ? 'hidden' : 'block'}`}>
                        <div className="w-full flex items-center gap-6">
                            <span className="w-1/6 text-right text-[#555]">头像</span>
                            <AvatarUploader className="w-32"/>
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">用户id</span>
                            <Input className="w-4/6" placeholder="请输入用户id" disabled={true} value={userInfo.id}/>
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">用户名</span>
                            <Input
                                className="w-4/6"
                                placeholder="请输入用户名"
                                value={newUserInfo.username}
                                onChange={handleUsernameChange}
                            />
                        </div>
                    </div>
                </div>
                <Button
                    type="primary"
                    className={`mt-4 mx-auto ${isUserInfoEdit ? 'block' : 'hidden'}`}
                    onClick={saveUserInfo}
                >
                    保存
                </Button>
            </div>
            <div className="w-full">
                <h1 className="text-2xl mr-auto mt-4 mb-4">账号设置</h1>
                <div className="w-3/5">
                    <button
                        className="absolute right-12 rounded-xl bg-blue-400 py-1 px-2 text-white text-[12px]"
                        onClick={() => setAccountEdit(!isAccountEdit)}
                    >
                        编辑
                    </button>
                    <div className={`flex w-1/2 flex-col items-start gap-4 ${isAccountEdit ? 'hidden' : 'block'}`}>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">邮箱</span>
                            <span className="">
                                {userInfo.email === ('' || null) ? '未设置' : userInfo.email}
                            </span>
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">手机号</span>
                            <span className="">
                                 {userInfo.phoneNumber === ('' || null) ? '未设置' : userInfo.phoneNumber}
                            </span>
                        </div>
                        <div className="w-full flex items-center gap-6">
                            <span className="w-1/6 text-right text-[#555]">密码</span>
                            <span className="">
                                ******
                            </span>
                        </div>
                    </div>
                    <div className={`flex w-3/5 flex-col items-start gap-4 ${!isAccountEdit ? 'hidden' : 'block'}`}>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">邮箱</span>
                            {userInfo.email === ('' || null) ?
                                <Button type="primary">绑定邮箱</Button>
                                : <Input className="w-4/6" placeholder="请输入邮箱" disabled={true} value={userInfo.email}/>}
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">手机号</span>
                            {userInfo.phoneNumber === ('' || null) ?
                                <Button type="primary">绑定手机号</Button>
                                : <Input className="w-4/6" placeholder="请输入手机号" disabled={true} value={userInfo.phoneNumber}/>}
                        </div>
                        <div className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">密码</span>
                            <Button type="primary">重置密码</Button>
                        </div>
                    </div>
                </div>
                <Button type="primary" className={`mt-4 mx-auto ${isAccountEdit ? 'block' : 'hidden'}`}>保存</Button>
            </div>
            <h1 className="text-2xl mr-auto mt-4 mb-4">第三方账号</h1>
            <div className="w-3/5 mb-8">
                <button
                    className="absolute right-12 rounded-xl bg-blue-400 py-1 px-2 text-white text-[12px]"
                    onClick={() => setThirdPartyEdit(!isThirdPartyEdit)}
                >
                    绑定第三方账号
                </button>
                {thirdPartyAuth.map((auth: any) => {
                    return (
                        <div key={auth.id} className="w-full flex gap-6">
                            <span className="w-1/6 text-right text-[#555]">{auth.type}</span>
                            <span className="">
                                已绑定
                            </span>
                        </div>
                    )
                })}
            </div>
            <div className="w-full flex justify-center mt-8">
                <Button
                    size={"large"}
                    onClick={() => logout()}
                >
                    退出登录
                </Button>
            </div>
        </div>
    );
};
