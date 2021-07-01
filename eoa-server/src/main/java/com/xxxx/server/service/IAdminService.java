package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MAQJ
 * @since 2021-05-17
 */
public interface IAdminService extends IService<Admin> {

    // 登录之后返回token
    RespBean login(String username, String password,String code, HttpServletRequest request);

    // 获取用户信息
    Admin getAdminByUsername(String username);

    //  根据用户id或者权限列表
    List<Role> getRoles(Integer adminId);
}
