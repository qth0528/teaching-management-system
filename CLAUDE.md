# CLAUDE.md

## 项目概述

教学数字平台的管理系统。

## 技术选型

### 前端
- **技术栈**: HTML + CSS + JavaScript + Vue 3 + Element Plus + ECharts
- **引入方式**: 全部通过 CDN 引入，**禁止使用 npm / webpack / vite 等构建工具**
- **目录**: `front/`

### 后端
- **技术栈**: Java + Spring Boot + MyBatis + Maven
- **目录**: `backend/`

### 数据库
- **数据库**: MySQL

## 项目结构

```
/
├── doc/          # 文档
├── front/        # 前端页面（HTML/CSS/JS，CDN 引入 Vue3/Element Plus/ECharts）
├── backend/      # 后端代码（Spring Boot + MyBatis + Maven）
└── CLAUDE.md     # 本文件
```

## 代码规范

### Java（后端）

- 严格遵循**阿里巴巴 Java 开发手册**规范
- 所有 Controller 接口必须做参数校验（使用 `@Valid` / `@Validated` + JSR-303 注解）
- 数据库查询**必须使用参数化方式**（MyBatis `#{ }` 占位符），**禁止字符串拼接 SQL**
- 所有接口统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- 每张数据表必须包含以下四个字段：
  - `id` — 主键
  - `create_time` — 创建时间
  - `update_time` — 更新时间
  - `is_deleted` — 逻辑删除标记

- 接口地址统一以 `/api/` 开头

### 前端

- 所有表单必须做输入校验（前端校验 + 后端校验双层保障）
- 使用 Element Plus 组件库
- 图表使用 ECharts

## 开发约定

1. **一次只修改一个文件** — 每次提交/修改聚焦在单个文件上
2. **接口地址统一前缀** — 所有后端 API 以 `/api/` 开头
3. **统一返回格式** — `{ "code": 200, "message": "success", "data": {} }`
4. **数据库通用字段** — 每张表必须包含 `id`, `create_time`, `update_time`, `is_deleted`
5. **SQL 安全** — 禁止字符串拼接，一律使用参数化查询
6. **禁止构建工具** — 前端仅通过 CDN 引入依赖，不使用 npm/webpack/vite

## 强制确认机制

**每完成一个阶段或文件，必须暂停并向开发者确认以下三点，批准后方可继续：**

1. 刚才产出了什么（文件清单、改动摘要）
2. 开发者确认了哪些内容（核对已完成的工作是否与预期一致）
3. 是否批准进入下一步（未经开发者明确批准，不得执行下一任务）
