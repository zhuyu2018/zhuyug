package com.megayu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
public class MegayuController {

    @RequestMapping(value = "/")
    public String index(HttpServletRequest request , HttpServletResponse response,Model model){

        String name = "apple";
        model.addAttribute("name",name);
        return "index";
    }
    @RequestMapping(value = "/welcome")
    public String welcome(HttpServletRequest request , HttpServletResponse response,Model model){

//        String name = "apple";
//        model.addAttribute("name",name);
        return "welcome";
    }

}
