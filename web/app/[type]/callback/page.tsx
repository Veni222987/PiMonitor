"use client";
import {useEffect} from "react";
import {ThirdPartyCallback} from "@/api/thirdParty";
import {useRouter} from "next/navigation";
import {AuthInfo, UserInfo} from "@/types/user";
import {setupToken} from "@/utils/AuthUtils";

export default function CallbackPage() {

    // 初始化router
    const router = useRouter();

    // 将用户信息存到Localstorage中
    const saveUserInfo = (user: UserInfo) => {
        localStorage.setItem("userInfo", JSON.stringify(user))
    }

    // 将授权信息存到Localstorage中
    const saveAuthInfo = (auths: AuthInfo[]) => {
        localStorage.setItem("auths", JSON.stringify(auths))
    }

    useEffect(() => {
        // 使用原生方法从?code=xxx中获取code
        function getUrlParameter(name: string) {
            name = name.replace(/[[]/, "\$$ ").replace(/[ $$]/, "\\]");
            let regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
            let results = regex.exec(window.location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }

        // 存储code和state
        let code = getUrlParameter('code');
        let state = getUrlParameter('state');

        // 处理第三方回调
        const handleCallback = async () => {
            try {
                // 向服务端发生校验数据并接受回调信息
                const {jwt, auths, user} = await ThirdPartyCallback({
                    type: window.location.pathname.split('/')[1],
                    code,
                    state
                });
                // 数据本地化存储
                setupToken(jwt);
                saveUserInfo(user);
                saveAuthInfo(auths);
                // 跳转到首页
                router.push('/');
            } catch (e) {
                console.error(e);
            }
        }

        // 非空校验
        if (code && state) {
            handleCallback().then()
        }
    }, []);

    return (
        <div className="w-full h-full flex items-center justify-center">
            <h1 className="text-2xl">正在校验第三方授权...</h1>
        </div>
    );
}
