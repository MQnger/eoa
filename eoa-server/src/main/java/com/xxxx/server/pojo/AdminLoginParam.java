package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: MAQJ
 * @Date: 2021/05/21/15:42
 * @Description:
 *  用于前端参数交互
 *  避免使用Admin类过多不需要的参数
 *  @EqualsAndHashCode(callSuper=true)避免对象比较的时候出错
 *  @Accessors(chain = true) 生成get/set
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
@ApiModel(value="AdminLogin对象",description = "")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;



}
