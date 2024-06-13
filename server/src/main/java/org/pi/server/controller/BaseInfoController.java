package org.pi.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Host;
import org.pi.server.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author huhuayu
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/v1/agents")
public class BaseInfoController {
    private final BaseInfoService baseInfoService;


    /**
     * 获取主机信息
     * @param userID 用户ID
     * @param agentID 主机ID
     * @return ResultCode.SUCCESS 获取成功 ResultCode.NO_AUTH_ERROR 无权限
     */
    @GetMapping("/info")
    public Result<Object> getComputerInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String agentID) {
        Host host = baseInfoService.getComputerInfo(userID, agentID);
        if (host == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(host);
    }

    /**
     * 注册主机
     * @param host 主机信息
     * @return ResultCode.SUCCESS 注册成功 ResultCode.PARAMS_ERROR 参数错误 ResultCode.OPERATION_ERROR 操作失败
     */
    @PostMapping("/info")
    public Result<Object> postComputerInfo(@GetAttribute("teamID") @NotNull Integer teamID, @NotNull @RequestBody Host host) {
        host.setTeamId(teamID);
        long id = baseInfoService.postComputerInfo(host);
        if (id == 0) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if (id == -1) {
            return ResultUtils.error(ResultCode.OPERATION_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return ResultUtils.success(map);
    }

    /**
     * 更新主机信息
     * @param agentID 主机ID
     * @param hostname 主机名
     * @return ResultCode.SUCCESS 更新成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.PARAMS_ERROR 参数错误
     */
    @PutMapping("/info")
    public Result<Object> putComputerInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String agentID, @RequestParam @NotNull String hostname) {
        boolean b = baseInfoService.putComputerInfo(userID ,agentID, hostname);
        if (!b) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 查看主机列表
     * @param userID 用户ID
     * @param teamID 团队信息
     * @param page 页码
     * @param size 每页数量
     */
    @GetMapping("/list")
    public Result<Object> getList(@GetAttribute("userID") @NotNull String userID,
                                          @RequestParam(value = "teamID", required = false) String teamID,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        IPage<Host> pages = baseInfoService.getList(userID, teamID, page, size);
        if (pages == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(pages);
    }
}
