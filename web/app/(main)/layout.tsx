"use client";
import React from 'react';
import {Layout, theme} from "antd";
import {Content, Footer} from "antd/es/layout/layout";
import HeadBar from "@/app/(main)/componenrs/HeadBar";

export default function MainLayout({ children, }: { children: React.ReactNode }) {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();
    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Layout>
                <HeadBar/>
                <Content style={{ margin: '16px 16px 0 16px' }}>
                    <div
                        style={{
                            padding: 24,
                            minHeight: 360,
                            background: colorBgContainer,
                            borderRadius: borderRadiusLG,
                        }}
                    >
                        {children}
                    </div>
                </Content>
                <Footer style={{ textAlign: 'center' }}>
                    PiMonitor ©{new Date().getFullYear()} Created by Team200PI
                </Footer>
            </Layout>
        </Layout>
    );
}
