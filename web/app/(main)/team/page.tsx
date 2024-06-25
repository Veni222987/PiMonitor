"use client";
import React, {useState, useEffect} from 'react';
import {GetTeamMembers, CreateTeam, UpdateTeam, DismissTeam, Invite, InviteCallback} from '@/api/team';
import {Button, Input, Modal, Popconfirm, Space, Table, TableProps, Tag, Tooltip} from "antd";
import {TeamListType, TeamMember} from "@/types/team";
import {GetTeamLists} from "@/api/user";
import {getLocalStorage} from "@/utils/StorageUtils";

export default function Team() {
    // 团队成员列表
    const [teamMembers, setTeamMembers] = useState([] as TeamMember[]);
    // 团队列表
    const [teamList, setTeamList] = useState([] as any);
    // 加载状态
    const [loading, setLoading] = useState(true);
    // 设置创建团队弹窗打开
    const [isCreateOpen, setIsCreateOpen] = useState(false);
    // 设置加入团队窗口打开
    const [isJoinOpen, setIsJoinOpen] = useState(false);
    // 设置成员列表弹窗打开
    const [isMemberOpen, setIsMemberOpen] = useState(false);
    // 设置邀请弹窗打开
    const [isInviteOpen, setIsInviteOpen] = useState(false);
    // 设置编辑团队弹窗打开
    const [isUpdateOpen, setIsUpdateOpen] = useState(false);
    // 新创建的团队名称
    const [teamName, setTeamName] = useState('');
    // 更改后的团队名称
    const [newTeamName, setNewTeamName] = useState('');
    // 生成的邀请码
    const [Code, setCode] = useState('');
    // 复制的邀请码
    const [invitationCode, setInvitationCode] = useState('');
    // 当前选择的团队ID
    const [teamID, setTeamID] = useState('');
    // 当前选中团队的管理员ID
    const [teamOwnerID, setTeamOwnerID] = useState(0);

    const columns: TableProps<TeamListType>['columns'] = [
        {
            title: '团队ID',
            dataIndex: 'id',
            key: 'id',
            width: 100,
            align: 'center',
        },
        {
            title: '团队名',
            dataIndex: 'name',
            key: 'name',
            width: 150,
            align: 'center',
        },
        {
            title: '所有者ID',
            dataIndex: 'owner',
            key: 'owner',
            width: 100,
            align: 'center',
        },
        {
            title: '成员列表',
            dataIndex: 'members',
            key: 'members',
            align: 'center',
            render: (members, records) => (
                <Button type="link" onClick={() => {
                    setTeamOwnerID(records.owner)
                    handleGetMembers(records.id + '').then()
                }}>查看详情</Button>
            )
        },
        {
            title: 'Token',
            dataIndex: 'token',
            key: 'token',
            align: 'center',
            ellipsis: {
                showTitle: false,
            },
            render: token => (
                <Tooltip placement="topLeft" title={token}>
                    {token}
                </Tooltip>
            )
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            key: 'createTime',
            align: 'center',
        },
        {
            title: '操作',
            key: 'action',
            width: 200,
            align: 'center',
            render: (text, record) => (
                <Space size="middle">
                    {record.owner === JSON.parse(getLocalStorage('userInfo') as string)?.id && <Button
                        size={"small"}
                        onClick={() => {
                            setIsInviteOpen(true)
                            handleInvite(record.id + '').then()
                        }}
                    >
                        邀请
                    </Button>}
                    {record.owner === JSON.parse(getLocalStorage('userInfo') as string)?.id && <Button
                        size={"small"}
                        onClick={() => {
                            setIsUpdateOpen(true)
                            setTeamID(record.id + '')
                        }}
                    >
                        编辑
                    </Button>}
                    {record.owner === JSON.parse(getLocalStorage('userInfo') as string)?.id && <Popconfirm
                        title={`确定要解散团队 ${record.name} 吗？`}
                        onConfirm={() => handleDismissTeam(record.id + '')}>
                        <Button
                            size={"small"}
                            danger
                        >解散</Button>
                    </Popconfirm>}
                </Space>
            ),
        },
    ];

    // 获取团队信息
    useEffect(() => {
        const fetchTeamInfo = async () => {
            try {
                const {records} = await GetTeamLists();
                setTeamList(records);
                console.log(records);
            } catch (error) {
                console.error('Error fetching team info: ', error);
            }
        };

        fetchTeamInfo().then(r => {
        });
    }, []);

    // 创建团队
    const handleCreateTeam = async () => {
        await CreateTeam({teamName});
        // Refresh team data or take other necessary actions
        await handleResetTeam();
        setIsCreateOpen(false);
    };

    // 获取成员列表
    const handleGetMembers = async (teamID: string) => {
        const {records} = await GetTeamMembers({teamID, page: '1', size: '10'});
        setTeamMembers(records);
        showMemberModal();
    };

    // 重新设置团队信息
    const handleResetTeam = async () => {
        const {records} = await GetTeamLists();
        setTeamList(records);
    };

    // 更新团队信息
    const handleUpdateTeam = async (teamID: string, teamName: string) => {
        await UpdateTeam({teamID, teamName});
        // Refresh team data or take other necessary actions
        await handleResetTeam();
    };

    // 解散团队
    const handleDismissTeam = async (teamID: string) => {
        await DismissTeam({teamID});
        // Perform actions after team dismissal
        await handleResetTeam();
    };

    // 邀请成员
    const handleInvite = async (teamID: string) => {
        const {Code} = await Invite({teamID, type: 'Code'});
        // Handle invite response as needed
        setCode(Code);
    };

    // 处理邀请回调
    const handleInviteCallback = async () => {
        await InviteCallback({code: invitationCode});
        // Handle callback response as needed
        await handleResetTeam();
        setIsJoinOpen(false)
    };

    // showMemberModal
    const showMemberModal = () => {
        setIsMemberOpen(true);
    }

    // showCreateModal
    const showCreateModal = () => {
        setIsCreateOpen(true);
    }

    // showJoinModal
    const showJoinModal = () => {
        setIsJoinOpen(true);
    }

    // handleTeamNameChange
    const handleTeamNameChange = (e: any) => {
        setTeamName(e.target.value);
    }

    // handleInvitationCodeChange
    const handleInvitationCodeChange = (e: any) => {
        setInvitationCode(e.target.value);
    }

    // handleNewTeamNameChange
    const handleNewTeamNameChange = (e: any) => {
        setNewTeamName(e.target.value);
    }

    return (
        <div className="flex flex-col">
            <h1 className="text-2xl mr-auto mb-2">我的团队</h1>
            <div className="flex mb-2 ml-auto gap-2">
                <Button onClick={showCreateModal}>创建团队</Button>
                <Button onClick={showJoinModal}>加入团队</Button>
            </div>
            <Table

                dataSource={teamList}
                columns={columns}
            />
            <Modal
                title="创建团队"
                footer={null}
                open={isCreateOpen}
                onCancel={() => {
                    setIsCreateOpen(false)
                }}
            >
                <div className="flex flex-col gap-4 mt-6 items-center">
                    <Input
                        className="w-1/2"
                        placeholder="请输入团队名称"
                        value={teamName}
                        onChange={handleTeamNameChange}
                    />
                    <Button
                        className="w-1/4"
                        type="primary"
                        onClick={handleCreateTeam}
                    >
                        创建
                    </Button>
                </div>
            </Modal>
            <Modal
                title="加入团队"
                footer={null}
                open={isJoinOpen}
                onCancel={() => {
                    setIsJoinOpen(false)
                }}
            >
                <div className="flex flex-col gap-4 mt-6 items-center">
                    <Input
                        className="w-1/2"
                        placeholder="请输入邀请码"
                        value={invitationCode}
                        onChange={handleInvitationCodeChange}
                    />
                    <Button
                        className="w-1/4"
                        type="primary"
                        onClick={handleInviteCallback}
                    >
                        加入
                    </Button>
                </div>
            </Modal>
            <Modal
                title="成员列表"
                footer={null}
                open={isMemberOpen}
                onCancel={() => {
                    setIsMemberOpen(false)
                }}
            >
                <Table
                    dataSource={teamMembers}
                    columns={[
                        {
                            title: '成员ID',
                            dataIndex: 'id',
                            key: 'id',
                        },
                        {
                            title: '成员名',
                            dataIndex: 'userName',
                            key: 'userName',
                        },
                        {
                            title: '成员角色',
                            dataIndex: 'role',
                            key: 'role',
                            render: (role, member) => (
                                <Tag color="blue">{teamOwnerID === member.id ? '管理员' : '普通成员'}</Tag>
                            )
                        },
                    ]}
                />
            </Modal>
            <Modal
                title="邀请成员"
                footer={null}
                open={isInviteOpen}
                onCancel={() => {
                    setIsInviteOpen(false)
                }}
            >
                <div className="flex flex-col gap-4 mt-6 items-center">
                    <p>
                        邀请码：{Code}
                    </p>
                    <p className="text-[10px] text-gray-400">
                        请将邀请码发送给需要邀请的成员
                    </p>
                </div>
            </Modal>
            <Modal
                title="编辑团队"
                footer={null}
                open={isUpdateOpen}
                onCancel={() => {
                    setIsUpdateOpen(false)
                    setTeamID('')
                }}
            >
                <div className="flex flex-col gap-4 mt-6 items-center">
                    <Input
                        className="w-1/2"
                        placeholder="请输入新团队名称"
                        value={newTeamName}
                        onChange={handleNewTeamNameChange}
                    />
                    <Button
                        className="w-1/4"
                        type="primary"
                        onClick={() => {
                            handleUpdateTeam(teamID, newTeamName).then(res => {
                                setIsUpdateOpen(false)
                                setTeamID('')
                            })
                        }}
                    >
                        更新
                    </Button>
                </div>
            </Modal>
        </div>
    );
};
