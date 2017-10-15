package com.megayu.controller;

import com.google.gson.Gson;
import com.megayu.entity.Book;
import com.megayu.repository.ArticleRepository;
import com.megayu.repository.BookRepository;
import com.megayu.util.DateUtil;
import com.megayu.util.LoginUtil;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
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

        @RequestMapping(value = "/openBookSubject")
        public String openBookSubject(HttpServletRequest request , HttpServletResponse response, Model model){
            String pageNum = request.getParameter("page");
            if(pageNum!=null && !"".equals(pageNum)){
                pageNum = (Integer.valueOf(pageNum)+1)+"";
            }else {
                pageNum = "1";
            }
            model.addAttribute("page",pageNum);
            return "book";
        }

    @ResponseBody
    @RequestMapping(value = "/queryBookList")
    public String queryBookList(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        String page = request.getParameter("page");
//        String size = request.getParameter("rows");

        int pagei = 0;
        int sizei = 30;
        if(page!=null && !"".equals(page)){
            pagei = Integer.valueOf(page)-1;
        }
//        if(size!=null && !"".equals(size)){
//            sizei = Integer.valueOf(size);
//        }
//
//        if(pagei>0){
//            pagei--;
//        }

//        String bookid = request.getParameter("bookid");

//        if(bookid!=null && !"".equals(bookid)){
//            Integer bid = Integer.valueOf(bookid);
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        Pageable pageable = new PageRequest(pagei, sizei,sort);
        Page<Book> pages = bookRepository.findByCreateuserOrPublicstatus(loginVo.getId(),1,pageable);
//        }
        List<Book> articleList = pages.getContent();
        if(articleList!=null && articleList.size()>0){
            for (Book book : articleList){
                book.setTime1(DateUtil.editDate(book.getCreatetime()));
            }
        }
        return new Gson().toJson(articleList);
    }
}
