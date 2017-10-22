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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        Gson gson = new Gson();
        return gson.toJson(books);
    }
    public List<Book> queryBook(final Integer userid){
        List<Book> books = bookRepository.queryBookLimit(userid,10);
        if(books!=null && books.size()>0){
            for(Book book:books){
                book.setTime1(DateUtil.editDate(book.getCreatetime()));
            }
        }
        return books;
    }
    public List<Article> queryArticle(final Integer type, final Integer userid){
        List<Article> articles = articleRepository.queryArticleLimit(userid,type,10);
        if(articles!=null && articles.size()>0){
            for(Article article : articles){
                article.setTime1(DateUtil.editDate(article.getCreatetime()));
            }
        }
        return articles;
    }
    @RequestMapping(value = "/openBook")
    public String openBook(HttpServletRequest request , HttpServletResponse response, Model model){
        String bookid = request.getParameter("bookid");
        String bookname = request.getParameter("bookname");
        Book book = bookRepository.findByIdAndDelstatus(Integer.valueOf(bookid),1);
        model.addAttribute("bookid",bookid);
        model.addAttribute("bookname",bookname);
        model.addAttribute("authorname",book.getAuthorname());
        return "article";
    }
    @ResponseBody
    @RequestMapping(value = "/queryArticlePage")
    public String queryArticlePage(HttpServletRequest request , HttpServletResponse response, Model model){
        String page = request.getParameter("page");
        String size = request.getParameter("rows");
        int pagei = 0;
        int sizei = 100;
        if(page!=null && !"".equals(page)){
            pagei = Integer.valueOf(page);
        }
        if(size!=null && !"".equals(size)){
            sizei = Integer.valueOf(size);
        }
        if(pagei>0){
            pagei--;
        }
        String bookid = request.getParameter("bookid");
        Page<Article> pages = null;
        if(bookid!=null && !"".equals(bookid)){
            Integer bid = Integer.valueOf(bookid);
            Sort sort = new Sort(Sort.Direction.DESC, "createtime");
            Pageable pageable = new PageRequest(pagei, sizei,sort);
            pages = articleRepository.findByBookidOrderByArticlesortAsc(bid,pageable);
        }
        List<Article> articleList = pages.getContent();
        return new Gson().toJson(articleList);
    }
    @ResponseBody
    @RequestMapping(value = "/queryArticleList")
    public String queryArticleList(HttpServletRequest request , HttpServletResponse response, Model model){
        String bookid = request.getParameter("bookid");
        Integer bid = 0;
        if(bookid!=null && !"".equals(bookid) &&!"undefined".equals(bookid)){
            bid = Integer.valueOf(bookid);
        }
        List<Article> articleList = articleRepository.findByBookid(bid);
        return new Gson().toJson(articleList);
    }
    @RequestMapping(value = "/openArticleDetail")
    public String openArticleDetail(HttpServletRequest request , HttpServletResponse response, Model model){
        String bookid = request.getParameter("bookid");
        String articleid = request.getParameter("articleid");//当前章节id
        String zhangxu = request.getParameter("zhang");
        if (articleid==null || articleid.equals("") || articleid.equals("undefined")){
            return "error";
        }
        if (bookid==null || bookid.equals("") || bookid.equals("undefined")){
            return "error";
        }
        Article article = articleRepository.findByIdAndDelstatusAndPublicstatus(Integer.valueOf(articleid),1,1);
        if (article==null){
            return "error";
        }
        String replaceStr = "</p><p>&nbsp;&nbsp;";
        Book book = bookRepository.findByIdAndDelstatus(Integer.valueOf(bookid),1);
        model.addAttribute("prevId",queryOtherArticleIdByCurrentSort(Integer.valueOf(bookid),article.getArticlesort(),"prev"));
        model.addAttribute("nextId",queryOtherArticleIdByCurrentSort(Integer.valueOf(bookid),article.getArticlesort(),"next"));
        model.addAttribute("bookname",book.getBookname());
        model.addAttribute("zhangxu",zhangxu);
        model.addAttribute("updatetime",DateUtil.editDateTime(article.getCreatetime()));
        model.addAttribute("authorname",book.getAuthorname());
        model.addAttribute("articlename",article.getArticlename());
        String articleContent = "<p>&nbsp;&nbsp;"+article.getArticlecontent().trim().replace("\n",replaceStr)+"</p>";
        model.addAttribute("articleContent",articleContent);
        model.addAttribute("contentLength",article.getArticlecontent().length());
        model.addAttribute("bookid",bookid);
        return "articledetail";
    }

    public String queryOtherArticleIdByCurrentSort(Integer bookid,Integer sort,String type){
        String id = "-1";
        Integer countArticle = 0;
        if("next".equals(type)){
            countArticle = articleRepository.findByBookid(bookid).size();
        }
        int i = 0;
        if (sort!=null&&sort>0){
            boolean flag = true;
            while (flag){
                if (i==30){
                    break;
                }
                i = i+1;
                if("prev".equals(type)){
                    if (sort<=1){
                        break;
                    }
                    sort = sort-1;
                    Article article = articleRepository.queryOtherArticle(bookid,sort);
                    if (article!=null){
                        id=article.getId().toString();
                        break;
                    }
                }else if ("next".equals(type)){
                    if (sort>=countArticle){
                        break;
                    }
                    sort = sort+1;
                    Article article = articleRepository.queryOtherArticle(bookid,sort);
                    if (article!=null){
                        id=article.getId().toString();
                        break;
                    }
                }
            }
        }
        return id;
    }
}
