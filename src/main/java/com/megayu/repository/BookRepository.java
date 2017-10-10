package com.megayu.repository;

import com.megayu.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> ,JpaSpecificationExecutor {
    @Query(value = "select * from y_book where createuser=?1 limit ?2 ", nativeQuery = true)
    @Modifying
    List<Book> queryBookLimit(Integer userid,int limit);
}
