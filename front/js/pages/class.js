/**
 * 班级管理页面
 * 依赖：Vue 3 + Element Plus（均已通过 CDN 在 index.html 中加载）
 */
(function () {
  'use strict';

  // 引用全局 TP 工具库
  var TP = window.TP;

  /* ==============================
   * 模拟教师数据
   * ============================== */
  var mockTeachers = [
    { id: 1, name: '张老师' },
    { id: 2, name: '李老师' },
    { id: 3, name: '王老师' },
    { id: 4, name: '赵老师' },
    { id: 5, name: '陈老师' },
    { id: 6, name: '刘老师' }
  ];

  function getTeacherName(id) {
    var t = mockTeachers.filter(function (t) { return t.id === id; })[0];
    return t ? t.name : '未知';
  }

  /* ==============================
   * 模拟各班学生数据（供"查看学生"使用）
   * ============================== */
  var mockClassStudents = {};

  function initClassStudents() {
    var names1 = ['张明', '李华', '王芳', '赵强', '孙丽', '周杰', '吴敏', '郑伟', '陈静', '刘洋',
                  '黄婷', '林峰', '何雪', '罗刚', '梁玉', '宋涛', '唐琳', '韩磊', '冯芳', '曹磊',
                  '许婧', '邓超', '彭慧', '肖勇', '田甜', '董强', '潘洁', '蔡文', '苏蕾', '叶飞',
                  '蒋丽', '余波', '康静', '廖凯', '邱萍'];
    var genders = [1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,
                   2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2];
    var idx = 0;
    for (var c = 1; c <= 8; c++) {
      var count = c <= 3 ? 5 : (c <= 6 ? 4 : 3); // 不同班级不同人数
      var students = [];
      for (var i = 0; i < count; i++) {
        students.push({
          id: idx + 1,
          studentNo: '2024' + ('000' + (idx + 1)).slice(-4),
          name: names1[idx % names1.length],
          gender: genders[idx % genders.length],
          phone: '138' + ('0000' + (idx + 1)).slice(-5)
        });
        idx++;
      }
      mockClassStudents[c] = students;
    }
  }
  initClassStudents();

  /* ==============================
   * 模拟班级数据
   * ============================== */
  var mockClasses = [
    { id: 1, name: '高一(1)班', teacherId: 1, room: 'A301', studentCount: 5 },
    { id: 2, name: '高一(2)班', teacherId: 2, room: 'A302', studentCount: 5 },
    { id: 3, name: '高一(3)班', teacherId: 3, room: 'A303', studentCount: 5 },
    { id: 4, name: '高二(1)班', teacherId: 4, room: 'B201', studentCount: 4 },
    { id: 5, name: '高二(2)班', teacherId: 5, room: 'B202', studentCount: 4 },
    { id: 6, name: '高二(3)班', teacherId: 6, room: 'B203', studentCount: 4 },
    { id: 7, name: '高三(1)班', teacherId: 1, room: 'C101', studentCount: 3 },
    { id: 8, name: '高三(2)班', teacherId: 2, room: 'C102', studentCount: 0 },
    { id: 9, name: '高一(4)班', teacherId: 3, room: 'A304', studentCount: 0 },
    { id: 10, name: '高三(3)班', teacherId: 6, room: 'C103', studentCount: 0 }
  ];

  var nextId = 11;

  /* ==============================
   * Vue 组件
   * ============================== */
  var ClassPage = {
    template:
'<div>' +
  '<!-- 搜索/操作栏 -->' +
  '<div class="toolbar">' +
    '<el-input' +
      ' v-model="searchName"' +
      ' placeholder="请输入班级名称"' +
      ' clearable' +
      ' style="width: 220px"' +
      ' @keyup.enter="handleSearch"' +
    '/>' +
    '<el-button type="primary" @click="handleSearch">搜索</el-button>' +
    '<el-button @click="handleReset">重置</el-button>' +
    '<span class="toolbar-spacer"></span>' +
    '<el-button type="primary" @click="handleAdd">新增班级</el-button>' +
  '</div>' +

  '<!-- 数据表格 -->' +
  '<div class="table-panel">' +
    '<el-table' +
      ' :data="pagedData"' +
      ' border' +
      ' stripe' +
      ' style="width: 100%"' +
      ' empty-text="暂无数据"' +
    '>' +
      '<el-table-column prop="name" label="班级名称" width="160" />' +
      '<el-table-column label="负责老师" width="120">' +
        '<template v-slot="{ row }">{{ getTeacherName(row.teacherId) }}</template>' +
      '</el-table-column>' +
      '<el-table-column prop="room" label="教室编号" width="120" />' +
      '<el-table-column label="学生人数" width="100" align="center">' +
        '<template v-slot="{ row }">{{ row.studentCount }}</template>' +
      '</el-table-column>' +
      '<el-table-column label="操作" width="260" align="center" fixed="right">' +
        '<template v-slot="{ row }">' +
          '<el-button type="primary" size="small" link @click="handleViewStudents(row)">查看学生</el-button>' +
          '<el-button type="primary" size="small" link @click="handleEdit(row)">编辑</el-button>' +
          '<el-button type="danger" size="small" link @click="handleDelete(row)">删除</el-button>' +
        '</template>' +
      '</el-table-column>' +
    '</el-table>' +

    '<!-- 分页 -->' +
    '<div style="display: flex; justify-content: flex-end; padding: 16px 0;">' +
      '<el-pagination' +
        ' v-model:current-page="currentPage"' +
        ' v-model:page-size="pageSize"' +
        ' :page-sizes="[10, 15, 20]"' +
        ' :total="filteredTotal"' +
        ' layout="total, sizes, prev, pager, next, jumper"' +
        ' background' +
        ' @size-change="handleSizeChange"' +
        ' @current-change="handlePageChange"' +
      '/>' +
    '</div>' +
  '</div>' +

  '<!-- 新增/编辑弹窗 -->' +
  '<el-dialog' +
    ' v-model="dialogVisible"' +
    ' :title="dialogTitle"' +
    ' width="480px"' +
    ' :close-on-click-modal="false"' +
    ' @closed="resetForm"' +
  '>' +
    '<el-form' +
      ' ref="formRef"' +
      ' :model="form"' +
      ' :rules="rules"' +
      ' label-width="90px"' +
      ' class="dialog-form"' +
    '>' +
      '<el-form-item label="班级名称" prop="name">' +
        '<el-input v-model="form.name" placeholder="请输入班级名称" maxlength="30" />' +
      '</el-form-item>' +
      '<el-form-item label="负责老师">' +
        '<el-select v-model="form.teacherId" placeholder="请选择负责老师" style="width: 100%">' +
          '<el-option' +
            ' v-for="t in teacherOptions"' +
            ' :key="t.id"' +
            ' :label="t.name"' +
            ' :value="t.id"' +
          '/>' +
        '</el-select>' +
      '</el-form-item>' +
      '<el-form-item label="教室编号">' +
        '<el-input v-model="form.room" placeholder="请输入教室编号" maxlength="20" />' +
      '</el-form-item>' +
    '</el-form>' +
    '<template #footer>' +
      '<el-button @click="dialogVisible = false">取消</el-button>' +
      '<el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>' +
    '</template>' +
  '</el-dialog>' +

  '<!-- 查看学生弹窗 -->' +
  '<el-dialog' +
    ' v-model="studentDialogVisible"' +
    ' :title="studentDialogTitle"' +
    ' width="640px"' +
  '>' +
    '<el-table :data="viewStudents" border stripe empty-text="该班级暂无学生">' +
      '<el-table-column prop="studentNo" label="学号" width="140" />' +
      '<el-table-column prop="name" label="姓名" width="120" />' +
      '<el-table-column label="性别" width="80" align="center">' +
        '<template v-slot="{ row }">{{ row.gender === 1 ? \'男\' : \'女\' }}</template>' +
      '</el-table-column>' +
      '<el-table-column prop="phone" label="手机号" width="150" />' +
    '</el-table>' +
  '</el-dialog>' +
'</div>',

    data: function () {
      return {
        // 搜索
        searchName: '',

        // 班级数据
        allClasses: TP.deepClone(mockClasses),

        // 分页
        currentPage: 1,
        pageSize: 10,

        // 教师下拉
        teacherOptions: mockTeachers,

        // 新增/编辑弹窗
        dialogVisible: false,
        dialogTitle: '新增班级',
        isEdit: false,
        editId: null,
        submitting: false,

        // 表单
        form: {
          name: '',
          teacherId: null,
          room: ''
        },

        // 校验规则
        rules: {
          name: [
            { required: true, message: '请输入班级名称', trigger: 'blur' }
          ]
        },

        // 查看学生弹窗
        studentDialogVisible: false,
        studentDialogTitle: '',
        viewStudents: []
      };
    },

    computed: {
      filteredStudents: function () {
        var list = this.allClasses;
        if (this.searchName) {
          var kw = this.searchName.trim();
          list = list.filter(function (c) { return c.name.indexOf(kw) !== -1; });
        }
        return list;
      },

      filteredTotal: function () {
        return this.filteredStudents.length;
      },

      pagedData: function () {
        var start = (this.currentPage - 1) * this.pageSize;
        return this.filteredStudents.slice(start, start + this.pageSize);
      }
    },

    methods: {
      getTeacherName: getTeacherName,

      /* ---------- 搜索 / 重置 ---------- */
      handleSearch: function () {
        this.currentPage = 1;
      },

      handleReset: function () {
        this.searchName = '';
        this.currentPage = 1;
      },

      /* ---------- 分页 ---------- */
      handleSizeChange: function (size) {
        this.pageSize = size;
        this.currentPage = 1;
      },

      handlePageChange: function (page) {
        this.currentPage = page;
      },

      /* ---------- 新增 ---------- */
      handleAdd: function () {
        this.dialogTitle = '新增班级';
        this.isEdit = false;
        this.editId = null;
        this.resetFormData();
        this.dialogVisible = true;
      },

      /* ---------- 编辑 ---------- */
      handleEdit: function (row) {
        this.dialogTitle = '编辑班级';
        this.isEdit = true;
        this.editId = row.id;
        this.form = {
          name: row.name,
          teacherId: row.teacherId,
          room: row.room
        };
        this.dialogVisible = true;
      },

      /* ---------- 删除 ---------- */
      handleDelete: function (row) {
        if (row.studentCount > 0) {
          TP.showError('该班级下还有 ' + row.studentCount + ' 名学生，请先将学生移出班级后再删除');
          return;
        }

        var self = this;
        TP.confirm('确定删除班级「' + row.name + '」吗？删除后不可恢复。', '删除确认').then(function () {
          self.allClasses = self.allClasses.filter(function (c) { return c.id !== row.id; });
          var total = self.filteredStudents.length;
          var maxPage = Math.ceil(total / self.pageSize) || 1;
          if (self.currentPage > maxPage) {
            self.currentPage = maxPage;
          }
          TP.showSuccess('删除成功');
        }).catch(function () {
          // 用户取消
        });
      },

      /* ---------- 查看学生 ---------- */
      handleViewStudents: function (row) {
        this.studentDialogTitle = row.name + ' —— 学生列表';
        this.viewStudents = mockClassStudents[row.id] || [];
        this.studentDialogVisible = true;
      },

      /* ---------- 提交表单 ---------- */
      handleSubmit: function () {
        var self = this;
        this.$refs.formRef.validate(function (valid) {
          if (!valid) return;

          self.submitting = true;

          setTimeout(function () {
            var teacherId = self.form.teacherId || null;
            var room = self.form.room || '';

            if (self.isEdit) {
              self.allClasses = self.allClasses.map(function (c) {
                if (c.id === self.editId) {
                  return {
                    id: c.id,
                    name: self.form.name,
                    teacherId: teacherId,
                    room: room,
                    studentCount: c.studentCount
                  };
                }
                return c;
              });
              TP.showSuccess('修改成功');
            } else {
              self.allClasses.push({
                id: nextId++,
                name: self.form.name,
                teacherId: teacherId,
                room: room,
                studentCount: 0
              });
              mockClassStudents[nextId - 1] = [];
              TP.showSuccess('新增成功');
              self.currentPage = Math.ceil(self.filteredStudents.length / self.pageSize) || 1;
            }

            self.dialogVisible = false;
            self.submitting = false;
          }, 200);
        });
      },

      /* ---------- 表单重置 ---------- */
      resetFormData: function () {
        this.form = {
          name: '',
          teacherId: null,
          room: ''
        };
      },

      resetForm: function () {
        this.resetFormData();
        if (this.$refs.formRef) {
          this.$refs.formRef.resetFields();
        }
      }
    }
  };

  window.ClassPage = ClassPage;
})();
