package org.pi.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Team;
import org.pi.server.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hu1hu
 */
@Slf4j
@RestController
@RequestMapping("/v1/team")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamController {
    private final TeamService teamService;

    /**
     * 创建团队
     * @param userID 用户ID
     * @param teamName 团队名称
     * @return ResultCode.SUCCESS 创建成功 ResultCode.OPERATION_ERROR 操作失败
     */
    @PostMapping("")
    public Result<Object> create(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String teamName) {
        String jwt = teamService.create(Long.parseLong(userID), teamName);
        if (jwt == null) {
            return new Result<>(ResultCode.OPERATION_ERROR);
        }
        return ResultUtils.success(Map.of("token", jwt));
    }

    /**
     * 修改团队信息
     * @param userID 用户ID
     * @param teamID 团队ID
     * @param teamName 团队名称
     * @return ResultCode.SUCCESS 修改成功 ResultCode.NO_AUTH_ERROR 无权限
     */
    @PutMapping("")
    public Result<Object> modify(@GetAttribute("userID") @NotNull String userID, @RequestParam String teamID, @RequestParam @NotNull String teamName) {
        boolean modify = teamService.modify(userID, teamID, teamName);
        return modify ? ResultUtils.success() : ResultUtils.error(ResultCode.NO_AUTH_ERROR);
    }

    /**
     * 获取团队列表
     * @param userID 用户ID
     * @return ResultCode.SUCCESS 获取成功
     */
    @GetMapping("/list")
    public Result<Object> list(@GetAttribute("userID") @NotNull String userID) {
        List<Team> list = teamService.list(userID);
        return ResultUtils.success(list);
    }

    /**
     * 获取团队信息
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return ResultCode.SUCCESS 获取成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.NOT_FOUND_ERROR 未找到
     */
    @GetMapping("")
    public Result<Object> info(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String teamID) {
        Team info = teamService.info(userID, teamID);
        return info == null ? ResultUtils.error(ResultCode.NO_AUTH_ERROR) : ResultUtils.success(info);
    }

    /**
     * 删除团队
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return ResultCode.SUCCESS 删除成功 ResultCode.OPERATION_ERROR 操作失败
     */
    @DeleteMapping()
    public Result<Object> delete(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String teamID) {
        boolean b = teamService.delete(userID, teamID);
        return b ? ResultUtils.success() : ResultUtils.error(ResultCode.OPERATION_ERROR);
    }

    /**
     * 邀请成员
     * @param userID 用户ID
     * @param type 邀请类型 Code/QRCode/Link
     * @param teamID 团队ID
     * @return ResultCode.SUCCESS 邀请成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.NOT_FOUND_ERROR 未找到
     * @throws Exception 异常
     */
    @GetMapping("/invite/{type}")
    public Result<Object> invite(@GetAttribute("userID") @NotNull String userID, @PathVariable @NotNull String type, @RequestParam @NotNull String teamID) throws Exception {
        String invite = teamService.invite(userID, type, teamID);
        if ("no_auth".equals(invite)) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        } else if ("no_team".equals(invite)) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        }
        return "no_type".equals(invite) ? ResultUtils.error(ResultCode.PARAMS_ERROR) : ResultUtils.success(Map.of(type, invite));
    }

    /**
     * 邀请回调
     * @param userID 用户ID
     * @param code 邀请码
     * @return ResultCode.SUCCESS 邀请成功 ResultCode.NOT_FOUND_ERROR 未找到
     */
    @GetMapping("/invite/callback")
    public Result<Object> inviteCallback(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String code) {
        String s = teamService.inviteCallback(userID, code);
        if ("no_auth".equals(s)) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if ("already_invited".equals(s)) {
            return ResultUtils.error(ResultCode.REPEAT_OPERATION);
        }
        return ResultUtils.success();
    }
}
