package org.pi.server.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class AliSmsTest {

    @Autowired
    private AliSmsUtils aliSmsUtils;
    @Test
    public void send() throws Exception {
        aliSmsUtils.send("1234", "19303012224");
    }
}
