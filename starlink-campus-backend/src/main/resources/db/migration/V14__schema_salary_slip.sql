CREATE TABLE IF NOT EXISTS `kg_salary_slip` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `staff_id` BIGINT NOT NULL,
    `month` VARCHAR(10) NOT NULL COMMENT '月份 2026-07',
    `base_salary` DECIMAL(10,2),
    `bonus` DECIMAL(10,2),
    `subsidies` DECIMAL(10,2),
    `deduction` DECIMAL(10,2),
    `social_insurance` DECIMAL(10,2),
    `net_pay` DECIMAL(10,2),
    `publish_status` VARCHAR(10) DEFAULT '待发布',
    `remark` VARCHAR(200),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_staff` (`staff_id`),
    INDEX `idx_month` (`month`),
    UNIQUE KEY `uk_staff_month` (`staff_id`, `month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工薪酬条';
