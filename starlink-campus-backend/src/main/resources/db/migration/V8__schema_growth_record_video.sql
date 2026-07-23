-- 添加视频 URL 字段
ALTER TABLE kg_growth_record ADD COLUMN video_url VARCHAR(500) COMMENT '视频URL' AFTER photo_url;
