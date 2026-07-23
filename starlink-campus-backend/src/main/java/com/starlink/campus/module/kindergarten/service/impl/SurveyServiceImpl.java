package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgSurvey;
import com.starlink.campus.module.kindergarten.entity.KgSurveyAnswer;
import com.starlink.campus.module.kindergarten.entity.KgSurveyQuestion;
import com.starlink.campus.module.kindergarten.mapper.KgSurveyAnswerMapper;
import com.starlink.campus.module.kindergarten.mapper.KgSurveyMapper;
import com.starlink.campus.module.kindergarten.mapper.KgSurveyQuestionMapper;
import com.starlink.campus.module.kindergarten.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveyServiceImpl extends ServiceImpl<KgSurveyMapper, KgSurvey> implements SurveyService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Autowired
    private KgSurveyMapper surveyMapper;
    
    @Autowired
    private KgSurveyQuestionMapper questionMapper;
    
    @Autowired
    private KgSurveyAnswerMapper answerMapper;

    @Override
    public IPage<KgSurvey> listSurveys(int pageNum, int pageSize) {
        Page<KgSurvey> page = new Page<>(pageNum, pageSize);
        return surveyMapper.selectPage(page, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSurvey(KgSurvey survey) {
        survey.setCreateTime(LocalDateTime.now());
        survey.setStatus("草稿");
        return surveyMapper.insert(survey) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSurvey(KgSurvey survey) {
        return surveyMapper.updateById(survey) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSurvey(Long id) {
        return surveyMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishSurvey(Long id) {
        KgSurvey survey = surveyMapper.selectById(id);
        if (survey != null && "草稿".equals(survey.getStatus())) {
            survey.setStatus("进行中");
            return surveyMapper.updateById(survey) > 0;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeSurvey(Long id) {
        KgSurvey survey = surveyMapper.selectById(id);
        if (survey != null) {
            survey.setStatus("已结束");
            return surveyMapper.updateById(survey) > 0;
        }
        return false;
    }

    @Override
    public List<KgSurveyQuestion> listQuestions(Long surveyId) {
        QueryWrapper<KgSurveyQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("survey_id", surveyId).orderByAsc("sort_order");
        return questionMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuestion(KgSurveyQuestion question) {
        return questionMapper.insert(question) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestion(KgSurveyQuestion question) {
        return questionMapper.updateById(question) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(Long id) {
        return questionMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitAnswer(KgSurveyAnswer answer) {
        QueryWrapper<KgSurveyAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("survey_id", answer.getSurveyId())
                    .eq("respondent_id", answer.getRespondentId());
        if (answerMapper.selectCount(queryWrapper) > 0) {
            logger.warn("User {} already submitted answer for survey {}", answer.getRespondentId(), answer.getSurveyId());
            return false;
        }
        answer.setSubmitTime(LocalDateTime.now());
        return answerMapper.insert(answer) > 0;
    }

    @Override
    public Map<String, Object> getSurveyStats(Long surveyId) {
        // 获取所有题目
        QueryWrapper<KgSurveyQuestion> qWrapper = new QueryWrapper<>();
        qWrapper.eq("survey_id", surveyId).orderByAsc("sort_order");
        List<KgSurveyQuestion> questions = questionMapper.selectList(qWrapper);

        // 避免全表加载 KgSurveyAnswer
        // 计算答题人数（通过 SQL count 去重计算 respondent_id）
        long totalRespondents = answerMapper.selectCount(new QueryWrapper<KgSurveyAnswer>().eq("survey_id", surveyId));
        
        List<Map<String, Object>> questionStats = new java.util.ArrayList<>();
        
        for (KgSurveyQuestion q : questions) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("questionId", q.getId());
            stat.put("questionText", q.getQuestionText());
            stat.put("questionType", q.getQuestionType());
            
            if ("SINGLE_CHOICE".equals(q.getQuestionType()) || "MULTIPLE_CHOICE".equals(q.getQuestionType())) {
                Map<String, Integer> optionCounts = new HashMap<>();
                if (q.getOptions() != null) {
                    try {
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        List<String> options = mapper.readValue(q.getOptions(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
                        for (String opt : options) {
                            optionCounts.put(opt, 0);
                        }
                    } catch (Exception e) {
                        logger.warn("Parse options failed for question " + q.getId());
                    }
                }
                
                if ("SINGLE_CHOICE".equals(q.getQuestionType())) {
                    List<Map<String, Object>> singleStats = answerMapper.countSingleChoiceStats(surveyId, q.getId());
                    for (Map<String, Object> row : singleStats) {
                        String optValue = (String) row.get("option_value");
                        Number cnt = (Number) row.get("cnt");
                        if (optValue != null && optionCounts.containsKey(optValue)) {
                            optionCounts.put(optValue, cnt.intValue());
                        }
                    }
                } else if ("MULTIPLE_CHOICE".equals(q.getQuestionType())) {
                    List<String> multiStats = answerMapper.getMultiChoiceAnswers(surveyId, q.getId());
                    for (String mAns : multiStats) {
                        if (mAns != null && !mAns.trim().isEmpty() && !mAns.equals("null")) {
                            try {
                                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                List<String> uList = mapper.readValue(mAns, new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
                                for (String uItem : uList) {
                                    if (optionCounts.containsKey(uItem)) {
                                        optionCounts.put(uItem, optionCounts.get(uItem) + 1);
                                    }
                                }
                            } catch (Exception e) {
                                // ignore parse error for individual multi choice answer
                            }
                        }
                    }
                }
                
                stat.put("optionStats", optionCounts);
            } else {
                // 文本题等其他类型，暂时只记录回答总数，不加载文本以防 OOM
                stat.put("answerCount", totalRespondents);
            }
            questionStats.add(stat);
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRespondents", totalRespondents);
        stats.put("questionStats", questionStats);
        
        return stats;
    }
}
