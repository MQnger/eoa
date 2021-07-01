package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MAQJ
 * @since 2021-05-17
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 通过用户id获取菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

    /**
     * 根通过角色获取菜单列表
     * @return
     */
    List<Menu> getAllMenusWithRole();
}
