import {authDel, authGet, authPost, authPut} from "@/utils/AuthUtils";
import {TeamMember} from "@/types/team";

/**
 * 创建团队
 * @param {string} teamName
 */
export const CreateTeam = authPost<{
    teamName: string
}, {
    token: string
}>('/v1/team')

/**
 * 修改团队信息
 * @param {string} teamID
 * @param {string} teamName
 */
export const UpdateTeam = authPut<{
    teamID: string,
    teamName: string
}, {}>('/v1/team')

/**
 * 解散团队
 * @param {string} teamID
 */
export const DismissTeam = authDel<{
    teamID: string
}, {}>('/v1/team')

/**
 * 查看团队信息
 * @param {string} teamID
 */
export const GetTeam = authGet<{
    teamID: string
}, {
    id: string,
    createName: string,
    name: string,
    owner: string,
    token: string
}>('/v1/team')

/**
 * 查看团队成员列表
 * @param {string} teamID
 * @param {string} page
 * @param {string} size
 */
export const GetTeamMembers = authGet<{
    teamID: string,
    page: string,
    size: string
}, {
    records: TeamMember[],
    total: number
    size: number,
    current: number,
    pages: number
}>('/v1/team/members')

/**
 * 邀请
 * @param {string} teamID
 * @pathParam {string} type
 */
export const Invite = authGet<{
    teamID: string
}, {
    code: string
}>('/v1/team/invite/:type')

/**
 * 邀请回调
 * @param {string} code
 */
export const InviteCallback = authGet<{
    code: string
}, {}>('/v1/team/invite/callback')
