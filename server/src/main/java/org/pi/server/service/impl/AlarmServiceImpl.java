package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.mapper.AlarmMapper;
import org.pi.server.mapper.HostMapper;
import org.pi.server.mapper.TeamMapper;
import org.pi.server.model.entity.Alarm;
import org.pi.server.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hu1hu
 * @date 2024/6/13 18:49
 * @description 报警Service 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {
    private final HostMapper hostMapper;
    private final TeamMapper teamMapper;

    @Override
    public IPage<Alarm> list(String userID, String hostID, int page, int size) {
        // 验证用户权限
        Integer teamId = hostMapper.selectById(Long.parseLong(hostID)).getTeamId();
        Long owner = teamMapper.selectById(teamId).getOwner();
        if (!owner.equals(Long.parseLong(userID))) {
            // 无权限
            return null;
        }
        return page(new Page<>(page, size));
    }

    @Override
    public boolean delete(String userID, String hostID, String id){
        // 验证用户权限
        Integer teamId = hostMapper.selectById(Long.parseLong(hostID)).getTeamId();
        Long owner = teamMapper.selectById(teamId).getOwner();
        if (!owner.equals(Long.parseLong(userID))) {
            // 无权限
            return false;
        }
        return removeById(id);
    }

    @Override
    public boolean update(String userID, @NotNull Alarm alarm) {
        // 验证用户权限
        Integer teamId = hostMapper.selectById(alarm.getHostId()).getTeamId();
        Long owner = teamMapper.selectById(teamId).getOwner();
        if (!owner.equals(Long.parseLong(userID))) {
            return false;
        }
        alarm.setUpdatedAt(LocalDateTime.now());
        return updateById(alarm);
    }

    @Override
    public boolean create(String userID, @NotNull Alarm alarm) {
        // 验证用户权限
        Integer teamId = hostMapper.selectById(alarm.getHostId()).getTeamId();
        Long owner = teamMapper.selectById(teamId).getOwner();
        if (!owner.equals(Long.parseLong(userID))) {
            return false;
        }
        alarm.setCreatedAt(LocalDateTime.now());
        alarm.setUpdatedAt(LocalDateTime.now());
        return save(alarm);
    }
}
