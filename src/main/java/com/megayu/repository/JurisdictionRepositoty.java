package com.megayu.repository;

import com.megayu.entity.Jurisdiction;
import com.megayu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JurisdictionRepositoty extends JpaRepository<Jurisdiction,Long> {

    Jurisdiction findByUserid(Integer userid);
}