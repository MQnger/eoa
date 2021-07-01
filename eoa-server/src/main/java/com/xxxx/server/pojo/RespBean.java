package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: MAQJ
 * @Date: 2021/05/21/15:21
 * @Description: 统一返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object object;

    /**
     * 成功返回结果
     *
     * @param message
     * @return
     */
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    /**
     * 成功返回结果
     *
     * @param message
     * @return
     */
    public static RespBean success(String message, Object object) {
        return new RespBean(200, message, object);
    }

    /**
     * 失败返回结果
     *
     * @param message
     * @return
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message
     * @return
     */
    public static RespBean error(String message, Object Object) {
        return new RespBean(500, message, Object);
    }
}
