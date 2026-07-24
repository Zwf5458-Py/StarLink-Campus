-- 1. 喂药管理表
CREATE TABLE IF NOT EXISTS `kg_medication_application` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `parent_id` BIGINT NOT NULL COMMENT '家长ID',
  `medication_name` VARCHAR(128) NOT NULL COMMENT '药品名称',
  `dosage` VARCHAR(128) NOT NULL COMMENT '用药剂量',
  `time_to_take` VARCHAR(64) NOT NULL COMMENT '用药时间 (如 午饭后)',
  `notes` TEXT COMMENT '注意事项',
  `image_url` VARCHAR(255) COMMENT '药品图片留存',
  `status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态 (PENDING待确认, ACCEPTED已接收, COMPLETED已执行, REJECTED已拒绝)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_status` (`student_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='喂药申请表';

CREATE TABLE IF NOT EXISTS `kg_medication_execution` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `application_id` BIGINT NOT NULL COMMENT '申请表ID',
  `executor_id` BIGINT NOT NULL COMMENT '执行人(保健医/班主任)ID',
  `student_temperature` DECIMAL(4,1) COMMENT '执行时幼儿体温',
  `actual_dosage` VARCHAR(128) COMMENT '实际给药剂量',
  `execution_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `evidence_image_url` VARCHAR(255) COMMENT '执行拍照存证',
  `remarks` TEXT COMMENT '执行备注',
  PRIMARY KEY (`id`),
  KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='喂药执行记录表';

-- 2. 体检档案
CREATE TABLE IF NOT EXISTS `kg_physical_exam` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `exam_type` VARCHAR(32) NOT NULL COMMENT '体检类型 (入园体检, 学期常规体检)',
  `exam_date` DATE NOT NULL COMMENT '体检日期',
  `height` DECIMAL(5,1) COMMENT '身高(cm)',
  `weight` DECIMAL(5,1) COMMENT '体重(kg)',
  `eyesight_left` DECIMAL(3,1) COMMENT '左眼视力',
  `eyesight_right` DECIMAL(3,1) COMMENT '右眼视力',
  `dental_caries` INT DEFAULT 0 COMMENT '龋齿数',
  `hemoglobin` DECIMAL(5,1) COMMENT '血红蛋白(g/L)',
  `doctor_conclusion` TEXT COMMENT '医生综合评估与建议',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='幼儿体检档案表';

-- 3. 午睡记录
CREATE TABLE IF NOT EXISTS `kg_nap_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `record_date` DATE NOT NULL COMMENT '记录日期',
  `sleep_time` DATETIME COMMENT '入睡时间',
  `wake_time` DATETIME COMMENT '起床时间',
  `sleep_quality` VARCHAR(32) COMMENT '睡眠质量 (EXCELLENT优, GOOD良, LIGHT浅睡易醒, RESTLESS不安稳)',
  `teacher_remarks` TEXT COMMENT '带班教师备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_date` (`student_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='幼儿午睡记录表';

-- 4. 每日聚合报告 (Daily Report)
CREATE TABLE IF NOT EXISTS `kg_daily_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `report_date` DATE NOT NULL COMMENT '报告日期',
  `attendance_status` VARCHAR(32) COMMENT '考勤状态',
  `morning_temp` DECIMAL(4,1) COMMENT '晨检体温',
  `meal_summary` VARCHAR(128) COMMENT '用餐情况摘要',
  `nap_summary` VARCHAR(128) COMMENT '午睡摘要',
  `performance_highlights` TEXT COMMENT '今日表现亮点',
  `teacher_comments` TEXT COMMENT '教师寄语',
  `is_read_by_parent` TINYINT(1) DEFAULT 0 COMMENT '家长是否已读',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_date` (`student_id`, `report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家园共育每日报告表';

-- 5. 家长投诉与建议闭环
CREATE TABLE IF NOT EXISTS `kg_feedback_ticket` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工单主键',
  `parent_id` BIGINT NOT NULL COMMENT '发起家长ID',
  `ticket_type` VARCHAR(32) NOT NULL COMMENT '工单类型 (TEACHING教学, LOGISTICS后勤, SAFETY安全, SERVICE服务)',
  `title` VARCHAR(255) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '详细内容',
  `attachment_urls` TEXT COMMENT '附件(多图逗号分隔)',
  `status` VARCHAR(32) DEFAULT 'SUBMITTED' COMMENT '状态 (SUBMITTED已提交, PROCESSING处理中, RESOLVED已解决, CLOSED已关闭)',
  `handler_id` BIGINT COMMENT '处理人ID',
  `handling_process` TEXT COMMENT '处理进展与回执记录',
  `satisfaction_score` INT COMMENT '家长满意度打分 (1-5星)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长客诉建议工单表';
