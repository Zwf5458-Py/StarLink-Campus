-- 添加 OA 申请人 ID 字段
ALTER TABLE kg_oa_approval ADD COLUMN applicant_id BIGINT COMMENT '申请人ID' AFTER id;
