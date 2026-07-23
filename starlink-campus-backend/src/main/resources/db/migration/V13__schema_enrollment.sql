CREATE TABLE IF NOT EXISTS `kg_enrollment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `child_name` VARCHAR(50) NOT NULL,
    `gender` TINYINT,
    `birth_date` DATE,
    `parent_name` VARCHAR(50),
    `phone` VARCHAR(20),
    `address` VARCHAR(200),
    `intent_class` VARCHAR(50),
    `status` VARCHAR(10) DEFAULT '意向',
    `source` VARCHAR(10) COMMENT '线上/推荐/自来',
    `visit_date` DATE,
    `remark` VARCHAR(500),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`),
    INDEX `idx_source` (`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招生管理';
