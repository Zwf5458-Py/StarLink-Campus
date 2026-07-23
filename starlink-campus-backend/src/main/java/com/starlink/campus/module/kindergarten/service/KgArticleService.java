package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.entity.KgArticleCategory;
import com.starlink.campus.module.kindergarten.entity.KgArticleComment;

import java.util.List;

public interface KgArticleService extends IService<KgArticle> {
    List<KgArticleCategory> listCategories();
    boolean addCategory(KgArticleCategory category);
    boolean updateCategory(KgArticleCategory category);
    boolean deleteCategory(Long id);
    
    List<KgArticle> listByCategory(String category);
    
    List<KgArticleComment> listComments(Long articleId);
    boolean addComment(KgArticleComment comment);
    boolean approveComment(Long commentId);
    boolean deleteComment(Long commentId);
}
