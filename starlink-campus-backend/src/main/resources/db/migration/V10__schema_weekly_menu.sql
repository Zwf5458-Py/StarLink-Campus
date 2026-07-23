CREATE TABLE IF NOT EXISTS `kg_weekly_menu` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `week_start_date` DATE NOT NULL COMMENT '周起始日期',
    `day_of_week` TINYINT NOT NULL COMMENT '星期几(1-7)',
    `meal_type` VARCHAR(10) NOT NULL COMMENT '餐别',
    `dishes` JSON COMMENT '菜品列表',
    `nutrition_note` VARCHAR(500) COMMENT '营养备注',
    `allergen_warning` VARCHAR(200) COMMENT '过敏原警告',
    `publish_status` VARCHAR(10) DEFAULT '草稿',
    `created_by` BIGINT COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_week` (`week_start_date`),
    INDEX `idx_day_meal` (`day_of_week`, `meal_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每周食谱';
