package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgSurveyQuestion;
import com.starlink.campus.module.kindergarten.mapper.KgSurveyAnswerMapper;
import com.starlink.campus.module.kindergarten.mapper.KgSurveyQuestionMapper;
import com.starlink.campus.module.kindergarten.service.impl.SurveyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceImplTest {

    @Mock
    private KgSurveyQuestionMapper questionMapper;

    @Mock
    private KgSurveyAnswerMapper answerMapper;

    @InjectMocks
    private SurveyServiceImpl surveyService;

    @Test
    public void testGetSurveyStats() {
        Long surveyId = 1L;

        KgSurveyQuestion q1 = new KgSurveyQuestion();
        q1.setId(10L);
        q1.setSurveyId(surveyId);
        q1.setQuestionType("SINGLE_CHOICE");
        q1.setOptions("[\"A\", \"B\"]");

        KgSurveyQuestion q2 = new KgSurveyQuestion();
        q2.setId(11L);
        q2.setSurveyId(surveyId);
        q2.setQuestionType("TEXT");

        when(questionMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(q1, q2));
        when(answerMapper.selectCount(any(QueryWrapper.class))).thenReturn(5L);

        List<Map<String, Object>> mockSingleStats = new ArrayList<>();
        Map<String, Object> stat1 = new HashMap<>();
        stat1.put("option_value", "A");
        stat1.put("cnt", 3);
        mockSingleStats.add(stat1);
        when(answerMapper.countSingleChoiceStats(eq(surveyId), eq(q1.getId()))).thenReturn(mockSingleStats);

        Map<String, Object> result = surveyService.getSurveyStats(surveyId);

        assertNotNull(result);
        assertEquals(5L, result.get("totalRespondents"));
        
        List<Map<String, Object>> qStats = (List<Map<String, Object>>) result.get("questionStats");
        assertEquals(2, qStats.size());

        Map<String, Object> q1Stat = qStats.get(0);
        assertEquals(10L, q1Stat.get("questionId"));
        Map<String, Integer> optionStats = (Map<String, Integer>) q1Stat.get("optionStats");
        assertEquals(3, optionStats.get("A"));
        assertEquals(0, optionStats.get("B"));

        Map<String, Object> q2Stat = qStats.get(1);
        assertEquals(11L, q2Stat.get("questionId"));
        assertEquals(5L, q2Stat.get("answerCount"));
    }
}
