package com.megayu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "y_article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String articlename;
    private Integer    articletype;
    private String   articlecontent;
    private String      remark;
    private String    articleediter;
    private Integer       bookid;
    private Date createtime;
    private Date       edittime;
    private Integer    createuser;
    private Integer       edituser;
    private Integer    author;
    private String        authorname;
    private Integer publicstatus;

    private Integer articlesort;

    private Integer delstatus;

    public Integer getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(Integer delstatus) {
        this.delstatus = delstatus;
    }

    private String time1;
    private String time2;

    public Integer getArticlesort() {
        return articlesort;
    }

    public void setArticlesort(Integer articlesort) {
        this.articlesort = articlesort;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public Integer getPublicstatus() {
        return publicstatus;
    }

    public void setPublicstatus(Integer publicstatus) {
        this.publicstatus = publicstatus;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public Integer getArticletype() {
        return articletype;
    }

    public void setArticletype(Integer articletype) {
        this.articletype = articletype;
    }

    public String getArticlecontent() {
        return articlecontent;
    }

    public void setArticlecontent(String articlecontent) {
        this.articlecontent = articlecontent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getArticleediter() {
        return articleediter;
    }

    public void setArticleediter(String articleediter) {
        this.articleediter = articleediter;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getEdittime() {
        return edittime;
    }

    public void setEdittime(Date edittime) {
        this.edittime = edittime;
    }

    public Integer getCreateuser() {
        return createuser;
    }

    public void setCreateuser(Integer createuser) {
        this.createuser = createuser;
    }

    public Integer getEdituser() {
        return edituser;
    }

    public void setEdituser(Integer edituser) {
        this.edituser = edituser;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }
}
