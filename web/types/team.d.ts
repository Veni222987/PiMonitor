interface TeamMember {
    id: number
    avatar: string
    username: string
}

interface TeamListType {
    id: number
    createTime: string
    name: string
    owner: number
    token: string
}

export {TeamMember, TeamListType}
