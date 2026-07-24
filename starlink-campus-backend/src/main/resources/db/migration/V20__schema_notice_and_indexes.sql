-- 1. 创建 kg_notice 表
CREATE TABLE IF NOT EXISTS `kg_notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` VARCHAR(32) DEFAULT NULL COMMENT '消息类型 (danger, warning, info)',
  `type_text` VARCHAR(64) DEFAULT NULL COMMENT '消息类型文本 (如: 🩺 晨检发热预警)',
  `title` VARCHAR(128) NOT NULL COMMENT '标题',
  `content` TEXT DEFAULT NULL COMMENT '正文内容',
  `target_role` VARCHAR(64) DEFAULT NULL COMMENT '接收角色，为空表示全员',
  `is_read` TINYINT DEFAULT 0 COMMENT '0-未读, 1-已读',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- 2. 创建 kg_article 表 (微官网文章)
CREATE TABLE IF NOT EXISTS `kg_article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `author` VARCHAR(64) DEFAULT NULL,
  `content` LONGTEXT,
  `category` VARCHAR(64) DEFAULT NULL,
  `views` INT DEFAULT 0,
  `status` VARCHAR(32) DEFAULT 'PUBLISHED',
  `sync_status` VARCHAR(32) DEFAULT 'UNSYNCED',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微官网文章表';

-- 3. 创建 kg_class_circle 表 (班级圈)
CREATE TABLE IF NOT EXISTS `kg_class_circle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `author` VARCHAR(64) NOT NULL,
  `class_name` VARCHAR(64) NOT NULL,
  `content` TEXT,
  `likes` INT DEFAULT 0,
  `comments` INT DEFAULT 0,
  `media_urls` TEXT,
  `publish_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_class_name` (`class_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级圈表';

-- 4. 为高频业务表补充索引以优化查询性能
-- kg_student
ALTER TABLE `kg_student` ADD INDEX `idx_guardian_phone` (`guardian_phone`);
ALTER TABLE `kg_student` ADD INDEX `idx_class_id` (`class_id`);

-- kg_attendance
ALTER TABLE `kg_attendance` ADD INDEX `idx_student_date` (`student_id`, `attendance_date`);

-- kg_oa_approval
ALTER TABLE `kg_oa_approval` ADD INDEX `idx_applicant_status` (`applicant_id`, `status`);

-- kg_fee_payment
ALTER TABLE `kg_fee_payment` ADD INDEX `idx_student_id` (`student_id`);

-- kg_growth_record
ALTER TABLE `kg_growth_record` ADD INDEX `idx_student_id` (`student_id`);
