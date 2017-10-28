package com.megayu.repository;


import com.megayu.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> ,JpaSpecificationExecutor<Book> {
    @Query(value = "select * from y_book where (createuser=?1 or publicstatus = 1) and delstatus = 1 limit ?2 ", nativeQuery = true)
    @Modifying
    List<Book> queryBookLimit(Integer userid,int limit);



//    @Query(value = "select * from y_book where createuser=?1 order by id desc ", nativeQuery = true)
//    @Modifying
//    List<Book> findByUser(Integer userid);

    Page<Book> findByCreateuserOrPublicstatus(Integer createuser, int publicstatus, Pageable pageable);
    @Query(value = "select * from y_book where (createuser=?1 or publicstatus=?2) and delstatus=1  order by id desc limit ?3,?4  ", nativeQuery = true)
    @Modifying
    List<Book> queryBookBy30List(Integer userid,String bookname,Integer publicstatus,int page,int size);

    Page<Book> findByCreateuser(Integer createuser, Pageable pageable);


    Book findById(Integer id);
    Book findByIdAndDelstatus(Integer id,Integer delstatus);

    Book findByBooknameAndDelstatus(String bookname,Integer delstatus);
}
