package org.pi.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.service.AliyunEmailService;
import org.pi.server.utils.AliEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AliyunEmailServiceImpl implements AliyunEmailService {
    private final AliEmailUtil aliEmailUtil;

    @Override
    public void send(String template, String toAddress, Map<String, String> map) throws Exception {
        String text = aliEmailUtil.buildContent(template, map);
        aliEmailUtil.send(text, toAddress);
    }
}
