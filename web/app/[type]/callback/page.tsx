"use client";

import {useEffect} from "react";
import {setLocalStorage} from "@/utils/StorageUtils";

export default function CallbackPage() {

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

        // 将code存到Localstorage中
        setLocalStorage('authCode', code);
        // 将state存到Localstorage中
        setLocalStorage('authState', state);
    }, []);

    return (
        <div className="w-full h-full flex items-center justify-center">
            <h1 className="text-2xl">正在校验第三方授权...</h1>
        </div>
    );
}
