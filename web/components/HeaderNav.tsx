import { GithubOutlined } from '@ant-design/icons'
import React from 'react'
import { Avatar } from 'antd'
import type { ChildrenProps } from '@/types/headerNav.d.ts'

const HeaderNav: React.FC<ChildrenProps> = (props) => {

    return (
        // left
        <div className="fixed w-full h-10vh flex justify-between items-center px-6 py-4 z-10">
            <div className="flex items-center hover:scale-105">
                <div className="h-full text-white font-bold font-franklin text-4xl italic">
                    <a href='/'>PiMonitor</a>
                </div>
            </div>

            <div className="flex justify-center items-center">
                <div className="rounded-full transition-transform transform hover:scale-105">
                    <div className='url'>
                        <a href='https://github.com/ApiKnight' target='_blank'>
                            <Avatar
                                size={36}
                                style={{ backgroundColor: 'rgba(0, 0, 0, 0.6)' }}
                                icon={<GithubOutlined />}
                                alt='Github'
                            />
                        </a>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default HeaderNav
