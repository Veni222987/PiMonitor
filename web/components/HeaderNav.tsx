import { GithubOutlined } from '@ant-design/icons'
import React from 'react'
import { Avatar } from 'antd'
import type { ChildrenProps } from '@/types/headerNav.d.ts'

const HeaderNav: React.FC<ChildrenProps> = (props) => {

    return (
        // left
        <div className="fixed w-full h-10vh flex justify-between items-center px-6 py-4 z-10">
            <div className="flex items-center hover:scale-105">
                <div className="h-full text-white font-bold font-franklin text-2xl italic">
                    <a href='/'>PiMonitor</a>
                </div>
            </div>

            {/* right */}
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
                {/*{!ifHideUser && (*/}
                {/*    <div className="flex items-center justify-center leading-9">*/}
                {/*        <div className="leading-9">*/}
                {/*            <Link*/}
                {/*                className="block transition-transform transform hover:scale-105"*/}
                {/*                href='/'>*/}
                {/*                <Avatar*/}
                {/*                    size={40}*/}
                {/*                    style={{ backgroundColor: 'black', marginLeft: '10px' }}*/}
                {/*                    icon={!user_info?.avatar_url && <UserOutlined />}*/}
                {/*                    src={user_info?.avatar_url}*/}
                {/*                    alt='用户头像'*/}
                {/*                />*/}
                {/*            </Link>*/}
                {/*        </div>*/}

                {/*        <div className="text-center text-indigo-600 ml-4">{user_info?.username || null}</div>*/}
                {/*    </div>*/}
                {/*)}*/}
            </div>
        </div>
    )
}

export default HeaderNav
