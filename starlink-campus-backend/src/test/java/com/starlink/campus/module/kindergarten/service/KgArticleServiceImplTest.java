package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.entity.KgArticleCategory;
import com.starlink.campus.module.kindergarten.entity.KgArticleComment;
import com.starlink.campus.module.kindergarten.mapper.KgArticleCategoryMapper;
import com.starlink.campus.module.kindergarten.mapper.KgArticleCommentMapper;
import com.starlink.campus.module.kindergarten.mapper.KgArticleMapper;
import com.starlink.campus.module.kindergarten.service.impl.KgArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KgArticleServiceImplTest {

    @Mock
    private KgArticleMapper articleMapper;

    @Mock
    private KgArticleCategoryMapper categoryMapper;

    @Mock
    private KgArticleCommentMapper commentMapper;

    @InjectMocks
    private KgArticleServiceImpl articleService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(articleService, "baseMapper", articleMapper);
    }

    @Test
    public void testArticlePublishFlow_MockSave() {
        // 测试文章发布流程 - 由于目前Service没重写发布方法，这里我们仅验证 listByCategory
        KgArticle article = new KgArticle();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setCategory("新闻");
        
        when(articleMapper.selectList(any())).thenReturn(Arrays.asList(article));
        
        List<KgArticle> list = articleService.listByCategory("新闻");
        
        assertEquals(1, list.size());
        assertEquals("Test Article", list.get(0).getTitle());
    }

    @Test
    public void testCommentApproveFlow_PendingToApproved() {
        // 测试评论审核流程 (待审核 -> 已审核)
        KgArticleComment comment = new KgArticleComment();
        comment.setId(1L);
        comment.setStatus("待审核");
        
        when(commentMapper.selectById(1L)).thenReturn(comment);
        when(commentMapper.updateById(comment)).thenReturn(1);
        
        boolean result = articleService.approveComment(1L);
        
        assertTrue(result);
        assertEquals("已审核", comment.getStatus());
        verify(commentMapper, times(1)).updateById(comment);
    }
    
    @Test
    public void testCategoryCRUD_AddCategory() {
        // 测试分类增加
        KgArticleCategory category = new KgArticleCategory();
        category.setCategoryName("新闻");
        
        when(categoryMapper.insert(category)).thenReturn(1);
        
        boolean result = articleService.addCategory(category);
        
        assertTrue(result);
        assertNotNull(category.getCreateTime());
        verify(categoryMapper, times(1)).insert(category);
    }

    @Test
    public void testCategoryCRUD_UpdateCategory() {
        // 测试分类更新
        KgArticleCategory category = new KgArticleCategory();
        category.setId(1L);
        category.setCategoryName("通知");
        
        when(categoryMapper.updateById(category)).thenReturn(1);
        
        boolean result = articleService.updateCategory(category);
        
        assertTrue(result);
        verify(categoryMapper, times(1)).updateById(category);
    }

    @Test
    public void testCategoryCRUD_DeleteCategory() {
        // 测试分类删除
        when(categoryMapper.deleteById(1L)).thenReturn(1);
        
        boolean result = articleService.deleteCategory(1L);
        
        assertTrue(result);
        verify(categoryMapper, times(1)).deleteById(1L);
    }
}
