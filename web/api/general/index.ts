import {post} from "@/utils/APIUtils";
import {authGet, authPost} from "@/utils/AuthUtils";

/**
 * 发送短信验证码
 * @param {string} phoneNumber
 */
export const SendCode = post<{
    account: string,
}, {}>('/v1/common/aliyun/code')

/**
 * 登录或注册验证码验证
 * @param {string} account
 * @param {string} code
 */
export const VerifyLoginCode = authPost<{
    account: string,
    code: string
},{
    jwt: string
}>('v1/common/authCode/loginOrRegister')

/**
 *绑定验证码验证
 * @param {string} account
 * @param {string} code
 */
export const VerifyBindCode = authPost<{
    account: string,
    code: string
},{
    jwt: string
}>("v1/common/authCode/bind")

/**
 *重置密码验证码验证
 * @param {string} account
 * @param {string} code
 */
export const VerifyResetCode = authPost<{
    account: string,
    code: string
},{
    jwt: string
}>("v1/common/authCode/resetPassword")

/**
 * 获取oss签名
 * @param {string} dir
 */
export const GetOssSign = authGet<{
    dir: string
},{
    ossAccessKeyId: string,
    signature: string,
    host: string,
    dir: string,
    policy: string
}>("v1/common/aliyun/oss/signature")
