package org.pi.server.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController("/api/v1")
public class BaseInfoController {

    @GetMapping("/agent/info")
    public Result<Object> GetComputerInfo(@RequestParam String mac) {
        // TODO 查数据库，返回CPU信息
        return new Result<>(ResultCode.SUCCESS);
    }
    

    @PostMapping("/agent/info")
    public Result<Object> PostComputerInfo(@RequestBody Map<String,Object> body) {
        System.out.println(body.toString());
        log.debug(body.toString());
        // TODO 将body解析后写入数据库
        var res = new Result<>(ResultCode.SUCCESS);
        return res;
    }
    
}
