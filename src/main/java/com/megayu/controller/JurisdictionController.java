package com.megayu.controller;

import com.megayu.entity.Jurisdiction;
import com.megayu.entity.User;
import com.megayu.repository.JurisdictionRepositoty;
import com.megayu.util.LoginUtil;
import com.megayu.util.MD5Util;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/login/jurisdiction")
public class JurisdictionController {
    @Autowired
    JurisdictionRepositoty jurisdictionRepositoty;
    @ResponseBody
    @RequestMapping(value = "/getJurisdiction")
    public String getJurisdiction(HttpServletRequest request , HttpServletResponse response, Model model) throws ServletException, IOException {
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        Jurisdiction jurisdiction = jurisdictionRepositoty.findByUserid(loginVo.getId());
        if(jurisdiction==null || jurisdiction.getBookmanage()!=1){
            return "no";
        }
        return "yes";
    }

}
