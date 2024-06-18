import React, {useEffect, useRef, useState} from "react";
import {Button, Modal} from "antd";
import SliderCaptcha, {ActionType} from "rc-slider-captcha";
import {sleep} from "ut2";


type CaptchaModalProps = {
    isOpen: boolean
    onClose: () => void
    setVerify: (isVerify: boolean) => void
}

const CaptchaModal: React.FC<CaptchaModalProps> = ({isOpen, onClose , setVerify}) => {
    const [isSuccess, setIsSuccess] = useState(false)
    const actionRef = useRef<ActionType>()

    useEffect(() => {
        if (isSuccess) {
            actionRef.current?.refresh()
            setIsSuccess(false)
        }
    }, [isSuccess])

    return (
        <Modal
            open={isOpen}
            closeIcon={null}
            title={null}
            footer={null}
            onCancel={onClose}
            className="w-fit"
            wrapClassName={"CaptchaModal"}
        >
            <h1 className="text-lg mb-2">请先进行安全验证：</h1>
            <SliderCaptcha
                request={async () => {
                    return {
                        bgUrl: '/captcha-bg.jpg',
                        puzzleUrl: '/captcha-puzzle.png'
                    };
                }}
                onVerify={async (data) => {
                    if (data?.x && data.x > 87 && data.x < 93) {
                        await sleep()
                        setVerify(true)
                        onClose()
                        setIsSuccess(true)
                        return Promise.resolve()
                    }
                    return Promise.reject();
                }}
                actionRef={actionRef}
            />
        </Modal>
    )
}

export default CaptchaModal
