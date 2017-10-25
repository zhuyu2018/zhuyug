package com.megayu.controller;

import com.megayu.entity.Article;
import com.megayu.repository.ArticleRepository;
import com.megayu.repository.BookRepository;
import com.megayu.util.LoginUtil;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value = "/login/miyu")
public class MiyuController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ArticleRepository articleRepository;
    @ResponseBody
    @RequestMapping(value = "/sendMiyu")
    public String sendMiyu(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        String miyutext = request.getParameter("miyutext");
        if(miyutext==null || miyutext.equals("") || miyutext.equals("undefined")){
            return "发送失败";
        }
        Article article = new Article();
        article.setCreatetime(new Date());
        article.setArticletype(1);
        article.setPublicstatus(1);
        article.setDelstatus(1);
        article.setAuthorname(loginVo.getLoginname());
        article.setAuthor(loginVo.getId());
        article.setCreateuser(loginVo.getId());
        article.setArticlename("觅语");
        article.setArticlecontent(miyutext);
        Article result = articleRepository.save(article);
        if(result==null||result.getId()==null || result.getId()<=0){
            return "发送失败";
        }
        return "success";
    }

}
