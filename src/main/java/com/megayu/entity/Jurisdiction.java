package com.megayu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "y_jurisdiction")
public class Jurisdiction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userid;

    private Integer bookmanage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getBookmanage() {
        return bookmanage;
    }

    public void setBookmanage(Integer bookmanage) {
        this.bookmanage = bookmanage;
    }
}
