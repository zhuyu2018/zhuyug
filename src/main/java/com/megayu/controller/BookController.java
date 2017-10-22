package com.megayu.controller;

import com.google.gson.Gson;
import com.megayu.entity.Book;
import com.megayu.repository.ArticleRepository;
import com.megayu.repository.BookRepository;
import com.megayu.util.DateUtil;
import com.megayu.util.LoginUtil;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/login/book")
public class BookController {
        @Autowired
        BookRepository bookRepository;
        @Autowired
        ArticleRepository articleRepository;
        @Autowired
        private EntityManager entity;

    @RequestMapping(value = "/openBookSubject")
        public String openBookSubject(HttpServletRequest request , HttpServletResponse response, Model model){
            String pageNum = request.getParameter("page");
            String bookname = request.getParameter("bookname");
            if(pageNum!=null && !"".equals(pageNum)){
                pageNum = (Integer.valueOf(pageNum)+1)+"";
            }else {
                pageNum = "1";
            }
            model.addAttribute("page",pageNum);
            if(bookname!=null && !"".equals(bookname) && !"undefined".equals(bookname)){
                model.addAttribute("bookname",bookname);
            }

            return "book";
        }

    @ResponseBody
    @RequestMapping(value = "/queryBookList")
    public String queryBookList(HttpServletRequest request , HttpServletResponse response, Model model){
        final LoginVo loginVo = LoginUtil.getLoginVo(request);
        String page = request.getParameter("page");
        String bookname = request.getParameter("bookname");
//        String size = request.getParameter("rows");

        int pagei = 0;
        int sizei = 30;
        if(page!=null && !"".equals(page)){
            pagei = (Integer.valueOf(page)-1)*sizei;
        }

        List<Book> articleList = this.queryBook30Auto(loginVo.getId(),bookname,pagei,sizei);
        if(articleList!=null && articleList.size()>0){
            for (Book book : articleList){
                book.setTime1(DateUtil.editDate(book.getCreatetime()));
            }
        }
        return new Gson().toJson(articleList);
    }

    public List<Book> queryBook30Auto(Integer userid,String bookname,int page,int size){
        StringBuffer sql = new StringBuffer("");
        sql.append(" select id,bookname,remark,author,authorname,createtime,edittime,createuser,edituser,publicstatus,delstatus,time1,time2 from y_book where 1=1 ");
        sql.append(" and (createuser="+userid+" or publicstatus=1) and delstatus=1 ");
        if(bookname!=null && !"".equals(bookname) && !"undefined".equals(bookname)){
            sql.append(" and bookname like '%"+bookname+"%' ");
        }
        sql.append(" order by id desc limit "+page+","+size+" ");
        Query query = entity.createNativeQuery(sql.toString(), Book.class);
        List<Book> books = query.getResultList();
        return  books;
    }


}
