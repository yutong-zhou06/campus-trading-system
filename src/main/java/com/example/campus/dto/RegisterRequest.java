package com.example.campus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册请求对象
 * 用于接收用户注册时提交的表单数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 确认密码（用于验证两次输入是否一致） */
    private String confirmPassword;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /**
     * 检查两次密码是否一致
     * @return true表示一致，false表示不一致
     */
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}