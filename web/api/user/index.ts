import {get, post} from "@/utils/APIUtils";
import {authGet, authPost} from "@/utils/AuthUtils";
import {AuthInfo, UserInfo} from "@/types/user";
import {TeamListType} from "@/types/team";

/**
 * 登录
 * @param {string} account
 * @param {string} password
 */
export const Login = post<{
    account: string,
    password: string
}, {
    jwt: string,
    auths: AuthInfo[],
    user: UserInfo
}>('/v1/users/login')

/**
 * 重置密码
 * @param {string} password
 */
export const ResetPassword = authPost<{
    password: string
}, {}>('/v1/users/resetPassword')

/**
 * 修改用户信息
 * @param {string} username
 * @param {string} avatar
 */
export const ModifyUserInfo = authPost<{
    username: string,
    avatar: string
}, {}>("/v1/users/modify")

/**
 * 查看用户信息
 */
export const GetUserInfo = authGet<{}, {
    auths: AuthInfo[],
    user: UserInfo
}>("/v1/users/info")

/**
 * 查看所属团队信息
 * @param {string} page
 * @param {string} size
 */
export const GetTeamLists = authGet<{
    page?: string,
    size?: string
}, {
    records: TeamListType[],
    total: number,
    size: number,
    current: number,
    pages: number
}>("/v1/users/teamList")
