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

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    @RequestMapping(value = "/openAddBook")
    public String openAddBook(HttpServletRequest request , HttpServletResponse response, Model model){
        return "addbook";
    }
    @ResponseBody
    @RequestMapping(value = "/delBook")
    public String delBook(HttpServletRequest request , HttpServletResponse response, Model model){
        try {
            LoginVo loginVo = LoginUtil.getLoginVo(request);
            String id=request.getParameter("id");
            Integer idI = Integer.valueOf(id);
            Book book = bookRepository.findByIdAndDelstatus(idI,1);
            if(book==null){
                return "书籍不存在";
            }
            if(book.getAuthor()!=null&&book.getAuthor().equals(loginVo.getId())){//是作者自己才能删除
                List<Article> articleList = articleRepository.findByBookid(book.getId());
                if(articleList!=null&&articleList.size()>0){
                    //有章节不允许删除
                    return "已有章节，不允许删除";
                }
                book.setDelstatus(0);
                bookRepository.save(book);
                return "success";
            }else{
                return "作者与操作人不一致，不允许删除";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/saveBook")
    public String saveBook(HttpServletRequest request , HttpServletResponse response, Model model){
        try {
            String bookname = request.getParameter("bookname");
            String remark = request.getParameter("remark");
            String publicstatus = request.getParameter("publicstatus");
            LoginVo loginVo = LoginUtil.getLoginVo(request);
            Integer author = loginVo.getId();
            String authorname = loginVo.getName();
            Integer createuser = loginVo.getId();
            Integer publicstatusi = Integer.valueOf(publicstatus);
            Book existBook = bookRepository.findByBookname(bookname);
            if(existBook!=null){
                return "该书籍已存在";
            }
            Book book = new Book();
            book.setBookname(bookname);
            book.setRemark(remark);
            book.setAuthor(author);
            book.setAuthorname(authorname);
            book.setCreateuser(createuser);
//            book.setPublicstatus(2);
            book.setCreatetime(new Date());
            book.setPublicstatus(publicstatusi);
            book.setDelstatus(1);
            Book resultbook = bookRepository.save(book);
            if(resultbook!=null && resultbook.getId()!=null&&resultbook.getId()>0){
                return "success";
            }else{
                return  "fail";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }



    }

    @ResponseBody
    @RequestMapping(value = "/queryBookPage")
    public String queryBookPage(HttpServletRequest request , HttpServletResponse response, Model model){
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        String bookname = request.getParameter("bookname");
        if(page==null){
            page=1;
        }
        if(rows==null||rows<0){
            rows= 10;
        }
        final String bname = bookname;
        final Integer userid = loginVo.getId();
        Specification<Book> sc = new Specification<Book>(){
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> bookname = root.get("bookname");
                Path<Integer> createuser = root.get("createuser");
                Path<Integer> delstatus = root.get("delstatus");
                query.where(cb.equal(createuser, userid));
                query.where(cb.equal(delstatus, 1));
                if(bname!=null&&!bname.equals("")){
                    query.where(cb.like(bookname, "%"+bname+"%"));
                }
                return null;
            }
        };

        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        Pageable pageable = new PageRequest(page-1, rows,sort);
        Page<Book> pages = bookRepository.findAll(sc,pageable);
        List<Book> bookList = pages.getContent();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if(bookList!=null && bookList.size()>0){
            for (Book book : bookList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("bookname",book.getBookname());
                map.put("remark",book.getRemark());
                map.put("authorname",book.getAuthorname());
                map.put("id",book.getId());
                if(book.getCreatetime()!=null){
                    map.put("createtime",DateUtil.editDate(book.getCreatetime()));
                }
                if(book.getPublicstatus()!=null){
                    if(book.getPublicstatus()==1){
                        map.put("publicmsg","是");
                    }else{
                        map.put("publicmsg","否");
                    }
                }
                List<Article> articleList = articleRepository.findByBookid(book.getId());
//                map.put("updatetime",DateUtil.editDate(book.getCreatetime()));
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
        Map map = new HashMap();
        map.put("page",page);
        map.put("rows",rows);
        map.put("totalCount",pages.getTotalElements());
        map.put("dataList",resultList);
        map.put("totalpages",pages.getTotalPages());
        return new Gson().toJson(map);
    }

    @RequestMapping(value = "/newBookArticle")
    public String newBookArticle(HttpServletRequest request , HttpServletResponse response, Model model){

        String bookid = request.getParameter("bookid");
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        Integer bid = Integer.valueOf(bookid);
        Book book = bookRepository.findByIdAndDelstatus(bid,1);
        if(book==null){
            return "error";
        }
        if(book.getAuthor()!=loginVo.getId()){
            return "error";
        }
        model.addAttribute("bookid",bookid);
        List<Article> articles = articleRepository.findByBookid(bid);
        if(articles!=null && articles.size()>0){
            model.addAttribute("countArticle",articles.size()+1);
        }else {
            model.addAttribute("countArticle",1);
        }
        return "newbookarticle";
    }
    @ResponseBody
    @RequestMapping(value = "/saveBookArticle")
    public String saveBookArticle(HttpServletRequest request , HttpServletResponse response, Model model){
        try {
            String bookid = request.getParameter("bookid");
            String articlename = request.getParameter("articlename");
            String articlesort = request.getParameter("articlesort");
            String articlecontent = request.getParameter("articlecontent");
            String publicstatus = request.getParameter("publicstatus");
            if (articlesort.indexOf("0")==0){
                return "数字格式异常，去除数首占位0";
            }
            Integer bid = Integer.valueOf(bookid);
            Integer as = Integer.valueOf(articlesort);
            Integer ps = Integer.valueOf(publicstatus);
            LoginVo loginVo = LoginUtil.getLoginVo(request);
            Book book = bookRepository.findByIdAndDelstatus(bid,1);
            if(book==null){
                return "待新增章节的书籍不存在";
            }
            if(book.getAuthor()!=loginVo.getId()){
                return "非本人书籍不能新增章节";
            }
            Article ar = articleRepository.findByArticlesortAndDelstatusAndBookid(as,1,bid);
            if (ar!=null){
                return "章节序号已存在";
            }
            Article article = new Article();
            article.setBookid(bid);
            article.setArticlecontent(articlecontent);
            article.setArticlename(articlename);
            article.setCreateuser(loginVo.getId());
            article.setAuthor(loginVo.getId());
            article.setAuthorname(loginVo.getLoginname());
            article.setArticlesort(as);
            article.setPublicstatus(ps);
            article.setArticletype(3);
            article.setCreatetime(new Date());
            Article articleresult = articleRepository.save(article);
            if(articleresult==null || articleresult.getId()==null || articleresult.getId()<=0){
                return "新增章节失败";
            }
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "操作异常";
        }

    }
}
