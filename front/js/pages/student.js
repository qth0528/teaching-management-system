/**
 * 学生管理页面
 * 依赖：Vue 3 + Element Plus（均已通过 CDN 在 index.html 中加载）
 */
(function () {
  'use strict';

  // 引用全局 TP 工具库
  var TP = window.TP;

  /* ==============================
   * 模拟班级数据
   * ============================== */
  var mockClasses = [
    { id: 1, name: '高一(1)班' },
    { id: 2, name: '高一(2)班' },
    { id: 3, name: '高一(3)班' },
    { id: 4, name: '高二(1)班' },
    { id: 5, name: '高二(2)班' },
    { id: 6, name: '高二(3)班' },
    { id: 7, name: '高三(1)班' },
    { id: 8, name: '高三(2)班' }
  ];

  function getClassName(id) {
    var c = mockClasses.filter(function (c) { return c.id === id; })[0];
    return c ? c.name : '未知';
  }

  /* ==============================
   * 模拟学生数据（≥30 条）
   * ============================== */
  var mockStudents = [
    { id: 1,  studentNo: '20240001', name: '张明',      gender: 1, classId: 1, phone: '13800138001', address: '北京市朝阳区建国路100号' },
    { id: 2,  studentNo: '20240002', name: '李华',      gender: 2, classId: 1, phone: '13800138002', address: '北京市海淀区中关村大街22号' },
    { id: 3,  studentNo: '20240003', name: '王芳',      gender: 2, classId: 1, phone: '13800138003', address: '北京市西城区金融街15号' },
    { id: 4,  studentNo: '20240004', name: '赵强',      gender: 1, classId: 1, phone: '13800138004', address: '北京市东城区王府井大街8号' },
    { id: 5,  studentNo: '20240005', name: '孙丽',      gender: 2, classId: 2, phone: '13800138005', address: '上海市浦东新区陆家嘴路200号' },
    { id: 6,  studentNo: '20240006', name: '周杰',      gender: 1, classId: 2, phone: '13800138006', address: '上海市徐汇区衡山路12号' },
    { id: 7,  studentNo: '20240007', name: '吴敏',      gender: 2, classId: 2, phone: '13800138007', address: '上海市静安区南京西路88号' },
    { id: 8,  studentNo: '20240008', name: '郑伟',      gender: 1, classId: 2, phone: '13800138008', address: '广州市天河区体育西路30号' },
    { id: 9,  studentNo: '20240009', name: '陈静',      gender: 2, classId: 3, phone: '13800138009', address: '广州市越秀区北京路55号' },
    { id: 10, studentNo: '20240010', name: '刘洋',      gender: 1, classId: 3, phone: '13800138010', address: '深圳市南山区科技园路18号' },
    { id: 11, studentNo: '20240011', name: '黄婷',      gender: 2, classId: 3, phone: '13800138011', address: '深圳市福田区华强北路9号' },
    { id: 12, studentNo: '20240012', name: '林峰',      gender: 1, classId: 3, phone: '13800138012', address: '杭州市西湖区文三路42号' },
    { id: 13, studentNo: '20240013', name: '何雪',      gender: 2, classId: 4, phone: '13800138013', address: '杭州市拱墅区湖墅南路16号' },
    { id: 14, studentNo: '20240014', name: '罗刚',      gender: 1, classId: 4, phone: '13800138014', address: '成都市武侯区人民南路72号' },
    { id: 15, studentNo: '20240015', name: '梁玉',      gender: 2, classId: 4, phone: '13800138015', address: '成都市锦江区春熙路33号' },
    { id: 16, studentNo: '20240016', name: '宋涛',      gender: 1, classId: 4, phone: '13800138016', address: '武汉市洪山区珞喻路66号' },
    { id: 17, studentNo: '20240017', name: '唐琳',      gender: 2, classId: 5, phone: '13800138017', address: '武汉市武昌区中南路28号' },
    { id: 18, studentNo: '20240018', name: '韩磊',      gender: 1, classId: 5, phone: '13800138018', address: '南京市鼓楼区中山路14号' },
    { id: 19, studentNo: '20240019', name: '冯芳',      gender: 2, classId: 5, phone: '13800138019', address: '南京市秦淮区夫子庙3号' },
    { id: 20, studentNo: '20240020', name: '曹磊',      gender: 1, classId: 5, phone: '13800138020', address: '重庆市渝中区解放碑11号' },
    { id: 21, studentNo: '20240021', name: '许婧',      gender: 2, classId: 6, phone: '13800138021', address: '重庆市江北区观音桥25号' },
    { id: 22, studentNo: '20240022', name: '邓超',      gender: 1, classId: 6, phone: '13800138022', address: '西安市雁塔区小寨路88号' },
    { id: 23, studentNo: '20240023', name: '彭慧',      gender: 2, classId: 6, phone: '13800138023', address: '西安市碑林区友谊西路40号' },
    { id: 24, studentNo: '20240024', name: '肖勇',      gender: 1, classId: 6, phone: '13800138024', address: '长沙市岳麓区麓山南路2号' },
    { id: 25, studentNo: '20240025', name: '田甜',      gender: 2, classId: 7, phone: '13800138025', address: '长沙市芙蓉区五一大道11号' },
    { id: 26, studentNo: '20240026', name: '董强',      gender: 1, classId: 7, phone: '13800138026', address: '天津市和平区南京路31号' },
    { id: 27, studentNo: '20240027', name: '潘洁',      gender: 2, classId: 7, phone: '13800138027', address: '天津市南开区卫津路92号' },
    { id: 28, studentNo: '20240028', name: '蔡文',      gender: 1, classId: 7, phone: '13800138028', address: '苏州市姑苏区观前街15号' },
    { id: 29, studentNo: '20240029', name: '苏蕾',      gender: 2, classId: 8, phone: '13800138029', address: '苏州市吴中区东吴北路6号' },
    { id: 30, studentNo: '20240030', name: '叶飞',      gender: 1, classId: 8, phone: '13800138030', address: '青岛市市南区香港中路18号' },
    { id: 31, studentNo: '20240031', name: '蒋丽',      gender: 2, classId: 8, phone: '13800138031', address: '青岛市崂山区海尔路7号' },
    { id: 32, studentNo: '20240032', name: '余波',      gender: 1, classId: 8, phone: '13800138032', address: '大连市中山区人民路5号' },
    { id: 33, studentNo: '20240033', name: '康静',      gender: 2, classId: 8, phone: '13800138033', address: '厦门市思明区湖滨南路12号' },
    { id: 34, studentNo: '20240034', name: '廖凯',      gender: 1, classId: 8, phone: '13800138034', address: '厦门市湖里区嘉禾路28号' },
    { id: 35, studentNo: '20240035', name: '邱萍',      gender: 2, classId: 1, phone: '13800138035', address: '济南市历下区泉城路20号' }
  ];

  var nextId = 36;

  /* ==============================
   * 模拟关联数据（删除时显示）
   * ============================== */
  function getRelatedCount(studentId) {
    // 用 studentId 的个位数模拟关联的考勤和作业记录数
    var n = studentId % 10;
    return {
      attendance: n > 0 ? n * 3 : 5,
      homework: n > 0 ? n * 2 : 3
    };
  }

  /* ==============================
   * Vue 组件
   * ============================== */
  var StudentPage = {
    template:
`<div>
  <!-- 搜索/操作栏 -->
  <div class="toolbar">
    <el-input
      v-model="searchName"
      placeholder="请输入姓名"
      clearable
      style="width: 200px"
      @keyup.enter="handleSearch"
    />
    <el-select
      v-model="searchClassId"
      placeholder="请选择班级"
      clearable
      style="width: 200px"
    >
      <el-option
        v-for="c in classOptions"
        :key="c.id"
        :label="c.name"
        :value="c.id"
      />
    </el-select>
    <el-button type="primary" @click="handleSearch">搜索</el-button>
    <el-button @click="handleReset">重置</el-button>
    <span class="toolbar-spacer"></span>
    <el-button type="primary" @click="handleAdd">新增学生</el-button>
  </div>

  <!-- 数据表格 -->
  <div class="table-panel">
    <el-table
      :data="pagedData"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
      empty-text="暂无数据"
    >
      <el-table-column prop="studentNo" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column label="性别" width="80" align="center">
        <template v-slot="{ row }">
          {{ TP.genderLabel(row.gender) }}
        </template>
      </el-table-column>
      <el-table-column label="班级" width="140">
        <template v-slot="{ row }">
          {{ getClassName(row.classId) }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template v-slot="{ row }">
          <el-button type="primary" size="small" link @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" link @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="display: flex; justify-content: flex-end; padding: 16px 0;">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30]"
        :total="filteredTotal"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>

  <!-- 新增/编辑弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="560px"
    :close-on-click-modal="false"
    @closed="resetForm"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="90px"
      class="dialog-form"
    >
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="form.studentNo" placeholder="请输入学号" maxlength="20" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名" maxlength="30" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
          <el-option label="男" :value="1" />
          <el-option label="女" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级" prop="classId">
        <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%">
          <el-option
            v-for="c in classOptions"
            :key="c.id"
            :label="c.name"
            :value="c.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
      </el-form-item>
      <el-form-item label="家庭住址">
        <el-input
          v-model="form.address"
          type="textarea"
          :rows="2"
          placeholder="请输入家庭住址"
          maxlength="200"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</div>`,

    data: function () {
      return {
        // 搜索条件
        searchName: '',
        searchClassId: null,

        // 班级下拉选项
        classOptions: mockClasses,

        // 原始数据（深拷贝，确保编辑不影响原始数据）
        allStudents: TP.deepClone(mockStudents),

        // 分页
        currentPage: 1,
        pageSize: 10,
        loading: false,

        // 弹窗
        dialogVisible: false,
        dialogTitle: '新增学生',
        isEdit: false,
        editId: null,
        submitting: false,

        // 表单
        form: {
          studentNo: '',
          name: '',
          gender: 1,
          classId: null,
          phone: '',
          address: ''
        },

        // 校验规则
        rules: {
          studentNo: [
            { required: true, message: '请输入学号', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' }
          ],
          phone: [
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
          ]
        },

        // 对外暴露 TP
        TP: window.TP
      };
    },

    computed: {
      // 过滤后的数据
      filteredStudents: function () {
        var list = this.allStudents;
        if (this.searchName) {
          var kw = this.searchName.trim();
          list = list.filter(function (s) { return s.name.indexOf(kw) !== -1; });
        }
        if (this.searchClassId !== null && this.searchClassId !== '') {
          list = list.filter(function (s) { return s.classId === this.searchClassId; }, this);
        }
        return list;
      },

      filteredTotal: function () {
        return this.filteredStudents.length;
      },

      // 当前页数据
      pagedData: function () {
        var start = (this.currentPage - 1) * this.pageSize;
        var end = start + this.pageSize;
        return this.filteredStudents.slice(start, end);
      }
    },

    methods: {
      getClassName: getClassName,

      /* ---------- 搜索 / 重置 ---------- */
      handleSearch: function () {
        this.currentPage = 1;
      },

      handleReset: function () {
        this.searchName = '';
        this.searchClassId = null;
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
        this.dialogTitle = '新增学生';
        this.isEdit = false;
        this.editId = null;
        this.resetFormData();
        this.dialogVisible = true;
      },

      /* ---------- 编辑 ---------- */
      handleEdit: function (row) {
        this.dialogTitle = '编辑学生';
        this.isEdit = true;
        this.editId = row.id;
        this.form = {
          studentNo: row.studentNo,
          name: row.name,
          gender: row.gender,
          classId: row.classId,
          phone: row.phone,
          address: row.address
        };
        this.dialogVisible = true;
      },

      /* ---------- 删除 ---------- */
      handleDelete: function (row) {
        var counts = getRelatedCount(row.id);
        var total = counts.attendance + counts.homework;
        var msg = '确定删除学生「' + row.name + '」吗？';

        if (total > 0) {
          msg += '\n\n该学生有以下关联数据：';
          msg += '\n  - 考勤记录：' + counts.attendance + ' 条';
          msg += '\n  - 作业提交记录：' + counts.homework + ' 条';
          msg += '\n\n删除后，上述关联数据也将一并删除。';
        }

        var self = this;
        TP.confirm(msg, '删除确认').then(function () {
          self.loading = true;
          // 模拟删除
          setTimeout(function () {
            self.allStudents = self.allStudents.filter(function (s) {
              return s.id !== row.id;
            });
            // 如果当前页没数据了，往前翻一页
            var totalAfter = self.filteredStudents.length;
            var maxPage = Math.ceil(totalAfter / self.pageSize) || 1;
            if (self.currentPage > maxPage) {
              self.currentPage = maxPage;
            }
            self.loading = false;
            TP.showSuccess('删除成功');
          }, 300);
        }).catch(function () {
          // 用户取消
        });
      },

      /* ---------- 提交表单 ---------- */
      handleSubmit: function () {
        var self = this;
        this.$refs.formRef.validate(function (valid) {
          if (!valid) return;

          self.submitting = true;

          // 模拟保存延迟
          setTimeout(function () {
            if (self.isEdit) {
              // 编辑模式：更新已有记录
              self.allStudents = self.allStudents.map(function (s) {
                if (s.id === self.editId) {
                  return {
                    id: s.id,
                    studentNo: self.form.studentNo,
                    name: self.form.name,
                    gender: self.form.gender,
                    classId: self.form.classId,
                    phone: self.form.phone,
                    address: self.form.address
                  };
                }
                return s;
              });
              TP.showSuccess('修改成功');
            } else {
              // 新增模式：添加到数组头部
              self.allStudents.unshift({
                id: nextId++,
                studentNo: self.form.studentNo,
                name: self.form.name,
                gender: self.form.gender,
                classId: self.form.classId,
                phone: self.form.phone,
                address: self.form.address
              });
              TP.showSuccess('新增成功');
              self.currentPage = 1;
            }

            self.dialogVisible = false;
            self.submitting = false;
          }, 300);
        });
      },

      /* ---------- 表单重置 ---------- */
      resetFormData: function () {
        this.form = {
          studentNo: '',
          name: '',
          gender: 1,
          classId: null,
          phone: '',
          address: ''
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

  window.StudentPage = StudentPage;
})();
