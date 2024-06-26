"use client";
import {useEffect} from "react";
import {ThirdPartyCallback} from "@/api/thirdParty";
import {useRouter} from "next/navigation";

export default function CallbackPage() {

    const router = useRouter();

    useEffect(() => {
        // 使用原生方法从?code=xxx中获取code
        function getUrlParameter(name: string) {
            name = name.replace(/[[]/, "\$$ ").replace(/[ $$]/, "\\]");
            let regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
            let results = regex.exec(window.location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }

        // 使用示例
        let code = getUrlParameter('code');
        let state = getUrlParameter('state');

        // // 将code存到Localstorage中
        // setLocalStorage('authCode', code);
        // // 将state存到Localstorage中
        // setLocalStorage('authState', state);

        const handleCallback = async () => {
            try {
                const res = await ThirdPartyCallback({
                    type: window.location.pathname.split('/')[1],
                    code,
                    state
                });
                console.log('res:', res);
                // 跳转到首页
                router.push('/');
            } catch (e) {
                console.error(e);
            }
        }

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
