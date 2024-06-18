"use client";
// import Image from "next/image";
import React, { useState, useEffect } from 'react'
// import HeaderNav from '@/components/HeaderNav'
// import { Button, Avatar, Space, Tag, CollapseProps, Collapse } from 'antd'
// import IndexShow from '@/components/IndexShow'
// import { IndexData } from '@/types/indexShow'
// import {
//   RadarChartOutlined,
//   NodeIndexOutlined,
//   PhoneOutlined,
// } from '@ant-design/icons'
// import {CreateUser} from "@/types/response.type";
import {getToken} from "@/utils/AuthUtils";
// import Dashboard from "@/app/(main)/dashboard/page";
// import Login from "@/app/login/page";

export default function Home() {
  // 判断用户是否已经登录
  // const [isLogin, setIsLogin] = useState(false);
  // 检测用户是否已经登录，如果已经登录就跳入主页，否则渲染登录页面
  useEffect(() => {
    if (getToken()) {
      window.location.href = '/dashboard';
    } else {
      window.location.href = '/login';
    }
  }, []);
  return (
      <>
        {/*{isLogin ? <Dashboard /> : <Login />}*/}
      </>
  );
}
