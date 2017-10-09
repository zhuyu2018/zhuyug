package com.megayu.controller;

import com.megayu.entity.User;
import com.megayu.repository.UserRepositoty;
import com.megayu.util.LoginUtil;
import com.megayu.util.MD5Util;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    UserRepositoty userRepository;

    @ResponseBody
    @RequestMapping(value = "/toLogin")
    public String toLogin(HttpServletRequest request , HttpServletResponse response, Model model) throws ServletException, IOException {
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        if(loginName==null || "".equals(loginName) ||password==null || password.equals("")){
            return "fail";
        }
        //用户名密码校验
        User userResult = userRepository.findByLoginnameAndPassword(loginName,MD5Util.MD5EncodeUTF8(password));
        if(userResult==null){
            return "fail";
        }else{
            LoginVo loginVo = new LoginVo();
            loginVo.setId(userResult.getId());
            loginVo.setLoginname(userResult.getLoginname());
            loginVo.setName(userResult.getName());
            request.getSession().setAttribute("loginVo",loginVo);
            return "success";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/toRegister")
    public String toRegister(HttpServletRequest request , HttpServletResponse response, Model model) throws ServletException, IOException {
        String registerName = request.getParameter("registerName");
        String registerPassword = request.getParameter("registerPassword");
        String registerSurePassword = request.getParameter("registerSurePassword");
        if(registerName==null || "".equals(registerName) ||registerPassword==null || registerPassword.equals("")||registerSurePassword==null || registerSurePassword.equals("") || !registerSurePassword.equals(registerPassword)){
            return "fail";
        }
        User user = new User();
        user.setLoginname(registerName);
        User userResult = userRepository.findOne(Example.of(user));
        if(userResult!=null){
            return "fail";
        }
        String encodePassword = MD5Util.MD5EncodeUTF8(registerPassword);
        user.setName(registerName);
        user.setPassword(encodePassword);
        userRepository.save(user);
        //注册
        return "success";
    }

    @RequestMapping(value = "/loginSuccess")
    public String loginSuccess(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
//        String loginName = (String) request.getSession().getAttribute("loginName");
        model.addAttribute("loginName",loginVo.getLoginname());
        return "loginsuccess";
    }

    @ResponseBody
    @RequestMapping(value="/loginOut")
    public String loginOut(HttpServletRequest request , HttpServletResponse response, Model model){
        request.getSession().invalidate();
        return "out";
    }
    @RequestMapping(value="infoEdit")
    public String userInfoEdit(HttpServletRequest request , HttpServletResponse response, Model model){
        try{
            LoginVo loginVo = LoginUtil.getLoginVo(request);
            request.setAttribute("loginName",loginVo.getLoginname());
            request.setAttribute("id",loginVo.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "infoedit";
    }
    @ResponseBody
    @RequestMapping(value="infoEditSave")
    public String infoEditSave(HttpServletRequest request , HttpServletResponse response, Model model){
        try{
//            String loginName = (String)request.getSession().getAttribute("loginName");
            LoginVo loginVo = LoginUtil.getLoginVo(request);
            String password = (String)request.getParameter("password");
            String passwordsure = (String)request.getParameter("passwordsure");
            String loginNameForm = (String)request.getParameter("loginName");
            if(loginVo.getLoginname()!=loginNameForm){
                return "fail";
            }
            if(password==null || "".equals(password)){
                return "fail";
            }
            if(!password.equals(passwordsure)){
                return "fail";
            }
            User user = new User();
            user.setId(loginVo.getId());
            User userResult = userRepository.findOne(Example.of(user));
            userResult.setPassword(MD5Util.MD5EncodeUTF8(password));
            userRepository.save(userResult);

            return "success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "fail";
    }
}
