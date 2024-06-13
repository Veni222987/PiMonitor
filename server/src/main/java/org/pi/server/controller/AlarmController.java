package org.pi.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Alarm;
import org.pi.server.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hu1hu
 * @date 2024/6/13 18:46
 * @description 报警Controller
 */

@Slf4j
@RestController
@RequestMapping("/v1/alarm")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AlarmController {
    private final AlarmService alarmService;

    /**
     * 获取报警列表(分页查询)
     * @param userID 用户ID
     * @param hostID 主机ID
     * @param page 页码
     * @param size 每页数量
     * @return Result
     */
    @GetMapping("/list")
    public Result<Object> list(@GetAttribute("userID") String userID,
                               @RequestParam String hostID,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size){
        IPage<Alarm> ipage = alarmService.list(userID, hostID, page, size);
        if (ipage == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(ipage);
    }

    /**
     * 删除报警规则
     * @param id 报警规则ID
     * @return Result
     */
    @DeleteMapping("")
    public Result<Object> delete(@GetAttribute("userID") String userID,
                                 @RequestParam String hostID,
                                 @RequestParam String id) {
        boolean result = alarmService.delete(userID, hostID, id);
        if (!result) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 更新报警规则
     * @param alarm 报警规则
     * @return Result
     */
    @PutMapping("")
    public Result<Object> update(@GetAttribute("userID") String userID,
                                 @RequestBody Alarm alarm){
        boolean b = alarmService.update(userID, alarm);
        if (!b) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 创建报警规则
     * @param userID 用户ID
     * @param alarm 报警规则
     * @return Result
     */
    @PostMapping("")
    public Result<Object> create(@GetAttribute("userID") String userID,
                                 @RequestBody Alarm alarm) {
        boolean b = alarmService.create(userID, alarm);
        if (!b) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success();
    }


}
