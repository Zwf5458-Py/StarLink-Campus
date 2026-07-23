package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgSurveyAnswer;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface KgSurveyAnswerMapper extends BaseMapper<KgSurveyAnswer> {

    @Select("SELECT JSON_UNQUOTE(JSON_EXTRACT(answers, CONCAT('$.\"', #{questionId}, '\"'))) AS option_value, COUNT(*) as cnt " +
            "FROM kg_survey_answer WHERE survey_id = #{surveyId} " +
            "AND JSON_EXTRACT(answers, CONCAT('$.\"', #{questionId}, '\"')) IS NOT NULL " +
            "GROUP BY option_value")
    List<Map<String, Object>> countSingleChoiceStats(@Param("surveyId") Long surveyId, @Param("questionId") Long questionId);

    @Select("SELECT JSON_EXTRACT(answers, CONCAT('$.\"', #{questionId}, '\"')) AS option_arr " +
            "FROM kg_survey_answer WHERE survey_id = #{surveyId} " +
            "AND JSON_EXTRACT(answers, CONCAT('$.\"', #{questionId}, '\"')) IS NOT NULL")
    List<String> getMultiChoiceAnswers(@Param("surveyId") Long surveyId, @Param("questionId") Long questionId);
}
