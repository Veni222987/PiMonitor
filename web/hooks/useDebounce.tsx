import {useEffect, useRef} from "react";

export const useDebounce = (fn: any, delay: number) => {
    const ref = useRef({
        fn,
        timer: null
    });

    useEffect(() => {
        // 更新引用
        ref.current.fn = fn;
    }, [fn]);

    function f(...args: any[]) {
        if (ref.current.timer) {
            clearTimeout(ref.current.timer);
        }
        // @ts-ignore
        ref.current.timer = setTimeout(() => {
            // this 指向
            // @ts-ignore
            ref.current.fn.call(this, ...args);
        }, delay);
    }

    return f;
}
