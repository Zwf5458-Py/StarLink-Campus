package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.entity.KgArticleCategory;
import com.starlink.campus.module.kindergarten.entity.KgArticleComment;
import com.starlink.campus.module.kindergarten.service.KgArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "校园门户CMS管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/article")
@CrossOrigin
public class KgArticleController {

    @Autowired
    private KgArticleService articleService;

    @Operation(summary = "获取文章列表")
    @GetMapping("/list")
    public R<List<KgArticle>> list() {
        QueryWrapper<KgArticle> query = new QueryWrapper<>();
        query.orderByDesc("create_time");
        return R.ok(articleService.list(query));
    }

    @Operation(summary = "新增文章")
    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody KgArticle article) {
        article.setViews(0);
        article.setStatus("待审核");
        article.setSyncStatus("未同步");
        article.setCreateTime(LocalDateTime.now());
        return R.ok(articleService.save(article));
    }

    @Operation(summary = "更新文章")
    @PostMapping("/update")
    public R<Boolean> update(@Valid @RequestBody KgArticle article) {
        return R.ok(articleService.updateById(article));
    }

    @Operation(summary = "删除文章")
    @PostMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok(articleService.removeById(id));
    }

    @Operation(summary = "审核文章")
    @PostMapping("/approve/{id}")
    public R<Boolean> approve(@PathVariable Long id) {
        KgArticle article = articleService.getById(id);
        if (article != null && "待审核".equals(article.getStatus())) {
            article.setStatus("已发布");
            return R.ok(articleService.updateById(article));
        }
        return R.fail("文章不存在或状态不正确");
    }

    @Operation(summary = "同步文章到微信")
    @PostMapping("/sync-wechat/{id}")
    public R<Boolean> syncWechat(@PathVariable Long id) {
        KgArticle article = articleService.getById(id);
        if (article != null && "已发布".equals(article.getStatus())) {
            System.out.println("[Mock Wechat Sync] Successfully synced article to Wechat: " + article.getTitle());
            article.setSyncStatus("已同步");
            return R.ok(articleService.updateById(article));
        }
        return R.fail("文章不存在或尚未发布");
    }

    @Operation(summary = "获取分类列表")
    @GetMapping("/category/list")
    public R<List<KgArticleCategory>> listCategories() {
        return R.ok(articleService.listCategories());
    }

    @Operation(summary = "新增分类")
    @PostMapping("/category/add")
    public R<Boolean> addCategory(@Valid @RequestBody KgArticleCategory category) {
        return R.ok(articleService.addCategory(category));
    }

    @Operation(summary = "更新分类")
    @PutMapping("/category/update")
    public R<Boolean> updateCategory(@Valid @RequestBody KgArticleCategory category) {
        return R.ok(articleService.updateCategory(category));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/category/delete/{id}")
    public R<Boolean> deleteCategory(@PathVariable Long id) {
        return R.ok(articleService.deleteCategory(id));
    }

    @Operation(summary = "按分类获取文章")
    @GetMapping("/by-category")
    public R<List<KgArticle>> listByCategory(@RequestParam String category) {
        return R.ok(articleService.listByCategory(category));
    }

    @Operation(summary = "获取文章评论")
    @GetMapping("/comment/list/{articleId}")
    public R<List<KgArticleComment>> listComments(@PathVariable Long articleId) {
        return R.ok(articleService.listComments(articleId));
    }

    @Operation(summary = "新增评论")
    @PostMapping("/comment/add")
    public R<Boolean> addComment(@Valid @RequestBody KgArticleComment comment) {
        return R.ok(articleService.addComment(comment));
    }

    @Operation(summary = "审核评论")
    @PostMapping("/comment/approve/{id}")
    public R<Boolean> approveComment(@PathVariable Long id) {
        return R.ok(articleService.approveComment(id));
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/comment/delete/{id}")
    public R<Boolean> deleteComment(@PathVariable Long id) {
        return R.ok(articleService.deleteComment(id));
    }
}
