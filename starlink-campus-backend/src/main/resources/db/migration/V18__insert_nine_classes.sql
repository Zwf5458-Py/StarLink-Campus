-- 补充大中小各 3 个班级 (共 9 个班级) 初始数据
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `kg_class`;

INSERT INTO `kg_class` (`id`, `campus_id`, `grade_level`, `class_name`, `student_count`, `room_number`, `create_time`) VALUES
(1, 1, '小班', '小(1)班 - 雏菊班', 20, 'C101', NOW()),
(2, 1, '小班', '小(2)班 - 苹果班', 22, 'C102', NOW()),
(3, 1, '小班', '小(3)班 - 樱桃班', 20, 'C103', NOW()),
(4, 1, '中班', '中(1)班 - 满天星班', 25, 'B201', NOW()),
(5, 1, '中班', '中(2)班 - 向日葵班', 24, 'B202', NOW()),
(6, 1, '中班', '中(3)班 - 郁金香班', 25, 'B203', NOW()),
(7, 1, '大班', '大(1)班 - 葵花班', 28, 'A301', NOW()),
(8, 1, '大班', '大(2)班 - 麦穗班', 30, 'A302', NOW()),
(9, 1, '大班', '大(3)班 - 飞天班', 29, 'A303', NOW());

-- 更新现有测试学生绑定到有效班级 (小(1)班、大(1)班等)
UPDATE `kg_student` SET `class_id` = 1 WHERE `id` = 101;
UPDATE `kg_student` SET `class_id` = 4 WHERE `id` = 102;
UPDATE `kg_student` SET `class_id` = 7 WHERE `id` = 103;

SET FOREIGN_KEY_CHECKS = 1;
