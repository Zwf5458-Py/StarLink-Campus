CREATE TABLE IF NOT EXISTS `kg_survey` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `type` VARCHAR(20) COMMENT '满意度/伙食/活动/教学',
    `target_scope` VARCHAR(20) COMMENT '全园/指定班级',
    `target_class_ids` JSON,
    `start_time` DATETIME,
    `end_time` DATETIME,
    `status` VARCHAR(10) DEFAULT '草稿',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`),
    INDEX `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷调查';

CREATE TABLE IF NOT EXISTS `kg_survey_question` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `survey_id` BIGINT NOT NULL,
    `question_text` TEXT NOT NULL,
    `question_type` VARCHAR(10) NOT NULL COMMENT '单选/多选/评分/文本',
    `options` JSON,
    `sort_order` INT DEFAULT 0,
    `required` TINYINT DEFAULT 1,
    INDEX `idx_survey` (`survey_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷题目';

CREATE TABLE IF NOT EXISTS `kg_survey_answer` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `survey_id` BIGINT NOT NULL,
    `respondent_id` BIGINT NOT NULL,
    `respondent_type` VARCHAR(10) COMMENT 'PARENT/STAFF',
    `answers` JSON NOT NULL,
    `submit_time` DATETIME,
    INDEX `idx_survey_respondent` (`survey_id`, `respondent_id`),
    UNIQUE KEY `uk_survey_respondent` (`survey_id`, `respondent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷答案';
