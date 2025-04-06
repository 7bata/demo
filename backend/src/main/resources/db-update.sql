-- 添加start_time和end_time字段到class_hour_records表
ALTER TABLE class_hour_records 
ADD COLUMN start_time VARCHAR(10) DEFAULT '08:00' COMMENT '课程开始时间',
ADD COLUMN end_time VARCHAR(10) DEFAULT '09:40' COMMENT '课程结束时间';

-- 查看表结构
DESCRIBE class_hour_records; 