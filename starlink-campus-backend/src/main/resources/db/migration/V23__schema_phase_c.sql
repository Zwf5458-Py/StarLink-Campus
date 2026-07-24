-- 1. 开放日活动与签到核销
CREATE TABLE IF NOT EXISTS `kg_open_day_event` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `event_title` VARCHAR(255) NOT NULL COMMENT '活动标题',
  `event_date` DATETIME NOT NULL COMMENT '活动时间',
  `location` VARCHAR(255) NOT NULL COMMENT '活动地点',
  `capacity` INT NOT NULL COMMENT '容量上限',
  `enrolled_count` INT DEFAULT 0 COMMENT '已报名人数',
  `status` VARCHAR(32) DEFAULT 'PUBLISHED' COMMENT '状态 (PUBLISHED已发布, FULL满额, COMPLETED已结束)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招生开放日活动表';

-- 2. 老带新转介绍追踪
CREATE TABLE IF NOT EXISTS `kg_referral_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `referrer_parent_id` BIGINT NOT NULL COMMENT '推荐人(在读家长)ID',
  `new_family_name` VARCHAR(128) NOT NULL COMMENT '被推荐家庭姓名',
  `contact_phone` VARCHAR(32) NOT NULL COMMENT '被推荐人电话',
  `referral_status` VARCHAR(32) DEFAULT 'LEAD' COMMENT '状态 (LEAD线索, VISITED已到访, ENROLLED已入园, REJECTED已流失)',
  `reward_status` VARCHAR(32) DEFAULT 'UNPAID' COMMENT '奖励发放状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '推荐时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_referrer` (`referrer_parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老带新转介绍记录表';

-- 3. 毕业生追踪
CREATE TABLE IF NOT EXISTS `kg_graduate_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `graduation_year` INT NOT NULL COMMENT '毕业年份',
  `primary_school_name` VARCHAR(255) COMMENT '升入小学名称',
  `future_direction` VARCHAR(128) COMMENT '未来方向(公立/私立/国际)',
  `contact_info` VARCHAR(128) COMMENT '毕业后联络方式',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_graduation_year` (`graduation_year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='毕业生去向追踪表';

-- 4. IoT 环境监测
CREATE TABLE IF NOT EXISTS `kg_environment_monitor` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `class_id` BIGINT NOT NULL COMMENT '班级ID (关联班牌)',
  `temperature` DECIMAL(4,1) COMMENT '温度',
  `humidity` DECIMAL(4,1) COMMENT '湿度',
  `co2_level` INT COMMENT 'CO2浓度(ppm)',
  `pm25_level` INT COMMENT 'PM2.5浓度',
  `warning_triggered` TINYINT(1) DEFAULT 0 COMMENT '是否触发预警',
  `record_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_time` (`class_id`, `record_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室环境质量监测表';

-- 5. 智能闸机联动
CREATE TABLE IF NOT EXISTS `kg_smart_gate_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `person_type` VARCHAR(32) NOT NULL COMMENT '人员类型 (STUDENT幼儿, STAFF教工, VISITOR访客)',
  `person_id` BIGINT NOT NULL COMMENT '关联人员ID',
  `gate_no` VARCHAR(64) NOT NULL COMMENT '闸机编号',
  `pass_direction` VARCHAR(16) NOT NULL COMMENT '通行方向 (IN进园, OUT出园)',
  `pass_method` VARCHAR(32) NOT NULL COMMENT '通行方式 (FACE人脸, IC_CARD刷卡)',
  `capture_image` VARCHAR(255) COMMENT '抓拍图像',
  `pass_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '通行时间',
  PRIMARY KEY (`id`),
  KEY `idx_person_time` (`person_type`, `person_id`, `pass_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能闸机通行记录表';
