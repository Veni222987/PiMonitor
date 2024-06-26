import {get, post} from "@/utils/APIUtils";
import {authDel, authGet} from "@/utils/AuthUtils";
import {AuthInfo, UserInfo} from "@/types/user";

/**
 * 第三方登录
 * @param {string} type
 */
export const ThirdPartyLogin = get<{
    type: string
}, {
    redirectURL: string
}>(`/v1/oauth/login/:type`)

/**
 * 绑定第三方账号
 * @param {string} type
 */
export const BindThirdParty = authGet<{
    type: string
}, {
    redirectURL: string
}>(`/v1/oauth/bind/:type`)

/**
 * 第三方接触绑定
 * @param {string} type
 */
export const UnbindThirdParty = authDel<{
    type: string
}, {}>(`/v1/oauth/unbind/:type`)

/**
 * 第三方授权回调
 * @param {string} type
 * @param {string} code
 * @param {string} state
 */
export const ThirdPartyCallback = get<{
    type: string,
    code: string,
    state: string
}, {
    jwt: string,
    auths: AuthInfo[],
    user: UserInfo
}>('/v1/oauth/:type/callback?code=:code&state=:state')
