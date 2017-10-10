package com.megayu.controller;

import com.google.gson.Gson;
import com.megayu.entity.Article;
import com.megayu.entity.Book;
import com.megayu.repository.ArticleRepository;
import com.megayu.repository.BookRepository;
import com.megayu.util.LoginUtil;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/login/article")
public class ArticleController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ArticleRepository articleRepository;

    @ResponseBody
    @RequestMapping(value = "/queryArticle")
    public String queryArticle(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        List<Article> articles = queryArticle(2,loginVo.getId());
        Gson gson = new Gson();
        return gson.toJson(articles);
    }

    @ResponseBody
    @RequestMapping(value = "/queryMiyu")
    public String queryMiyu(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        List<Article> articles = queryArticle(1,loginVo.getId());
        Gson gson = new Gson();
        return gson.toJson(articles);
    }

    @ResponseBody
    @RequestMapping(value = "/queryBook")
    public String queryBook(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        List<Book> books = queryBook(loginVo.getId());
//        List<Article> articles = queryArticle(2,loginVo.getId());
        Gson gson = new Gson();
        return gson.toJson(books);
    }

    public List<Book> queryBook(final Integer userid){
//        List<Book> books = bookRepository.findAll(new Specification<Book>() {
//            @Override
//            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> list = new ArrayList<Predicate>();
//
//                if (userid>0) {
//                    list.add(cb.equal(root.get("createuser").as(Integer.class), userid));
//                }
//
//                Predicate[] p = new Predicate[list.size()];
//                return cb.and(list.toArray(p));
//            }
//
//        });

//        return books;
        return bookRepository.queryBookLimit(userid,10);
    }

    public List<Article> queryArticle(final Integer type, final Integer userid){
//        List<Article> articleList = articleRepository.findAll(new Specification<Article>() {
//            @Override
//            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> list = new ArrayList<Predicate>();
//
//                if (type>0) {
//                    list.add(cb.equal(root.get("articletype").as(Integer.class), type));
//                }
//
//                if (userid>0) {
//                    list.add(cb.equal(root.get("createuser").as(Integer.class), userid));
//                }
//
//                Predicate[] p = new Predicate[list.size()];
//                return cb.and(list.toArray(p));
//            }
//
//        });
//        return articleList;
        return articleRepository.queryArticleLimit(userid,type,10);
    }
}
