package com.megayu.controller;

import com.google.gson.Gson;
import com.megayu.entity.Userbackup;
import com.megayu.repository.ArticleRepository;
import com.megayu.repository.BookRepository;
import com.megayu.repository.UserbackupRepository;
import com.megayu.util.DateUtil;
import com.megayu.util.LoginUtil;
import com.megayu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value = "/login/backupArticle")
public class BackupArticleController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserbackupRepository userbackupRepository;
    @ResponseBody
    @RequestMapping(value = "/getBackupArticle")
    public String getBackupArticle(HttpServletRequest request , HttpServletResponse response, Model model){
        String type = request.getParameter("type");
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        if (type==null || type.equals("")){
            return "fail";
        }
        if (type.equals("1")){
            //普通文章备份
        }else {
            //书籍章节备份
            String bookid = request.getParameter("bookid");
            Userbackup userbackup = userbackupRepository.findByBookidAndCreateuserAndDelstatus(Integer.valueOf(bookid),loginVo.getId(),1);
            if(userbackup!=null){
                Gson gson = new Gson();
                return gson.toJson(userbackup);
            }
        }
        return "";
    }
    @ResponseBody
    @RequestMapping(value = "/saveBackupArticle")
    public String saveBackupArticle(HttpServletRequest request , HttpServletResponse response, Model model){
        String type = request.getParameter("type");
        LoginVo loginVo = LoginUtil.getLoginVo(request);
        String bookid = request.getParameter("bookid");
        String articlename = request.getParameter("articlename");
        String articlecontent = request.getParameter("articlecontent");
        Date date = new Date();
        if(articlecontent!=null && !"".equals(articlecontent)&& !"undefined".equals(articlecontent)){
            Userbackup userbackup = userbackupRepository.findByBookidAndCreateuserAndDelstatus(Integer.valueOf(bookid),loginVo.getId(),1);

            if(userbackup!=null){
                //更新
                userbackup.setArticlename(articlename);
                userbackup.setArticlecontent(articlecontent);
                userbackup.setUpdatetime(date);
                userbackupRepository.save(userbackup);
            }else {
                //新增
                Userbackup newuserbackup = new Userbackup();
                newuserbackup.setBookid(Integer.valueOf(bookid));
                newuserbackup.setArticlecontent(articlecontent);
                newuserbackup.setArticlename(articlename);
                newuserbackup.setCreatetime(date);
                newuserbackup.setCreateuser(loginVo.getId());
                newuserbackup.setType(Integer.valueOf(type));
                newuserbackup.setDelstatus(1);
                userbackupRepository.save(newuserbackup);
            }
            return DateUtil.getHMS(date);
        }
        return "0";
    }
}
