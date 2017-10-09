package com.megayu.filter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/login/*",filterName = "loginFilter")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        request.setAttribute("contextPath",contextPath);
        if(urlFilter(url)){
            filterChain.doFilter(request,response);
        }else{
            Object loginName = request.getSession().getAttribute("loginName");
            if(loginName==null || "".equals(loginName.toString())){
                System.out.print("被拦截的url："+url+"");
                response.setHeader("Cache-Control", "no-store");
                response.setDateHeader("Expires", 0);
                response.setHeader("Prama", "no-cache");
                response.sendRedirect("/");
            }
            filterChain.doFilter(request,response);
        }

    }

    public boolean urlFilter(String url){
        if("/login/toLogin".equals(url)){
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}