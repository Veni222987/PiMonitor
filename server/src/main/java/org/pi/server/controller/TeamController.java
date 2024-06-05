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

@Slf4j
@RestController
@RequestMapping("/v1/team")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamController {
    private final TeamService teamService;

    /**
     * 创建团队
     *
     * @param userID
     * @param teamName
     * @return
     */
    @PostMapping("")
    public Result<Object> create(@GetAttribute("userID") String userID, @RequestParam String teamName) {
        String jwt = teamService.create(Long.parseLong(userID), teamName);
        if (jwt == null) {
            return new Result<>(ResultCode.OPERATION_ERROR);
        }
        return ResultUtils.success(Map.of("token", jwt));
    }

    /**
     * 修改团队信息
     * @param userID
     * @param teamID
     * @param teamName
     * @return
     */
    @PutMapping("")
    public Result<Object> modify(@GetAttribute("userID") @NotNull String userID, @RequestParam String teamID, @RequestParam String teamName) {
        boolean modify = teamService.modify(userID, teamID, teamName);
        return modify ? ResultUtils.success() : ResultUtils.error(ResultCode.NO_AUTH_ERROR);
    }

    /**
     * 获取团队列表
     * @param userID
     * @return
     */
    @GetMapping("/list")
    public Result<Object> list(@GetAttribute("userID") String userID) {
        List<Team> list = teamService.list(userID);
        return ResultUtils.success(list);
    }

    /**
     * 获取团队信息
     * @param userID
     * @param teamID
     * @return
     */
    @GetMapping("")
    public Result<Object> info(@GetAttribute("userID") String userID, @RequestParam String teamID) {
        Team info = teamService.info(userID, teamID);
        return info == null ? ResultUtils.error(ResultCode.NO_AUTH_ERROR) : ResultUtils.success(info);
    }

    /**
     * 删除团队
     * @param userID
     * @param teamID
     * @return
     */
    @DeleteMapping()
    public Result<Object> delete(@GetAttribute("userID") String userID, @RequestParam String teamID) {
        boolean b = teamService.delete(userID, teamID);
        return b ? ResultUtils.success() : ResultUtils.error(ResultCode.OPERATION_ERROR);
    }

    /**
     * 邀请成员
     * @param userID
     * @param type
     * @param teamID
     * @return
     * @throws Exception
     */
    @GetMapping("/invite/{type}")
    public Result<Object> invite(@GetAttribute("userID") String userID, @PathVariable String type, @RequestParam String teamID) throws Exception {
        String invite = teamService.invite(userID, type, teamID);
        if (invite.equals("no_auth")) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        } else if (invite.equals("no_team")) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        }
        return invite.equals("no_type") ? ResultUtils.error(ResultCode.PARAMS_ERROR) : ResultUtils.success(Map.of(type, invite));
    }

    /**
     * 邀请回调
     * @param userID
     * @param code
     * @return
     */
    @GetMapping("/invite/callback")
    public Result<Object> inviteCallback(@GetAttribute("userID") String userID, @RequestParam String code) {
        String s = teamService.inviteCallback(userID, code);
        if (s.equals("no_auth")) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if (s.equals("already_invited")) {
            return ResultUtils.error(ResultCode.REPEAT_OPERATION);
        }
        return ResultUtils.success();
    }
}
