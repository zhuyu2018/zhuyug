package com.megayu.controller;

import com.google.gson.Gson;
import com.megayu.entity.Article;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/login/bookManage")
public class BookManageController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ArticleRepository articleRepository;
    @RequestMapping(value = "/openBookManage")
    public String openBookManage(HttpServletRequest request , HttpServletResponse response, Model model){

        return "booklist";
    }

    @ResponseBody
    @RequestMapping(value = "/queryBookPage")
    public String queryBookPage(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        String page = request.getParameter("page");
        String size = request.getParameter("rows");

        int pagei = 0;
        int sizei = 30;
        if(page!=null && !"".equals(page)){
            pagei = Integer.valueOf(page);
        }
        if(size!=null && !"".equals(size)){
            sizei = Integer.valueOf(size);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        Pageable pageable = new PageRequest(pagei, sizei,sort);
        Page<Book> pages = bookRepository.findByCreateuser(loginVo.getId(),pageable);
        List<Book> bookList = pages.getContent();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if(bookList!=null && bookList.size()>0){
            for (Book book : bookList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("bookname",book.getBookname());
                map.put("remark",book.getRemark());
                map.put("authorname",book.getAuthorname());
                map.put("createtime",DateUtil.editDate(book.getCreatetime()));
                List<Article> articleList = articleRepository.findByBookid(book.getId());
                map.put("updatetime",DateUtil.editDate(book.getCreatetime()));
                if(articleList!=null&&articleList.size()>0){
                    map.put("articleCount",articleList.size());
                    map.put("updatetime",DateUtil.editDate(articleList.get(0).getCreatetime()));
                    map.put("newArticleName",articleList.get(0).getArticlename());
                }else{
                    map.put("articleCount",0);
                    map.put("newArticleName","无");
                }
                resultList.add(map);
            }
        }
        return new Gson().toJson(resultList);
    }
}
