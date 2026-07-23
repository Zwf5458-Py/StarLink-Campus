package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgSurvey;
import com.starlink.campus.module.kindergarten.entity.KgSurveyAnswer;
import com.starlink.campus.module.kindergarten.entity.KgSurveyQuestion;
import com.starlink.campus.module.kindergarten.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SaCheckLogin
@RestController
@CrossOrigin
@RequestMapping("/kindergarten/survey")
@Tag(name = "问卷调查与满意度")
public class SurveyController {

    private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/list")
    @Operation(summary = "获取问卷列表")
    public R<IPage<KgSurvey>> listSurveys(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(surveyService.listSurveys(pageNum, pageSize));
    }

    @PostMapping("/add")
    @Operation(summary = "新增问卷")
    public R<String> addSurvey(@RequestBody KgSurvey survey) {
        if (surveyService.addSurvey(survey)) {
            return R.ok("新增问卷成功");
        }
        return R.fail("新增问卷失败");
    }

    @PostMapping("/update")
    @Operation(summary = "修改问卷")
    public R<String> updateSurvey(@RequestBody KgSurvey survey) {
        if (surveyService.updateSurvey(survey)) {
            return R.ok("修改问卷成功");
        }
        return R.fail("修改问卷失败");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除问卷")
    public R<String> deleteSurvey(@PathVariable Long id) {
        if (surveyService.deleteSurvey(id)) {
            return R.ok("删除问卷成功");
        }
        return R.fail("删除问卷失败");
    }

    @PostMapping("/publish/{id}")
    @Operation(summary = "发布问卷")
    public R<String> publishSurvey(@PathVariable Long id) {
        if (surveyService.publishSurvey(id)) {
            return R.ok("发布问卷成功");
        }
        return R.fail("发布问卷失败");
    }

    @PostMapping("/close/{id}")
    @Operation(summary = "结束问卷")
    public R<String> closeSurvey(@PathVariable Long id) {
        if (surveyService.closeSurvey(id)) {
            return R.ok("结束问卷成功");
        }
        return R.fail("结束问卷失败");
    }

    @GetMapping("/question/list")
    @Operation(summary = "获取题目列表")
    public R<List<KgSurveyQuestion>> listQuestions(@RequestParam Long surveyId) {
        return R.ok(surveyService.listQuestions(surveyId));
    }

    @PostMapping("/question/add")
    @Operation(summary = "新增题目")
    public R<String> addQuestion(@RequestBody KgSurveyQuestion question) {
        if (surveyService.addQuestion(question)) {
            return R.ok("新增题目成功");
        }
        return R.fail("新增题目失败");
    }

    @PostMapping("/question/update")
    @Operation(summary = "修改题目")
    public R<String> updateQuestion(@RequestBody KgSurveyQuestion question) {
        if (surveyService.updateQuestion(question)) {
            return R.ok("修改题目成功");
        }
        return R.fail("修改题目失败");
    }

    @DeleteMapping("/question/delete/{id}")
    @Operation(summary = "删除题目")
    public R<String> deleteQuestion(@PathVariable Long id) {
        if (surveyService.deleteQuestion(id)) {
            return R.ok("删除题目成功");
        }
        return R.fail("删除题目失败");
    }

    @PostMapping("/answer/submit")
    @Operation(summary = "提交问卷答案")
    public R<String> submitAnswer(@RequestBody KgSurveyAnswer answer) {
        if (surveyService.submitAnswer(answer)) {
            return R.ok("提交答案成功");
        }
        return R.fail("提交答案失败或重复提交");
    }

    @GetMapping("/stats")
    @Operation(summary = "获取问卷统计结果")
    public R<Map<String, Object>> getSurveyStats(@RequestParam Long surveyId) {
        return R.ok(surveyService.getSurveyStats(surveyId));
    }
}
