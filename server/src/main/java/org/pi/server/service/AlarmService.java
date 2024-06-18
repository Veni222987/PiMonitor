package org.pi.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.pi.server.model.entity.Alarm;

import java.util.List;

/**
 * @author hu1hu
 * @date 2024/6/13 18:48
 * @description 报警接口
 */
public interface AlarmService {
    /**
     * 获取报警列表
     * @return
     * @throws Exception
     */
    IPage<Alarm> list(String userID, String teamID, int page, int size);

    /**
     * 删除报警
     * @param id
     * @return
     * @throws Exception
     */
    boolean delete(String userID, String hostID, String id);

    /**
     * 更新报警
     * @param id
     * @return
     * @throws Exception
     */
    boolean update(String id, Alarm alarm);

    boolean create(String userID, Alarm alarm);

}
