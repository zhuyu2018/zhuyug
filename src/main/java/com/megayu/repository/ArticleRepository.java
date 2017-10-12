package com.megayu.repository;

import com.megayu.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> ,JpaSpecificationExecutor {
    @Query(value = "select * from y_article where createuser=?1 and articletype=?2 limit ?3 ", nativeQuery = true)
    @Modifying
    List<Article> queryArticleLimit(Integer userid,int type, int limit);

    Page<Article> findByBookid(Integer bookid, Pageable pageable);
    @Query(value = "select * from y_article where bookid=?1 order by id asc ", nativeQuery = true)
    @Modifying
    List<Article> findByBookid(Integer bookid);
}
