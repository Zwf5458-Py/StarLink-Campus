package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.entity.KgArticleCategory;
import com.starlink.campus.module.kindergarten.entity.KgArticleComment;
import com.starlink.campus.module.kindergarten.mapper.KgArticleMapper;
import com.starlink.campus.module.kindergarten.mapper.KgArticleCategoryMapper;
import com.starlink.campus.module.kindergarten.mapper.KgArticleCommentMapper;
import com.starlink.campus.module.kindergarten.service.KgArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class KgArticleServiceImpl extends ServiceImpl<KgArticleMapper, KgArticle> implements KgArticleService {

    @Autowired
    private KgArticleCategoryMapper categoryMapper;
    
    @Autowired
    private KgArticleCommentMapper commentMapper;

    @Override
    public List<KgArticleCategory> listCategories() {
        return categoryMapper.selectList(new QueryWrapper<KgArticleCategory>().orderByAsc("sort_order"));
    }

    @Override
    public boolean addCategory(KgArticleCategory category) {
        if (category.getCreateTime() == null) {
            category.setCreateTime(LocalDateTime.now());
        }
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean updateCategory(KgArticleCategory category) {
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    public boolean deleteCategory(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public List<KgArticle> listByCategory(String category) {
        return this.list(new QueryWrapper<KgArticle>().eq("category", category).orderByDesc("create_time"));
    }

    @Override
    public List<KgArticleComment> listComments(Long articleId) {
        return commentMapper.selectList(new QueryWrapper<KgArticleComment>()
                .eq("article_id", articleId)
                .orderByDesc("create_time"));
    }

    @Override
    public boolean addComment(KgArticleComment comment) {
        if (comment.getCreateTime() == null) {
            comment.setCreateTime(LocalDateTime.now());
        }
        if (comment.getStatus() == null) {
            comment.setStatus("待审核");
        }
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public boolean approveComment(Long commentId) {
        KgArticleComment comment = commentMapper.selectById(commentId);
        if (comment != null) {
            comment.setStatus("已审核");
            return commentMapper.updateById(comment) > 0;
        }
        return false;
    }

    @Override
    public boolean deleteComment(Long commentId) {
        return commentMapper.deleteById(commentId) > 0;
    }
}
