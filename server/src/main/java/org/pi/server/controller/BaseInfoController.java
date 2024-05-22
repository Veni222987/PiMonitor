package org.pi.server.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;


@Slf4j
@RestController
@RequestMapping("/v1/agents/info")
public class BaseInfoController {

    @GetMapping
    public Result<Object> getComputerInfo(@RequestParam String mac) {
        // TODO 查数据库，返回CPU信息
        return new Result<>(ResultCode.SUCCESS);
    }
    

    @PostMapping
    public Result<Object> postComputerInfo(@RequestBody Map<String,Object> body) {
        System.out.println(body.toString());
        log.debug(body.toString());
        // TODO 将body解析后写入数据库
        return new Result<>(ResultCode.SUCCESS);
    }

    @PutMapping
    public Result<Object> putComputerInfo(@RequestBody Map<String,Object> body) {
        System.out.println(body.toString());
        log.debug(body.toString());
        // TODO 将body解析后写入数据库
        return new Result<>(ResultCode.SUCCESS);
    }
}