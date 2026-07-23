-- 学生成长档案表
CREATE TABLE IF NOT EXISTS `kg_growth_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `category` VARCHAR(20) NOT NULL COMMENT '类别: 身高/体重/语言/社交/运动/艺术',
    `value` VARCHAR(200) COMMENT '值',
    `unit` VARCHAR(20) COMMENT '单位',
    `photo_url` VARCHAR(500) COMMENT '照片URL',
    `teacher_comment` TEXT COMMENT '教师评语',
    `semester` VARCHAR(20) COMMENT '学期: 2026春/2026秋',
    `recorded_by` BIGINT COMMENT '记录人 staffId',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_student_date` (`student_id`, `record_date`),
    INDEX `idx_student_category` (`student_id`, `category`),
    INDEX `idx_semester` (`semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成长档案';
