package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

@TableName("kg_survey_question")
public class KgSurveyQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long surveyId;
    private String questionText;
    private String questionType;  // 单选/多选/评分/文本
    private String options;       // JSON
    private Integer sortOrder;
    private Integer required;     // 1必填/0选填

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Integer getRequired() { return required; }
    public void setRequired(Integer required) { this.required = required; }
}
