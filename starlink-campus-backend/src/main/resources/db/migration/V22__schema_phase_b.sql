-- 1. 3-6 岁儿童发展评估表
CREATE TABLE IF NOT EXISTS `kg_development_assessment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `term` VARCHAR(32) NOT NULL COMMENT '学期 (如 2026-Fall)',
  `health_score` DECIMAL(4,1) NOT NULL DEFAULT 0.0 COMMENT '健康领域得分',
  `language_score` DECIMAL(4,1) NOT NULL DEFAULT 0.0 COMMENT '语言领域得分',
  `social_score` DECIMAL(4,1) NOT NULL DEFAULT 0.0 COMMENT '社会领域得分',
  `science_score` DECIMAL(4,1) NOT NULL DEFAULT 0.0 COMMENT '科学领域得分',
  `art_score` DECIMAL(4,1) NOT NULL DEFAULT 0.0 COMMENT '艺术领域得分',
  `teacher_evaluation` TEXT COMMENT '教师综合评语',
  `teacher_id` BIGINT NOT NULL COMMENT '评估教师ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_term` (`student_id`, `term`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='3-6岁儿童发展评估表';

-- 2. 48小时食品留样与溯源
CREATE TABLE IF NOT EXISTS `kg_food_supplier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
  `license_number` VARCHAR(128) COMMENT '营业执照/食品经营许可证',
  `contact_person` VARCHAR(64) COMMENT '联系人',
  `contact_phone` VARCHAR(32) COMMENT '联系电话',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态: 1正常, 0黑名单',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食品供应商表';

CREATE TABLE IF NOT EXISTS `kg_food_sample` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `meal_type` VARCHAR(32) NOT NULL COMMENT '餐别 (BREAKFAST, LUNCH, SNACK)',
  `dish_name` VARCHAR(128) NOT NULL COMMENT '菜品名称',
  `sample_weight` DECIMAL(5,1) NOT NULL COMMENT '留样重量(克)',
  `sampler_id` BIGINT NOT NULL COMMENT '留样人ID',
  `fridge_no` VARCHAR(32) COMMENT '冷藏柜编号',
  `sample_time` DATETIME NOT NULL COMMENT '留样时间',
  `destroy_time` DATETIME COMMENT '销毁时间(>=48h)',
  `destroyer_id` BIGINT COMMENT '销毁人ID',
  `supplier_id` BIGINT COMMENT '关联供应商溯源ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_sample_time` (`sample_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食品48小时留样台账';

-- 3. 校车路线与刷卡
CREATE TABLE IF NOT EXISTS `kg_school_bus` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plate_number` VARCHAR(32) NOT NULL COMMENT '车牌号',
  `driver_name` VARCHAR(64) NOT NULL COMMENT '司机姓名',
  `driver_phone` VARCHAR(32) COMMENT '司机电话',
  `route_name` VARCHAR(128) NOT NULL COMMENT '路线名称',
  `capacity` INT NOT NULL COMMENT '核载人数',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态: 1正常运行, 0维修中',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校车信息表';

CREATE TABLE IF NOT EXISTS `kg_bus_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bus_id` BIGINT NOT NULL COMMENT '校车ID',
  `student_id` BIGINT NOT NULL COMMENT '幼儿ID',
  `direction` VARCHAR(16) NOT NULL COMMENT '方向 (TO_SCHOOL上学, TO_HOME放学)',
  `action_type` VARCHAR(16) NOT NULL COMMENT '动作 (BOARDING上车, ALIGHTING下车)',
  `action_time` DATETIME NOT NULL COMMENT '刷卡/刷脸时间',
  `station_name` VARCHAR(128) COMMENT '上下车站点名',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_bus_date` (`bus_id`, `action_time`),
  KEY `idx_student_date` (`student_id`, `action_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校车上下车记录表';

-- 4. 固定资产与物料台账
CREATE TABLE IF NOT EXISTS `kg_asset_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `asset_code` VARCHAR(64) NOT NULL UNIQUE COMMENT '资产编号',
  `asset_name` VARCHAR(128) NOT NULL COMMENT '资产/物料名称',
  `category` VARCHAR(64) NOT NULL COMMENT '分类 (TEACHING教具, TOY玩具, BOOK图书, DEVICE办公设备, OTHER其他)',
  `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '库存总数',
  `available_quantity` INT NOT NULL DEFAULT 0 COMMENT '可用/在库数',
  `unit_price` DECIMAL(10,2) COMMENT '单价',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产物料表';

CREATE TABLE IF NOT EXISTS `kg_asset_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `asset_id` BIGINT NOT NULL COMMENT '资产ID',
  `record_type` VARCHAR(32) NOT NULL COMMENT '操作类型 (INBOUND入库, OUTBOUND领用, RETURN归还, SCRAP报废)',
  `quantity` INT NOT NULL COMMENT '数量',
  `operator_id` BIGINT NOT NULL COMMENT '操作人/领用人ID',
  `remarks` VARCHAR(255) COMMENT '备注说明',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产出入库记录表';
