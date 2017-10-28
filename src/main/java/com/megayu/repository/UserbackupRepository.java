package com.megayu.repository;

import com.megayu.entity.Userbackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserbackupRepository  extends JpaRepository<Userbackup,Long>,JpaSpecificationExecutor {


    Userbackup findByBookidAndCreateuserAndDelstatus(Integer bookid,Integer userid,Integer delstatus);
}
