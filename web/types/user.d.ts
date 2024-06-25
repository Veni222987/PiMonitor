interface AuthInfo {
    id: number
    userId: number
    openId: string
    type: string
    bindTime: string
    name: string
    avatar: string
}

interface UserInfo {
    id: number
    username: string
    avatar: string
    phoneNumber: string
    email: string
    password: string
}

export type {AuthInfo, UserInfo}
