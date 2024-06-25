"use client";
import React, {useEffect, useState} from "react";
import {
    DesktopOutlined,
    FileOutlined,
    PieChartOutlined,
    TeamOutlined,
    UserOutlined,
} from '@ant-design/icons';
import type {MenuProps} from 'antd';
import {Layout, Menu} from 'antd';
import {usePathname} from "next/navigation";
import Link from "next/link";

const {Header, Content, Footer, Sider} = Layout;

type MenuItem = Required<MenuProps>['items'][number];

function getItem(
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
): MenuItem {
    return {
        key,
        icon,
        children,
        label,
    } as MenuItem;
}

const items: MenuItem[] = [
    getItem('dashboard', '1', <PieChartOutlined/>),
    getItem('User', 'sub1', <UserOutlined/>),
    getItem('Team', 'sub2', <TeamOutlined/>),
    getItem('Files', '9', <FileOutlined/>),
];

export default function HeadBar() {
    // 根据当前路径判断选中的菜单项（dashboard: 1, user: 2, team: 3）
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
    const pathname = usePathname();

    useEffect(() => {

        if (pathname === '/dashboard' || pathname === '/') {
            setSelectedKeys(['1']);
        } else if (pathname === '/user') {
            setSelectedKeys(['2']);
        } else if (pathname === '/team') {
            setSelectedKeys(['3']);
        }

        console.log('pathname:', pathname, 'selectedKeys:', selectedKeys);
    }, [pathname]); // 监听router.pathname变化

    return (
        <Header style={{display: 'flex', alignItems: 'center', padding: '0 30px'}}>
            <div className="flex items-center justify-center hover:scale-105 my-2 mr-8">
                <div className="h-full text-white font-bold font-franklin text-2xl italic">
                    <a href='/'>PiMonitor</a>
                </div>
            </div>
            <Menu
                theme="dark"
                defaultSelectedKeys={['1']}
                selectedKeys={selectedKeys}
                mode="horizontal"
                style={{flex: 1, minWidth: 0}}
            >
                <Menu.Item key="1" icon={<PieChartOutlined/>}>
                    <Link href="/dashboard">Dashboard</Link>
                </Menu.Item>
                <Menu.Item key="2" icon={<DesktopOutlined/>}>
                    <Link href="/user">User</Link>
                </Menu.Item>
                <Menu.Item key="3" icon={<TeamOutlined/>}>
                    <Link href="/team">Team</Link>
                </Menu.Item>
            </Menu>
        </Header>
    )
}
