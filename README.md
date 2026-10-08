# 教学管理系统 (Teaching Management System)

## 项目简介
本项目是一个基于 Spring Boot + Vue3 的教学数字管理平台，实现了学生、班级、考勤、成绩、作业等核心业务模块，并利用 AI 辅助工具（Claude Code）完成了工程化开发与规范设计。

## 技术栈
*   **前端**：HTML + CSS + JavaScript + Vue3 + Element Plus + ECharts (CDN引入)
*   **后端**：Java + SpringBoot + MyBatis + Maven
*   **数据库**：MySQL

## 核心功能
1.  **登录**：基于拦截器的用户登录与权限校验。
2.  **仪表盘**：展示学生总数、班级总数、出勤率等数据可视化图表。
3.  **学生管理**：支持按姓名搜索、按班级筛选、分页显示。
4.  **班级/课程/考勤/成绩/作业管理**：全面覆盖教学场景。

## 项目文档
*   [需求文档](doc/需求文档.md)
*   [数据库设计](doc/数据库设计.md)
*   [接口文档](doc/接口文档.md)

## 开发规范
*   统一返回格式：`{ "code": 200, "message": "success", "data": {} }`
*   数据库查询采用参数化方式，防止SQL注入。
*   遵循阿里巴巴Java开发规范。
