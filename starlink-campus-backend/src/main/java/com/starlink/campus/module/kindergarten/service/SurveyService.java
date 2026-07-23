package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgSurvey;
import com.starlink.campus.module.kindergarten.entity.KgSurveyAnswer;
import com.starlink.campus.module.kindergarten.entity.KgSurveyQuestion;

import java.util.List;
import java.util.Map;

public interface SurveyService extends IService<KgSurvey> {
    IPage<KgSurvey> listSurveys(int pageNum, int pageSize);
    boolean addSurvey(KgSurvey survey);
    boolean updateSurvey(KgSurvey survey);
    boolean deleteSurvey(Long id);
    boolean publishSurvey(Long id);
    boolean closeSurvey(Long id);
    
    List<KgSurveyQuestion> listQuestions(Long surveyId);
    boolean addQuestion(KgSurveyQuestion question);
    boolean updateQuestion(KgSurveyQuestion question);
    boolean deleteQuestion(Long id);
    
    boolean submitAnswer(KgSurveyAnswer answer);
    Map<String, Object> getSurveyStats(Long surveyId);
}
