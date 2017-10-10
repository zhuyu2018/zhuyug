package com.megayu.repository;

import com.megayu.entity.Article;
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
}
