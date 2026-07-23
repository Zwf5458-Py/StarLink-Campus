CREATE TABLE IF NOT EXISTS `kg_weekly_plan` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `class_id` BIGINT NOT NULL COMMENT '班级ID',
    `week_start_date` DATE NOT NULL COMMENT '周起始日期',
    `theme` VARCHAR(100) COMMENT '本周主题',
    `objectives` TEXT COMMENT '教学目标',
    `activities` JSON COMMENT '活动安排',
    `outdoor_plan` TEXT COMMENT '户外安排',
    `parent_cooperation` TEXT COMMENT '家长配合事项',
    `publish_status` VARCHAR(10) DEFAULT '草稿',
    `created_by` BIGINT COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_class_week` (`class_id`, `week_start_date`),
    INDEX `idx_status` (`publish_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学周计划';
