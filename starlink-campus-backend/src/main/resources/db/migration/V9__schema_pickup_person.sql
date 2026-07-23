CREATE TABLE IF NOT EXISTS `kg_pickup_person` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `relation` VARCHAR(20) NOT NULL COMMENT '关系: 父亲/母亲/祖父...',
    `phone` VARCHAR(20) COMMENT '手机号',
    `id_card_no` VARCHAR(20) COMMENT '身份证号',
    `face_feature_id` VARCHAR(100) COMMENT '人脸特征ID',
    `ic_card_no` VARCHAR(50) COMMENT 'IC卡号',
    `is_primary` TINYINT DEFAULT 0 COMMENT '1主要/0辅助',
    `photo_url` VARCHAR(500) COMMENT '照片',
    `status` VARCHAR(10) DEFAULT '待审核' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_student` (`student_id`),
    INDEX `idx_ic_card` (`ic_card_no`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接送人管理';
