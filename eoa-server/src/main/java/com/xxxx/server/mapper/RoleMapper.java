package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MAQJ
 * @since 2021-05-17
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id获取权限列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
