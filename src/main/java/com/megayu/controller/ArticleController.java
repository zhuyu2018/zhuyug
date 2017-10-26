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

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    @Autowired
    private EntityManager entity;
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
        String page = request.getParameter("page");
        int sizei = 50;
        int pagei = (Integer.valueOf(page)-1)*sizei;
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        List<Article> articles = queryArticleAuto(loginVo.getId(),null,1,pagei,sizei);
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
        List<Article> articles = articleRepository.queryArticleLimit(userid,type,50);
        if(articles!=null && articles.size()>0){
            for(Article article : articles){
                article.setTime1(DateUtil.editDate(article.getCreatetime()));
            }
        }
        return articles;
    }

    public List<Article> queryArticleAuto(Integer userid,String articlename,Integer type,int page,int size){
        StringBuffer sql = new StringBuffer("");
        sql.append(" select id,article,articlecontent,author,authorname,createtime,edittime,createuser,edituser,publicstatus,delstatus,time1,time2 from y_article where 1=1 ");
        sql.append(" and (createuser="+userid+" or publicstatus=1) and delstatus=1 ");
        if(articlename!=null && !"".equals(articlename) && !"undefined".equals(articlename)){
            sql.append(" and articlename like '%"+articlename+"%' ");
        }
        if(type!=null){
            sql.append(" and articletype = "+type);
        }
        sql.append(" order by id desc limit "+page+","+size+" ");
        Query query = entity.createNativeQuery(sql.toString(), Article.class);
        List<Article> articles = query.getResultList();
        return  articles;
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
            Sort sort = new Sort(Sort.Direction.ASC, "articlesort");
            Pageable pageable = new PageRequest(pagei, sizei,sort);
            pages = articleRepository.findByBookid(bid,pageable);
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
        List<Article> articleList = articleRepository.findByBookidOrderByArticlesortAsc(bid);
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
        model.addAttribute("zhangxu","第"+numberToChineseNumber(article.getArticlesort().toString())+"章");
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
            countArticle = articleRepository.findMaxSortByBookid(bookid).getArticlesort();
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

    public String numberToChineseNumber(String numstr){
        String[] chineseNumb = {"零","一","二","三","四","五","六","七","八","九","十"};
        if(numstr.length()==1){
            return chineseNumb[Integer.valueOf(numstr)];
        }else if(numstr.length()==2){
            String[] numArr = numstr.split("");
            String lastNum = "";
            if(numArr[1]!="0"){
                lastNum =chineseNumb[Integer.valueOf(numArr[1])];
            }
            return chineseNumb[Integer.valueOf(numArr[0])]+"十"+lastNum;
        }else if (numstr.length()==3){
            String[] numArr = numstr.split("");
            String lastNum = "";
            if(numArr[2]!="0"){
                lastNum =chineseNumb[Integer.valueOf(numArr[2])];
            }
            String msg = "";
            msg = msg + chineseNumb[Integer.valueOf(numArr[0])]+"百";
            if(numArr[1]=="0"&&numArr[2]=="0"){
                return msg;
            }else if(numArr[1]=="0"&&numArr[2]!="0"){
                return msg + chineseNumb[Integer.valueOf(numArr[2])];
            }else if(numArr[1]!="0"&&numArr[2]!="0"){
                return msg +chineseNumb[Integer.valueOf(numArr[1])] + "十" +chineseNumb[Integer.valueOf(numArr[2])];
            }else if(numArr[1]!="0"&&numArr[2]=="0"){
                return msg +chineseNumb[Integer.valueOf(numArr[1])] + "十";
            }
        }else if (numstr.length()==4){
            String[] numArr = numstr.split("");
            String msg = "";
            msg = msg + chineseNumb[Integer.valueOf(numArr[0])]+"千";
            if(numArr[1]!="0"&&numArr[2]!="0"&&numArr[3]!="0"){
                return msg + chineseNumb[Integer.valueOf(numArr[1])]+"百"+chineseNumb[Integer.valueOf(numArr[2])]+"十"+chineseNumb[Integer.valueOf(numArr[3])];
            }else if (numArr[1]!="0"&&numArr[2]=="0"&&numArr[3]!="0"){
                return msg + chineseNumb[Integer.valueOf(numArr[1])]+"百零"+chineseNumb[Integer.valueOf(numArr[3])];
            }else if (numArr[1]=="0"&&numArr[2]=="0"&&numArr[3]!="0"){
                return msg + "零"+chineseNumb[Integer.valueOf(numArr[3])];
            }else if (numArr[1]=="0"&&numArr[2]!="0"&&numArr[3]!="0"){
                return msg + "零"+chineseNumb[Integer.valueOf(numArr[2])]+"十"+chineseNumb[Integer.valueOf(numArr[3])];
            }else if(numArr[1]!="0"&&numArr[2]!="0"&&numArr[3]=="0"){
                return msg + chineseNumb[Integer.valueOf(numArr[1])]+"百"+chineseNumb[Integer.valueOf(numArr[2])]+"十";
            }else if (numArr[1]!="0"&&numArr[2]=="0"&&numArr[3]=="0"){
                return msg + chineseNumb[Integer.valueOf(numArr[1])]+"百";
            }else if (numArr[1]=="0"&&numArr[2]=="0"&&numArr[3]=="0"){
                return msg;
            }else if (numArr[1]=="0"&&numArr[2]!="0"&&numArr[3]=="0"){
                return msg + "零"+chineseNumb[Integer.valueOf(numArr[2])]+"十";
            }
        }
        return "";
    }
}
