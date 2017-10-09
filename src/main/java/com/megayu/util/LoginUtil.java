package com.megayu.util;

import com.megayu.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;

public class LoginUtil {

    public static LoginVo getLoginVo(HttpServletRequest request){
        LoginVo loginVo = null;
        try {
            loginVo = (LoginVo)request.getSession().getAttribute("loginVo");
        }catch (Exception e){
            e.printStackTrace();
        }
        return loginVo;
    }
}
