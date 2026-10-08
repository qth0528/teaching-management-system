-- =====================================================
-- 教学数字平台管理系统 —— 数据库建表脚本
-- 数据库: MySQL 8.0
-- 字符集: utf8mb4
-- 存储引擎: InnoDB
-- =====================================================

CREATE DATABASE IF NOT EXISTS `teaching_platform`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `teaching_platform`;

-- =====================================================
-- 1. 用户表
-- =====================================================
CREATE TABLE `user` (
  `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username`         VARCHAR(50)  NOT NULL                COMMENT '用户名',
  `password`         VARCHAR(255) NOT NULL                COMMENT '密码（BCrypt加密存储）',
  `status`           TINYINT      NOT NULL DEFAULT 1      COMMENT '账号状态：1=正常，0=锁定',
  `login_fail_count` INT          NOT NULL DEFAULT 0      COMMENT '连续登录失败次数',
  `locked_until`     DATETIME     DEFAULT NULL            COMMENT '锁定截止时间',
  `last_login_time`  DATETIME     DEFAULT NULL            COMMENT '最后登录时间',
  `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`       TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 教师表
-- =====================================================
CREATE TABLE `teacher` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`     BIGINT      NOT NULL                COMMENT '关联用户ID',
  `teacher_no`  VARCHAR(30) NOT NULL                COMMENT '教师编号',
  `name`        VARCHAR(50) NOT NULL                COMMENT '教师姓名',
  `gender`      TINYINT     NOT NULL DEFAULT 0      COMMENT '性别：1=男，2=女',
  `phone`       VARCHAR(20) DEFAULT NULL            COMMENT '手机号',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT     NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_no` (`teacher_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师表';

-- =====================================================
-- 3. 班级表
-- =====================================================
CREATE TABLE `class` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_name`  VARCHAR(50) NOT NULL                COMMENT '班级名称',
  `teacher_id`  BIGINT      NOT NULL                COMMENT '负责教师ID',
  `room_number` VARCHAR(30) DEFAULT NULL            COMMENT '教室编号',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT     NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_class_name` (`class_name`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- =====================================================
-- 4. 学生表
-- =====================================================
CREATE TABLE `student` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_no`  VARCHAR(30)  NOT NULL                COMMENT '学号',
  `name`        VARCHAR(50)  NOT NULL                COMMENT '学生姓名',
  `gender`      TINYINT      NOT NULL DEFAULT 0      COMMENT '性别：1=男，2=女',
  `class_id`    BIGINT       NOT NULL                COMMENT '所属班级ID',
  `phone`       VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
  `address`     VARCHAR(255) DEFAULT NULL            COMMENT '家庭住址',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_no` (`student_no`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- =====================================================
-- 5. 课程表
-- =====================================================
CREATE TABLE `course` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_name` VARCHAR(50)  NOT NULL                COMMENT '课程名称',
  `full_score`  DECIMAL(5,1) NOT NULL DEFAULT 100.0  COMMENT '满分值',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_name` (`course_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- =====================================================
-- 6. 排课表
-- =====================================================
CREATE TABLE `schedule` (
  `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_id`     BIGINT      NOT NULL                COMMENT '班级ID',
  `course_id`    BIGINT      NOT NULL                COMMENT '课程ID',
  `teacher_id`   BIGINT      NOT NULL                COMMENT '授课教师ID',
  `day_of_week`  TINYINT     NOT NULL                COMMENT '星期：1=周一，2=周二，3=周三，4=周四，5=周五',
  `period`       TINYINT     NOT NULL                COMMENT '节次：1~8',
  `room_number`  VARCHAR(30) DEFAULT NULL            COMMENT '上课教室',
  `semester`     VARCHAR(30) NOT NULL                COMMENT '学期（如2025-2026-1）',
  `create_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`   TINYINT     NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  KEY `idx_class_semester` (`class_id`, `semester`),
  UNIQUE KEY `uk_class_time` (`class_id`, `day_of_week`, `period`, `semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排课表';

-- =====================================================
-- 7. 考勤表
-- =====================================================
CREATE TABLE `attendance` (
  `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id`      BIGINT   NOT NULL                COMMENT '学生ID',
  `class_id`        BIGINT   NOT NULL                COMMENT '班级ID（冗余，便于按班级查询）',
  `attendance_date` DATE     NOT NULL                COMMENT '考勤日期',
  `status`          TINYINT  NOT NULL DEFAULT 1      COMMENT '考勤状态：1=出勤，2=迟到，3=请假，4=缺勤',
  `create_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`      TINYINT  NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_date` (`student_id`, `attendance_date`),
  KEY `idx_class_date` (`class_id`, `attendance_date`),
  KEY `idx_attendance_date` (`attendance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤表';

-- =====================================================
-- 8. 成绩表
-- =====================================================
CREATE TABLE `score` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id`  BIGINT       NOT NULL                COMMENT '学生ID',
  `class_id`    BIGINT       NOT NULL                COMMENT '班级ID（冗余，记录考试时所在班级）',
  `course_id`   BIGINT       NOT NULL                COMMENT '课程ID',
  `exam_name`   VARCHAR(100) NOT NULL                COMMENT '考试名称（如"第一次月考"）',
  `exam_type`   TINYINT      NOT NULL DEFAULT 5      COMMENT '考试类型：1=月考，2=期中，3=期末，4=模拟考，5=其他',
  `score`       DECIMAL(5,1) DEFAULT NULL            COMMENT '分数，NULL表示缺考',
  `exam_date`   DATE         NOT NULL                COMMENT '考试日期',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_exam_course` (`student_id`, `exam_name`, `course_id`),
  KEY `idx_class_course` (`class_id`, `course_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_exam_name` (`exam_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩表';

-- =====================================================
-- 9. 作业表
-- =====================================================
CREATE TABLE `homework` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title`        VARCHAR(200) NOT NULL                COMMENT '作业标题',
  `description`  TEXT         DEFAULT NULL            COMMENT '作业内容描述',
  `class_id`     BIGINT       NOT NULL                COMMENT '班级ID',
  `course_id`    BIGINT       NOT NULL                COMMENT '课程ID',
  `teacher_id`   BIGINT       NOT NULL                COMMENT '发布教师ID',
  `publish_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `deadline`     DATETIME     NOT NULL                COMMENT '截止时间',
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`   TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业表';

-- =====================================================
-- 10. 作业提交表
-- =====================================================
CREATE TABLE `homework_submission` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `homework_id` BIGINT        NOT NULL                COMMENT '作业ID',
  `student_id`  BIGINT        NOT NULL                COMMENT '学生ID',
  `content`     TEXT          DEFAULT NULL            COMMENT '提交内容',
  `submit_time` DATETIME      DEFAULT NULL            COMMENT '提交时间，NULL表示未提交',
  `score`       DECIMAL(5,1)  DEFAULT NULL            COMMENT '批改分数（0~100）',
  `comment`     VARCHAR(500)  DEFAULT NULL            COMMENT '教师评语',
  `status`      TINYINT       NOT NULL DEFAULT 1      COMMENT '批改状态：1=未提交，2=已提交待批改，3=已批改',
  `is_late`     TINYINT       NOT NULL DEFAULT 0      COMMENT '是否迟交：0=否，1=是',
  `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  TINYINT       NOT NULL DEFAULT 0      COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_homework_student` (`homework_id`, `student_id`),
  KEY `idx_homework_status` (`homework_id`, `status`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业提交表';

-- =====================================================
-- 初始数据：管理员账号
-- 密码：123456（BCrypt加密）
-- =====================================================
INSERT INTO `user` (`username`, `password`, `status`, `login_fail_count`) VALUES
('admin', '$2b$12$uIrBGq/oVxB42qhcyplYnukr7mIFolELMdG6RU5pNiOqglCirRpKC', 1, 0);

-- =====================================================
-- 初始数据：教师
-- =====================================================
INSERT INTO `teacher` (`user_id`, `teacher_no`, `name`, `gender`, `phone`) VALUES
(1, 'T001', '张老师', 1, '13800001111'),
(1, 'T002', '李老师', 2, '13800002222'),
(1, 'T003', '王老师', 1, '13800003333');
