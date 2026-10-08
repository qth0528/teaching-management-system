/**
 * 仪表盘页面
 * 依赖：Vue 3 + Element Plus + ECharts（均已通过 CDN 在 index.html 中加载）
 */
(function () {
  'use strict';

  // 引用全局 ECharts
  var echarts = window.echarts;

  // 开发环境后端地址，部署到同一域名时改为空字符串 ''
  var API_BASE = 'http://localhost:8080';

  var DashboardPage = {
    template:
      '<div v-loading="loading">' +
        '<!-- 统计卡片 -->' +
        '<div class="stat-cards">' +
          '<div class="stat-card">' +
            '<div class="card-icon blue">&#x1f393;</div>' +
            '<div class="card-info">' +
              '<div class="card-value">{{ stats.studentCount }}</div>' +
              '<div class="card-label">学生总数</div>' +
            '</div>' +
          '</div>' +
          '<div class="stat-card">' +
            '<div class="card-icon green">&#x1f3eb;</div>' +
            '<div class="card-info">' +
              '<div class="card-value">{{ stats.classCount }}</div>' +
              '<div class="card-label">班级总数</div>' +
            '</div>' +
          '</div>' +
          '<div class="stat-card">' +
            '<div class="card-icon orange">&#x2705;</div>' +
            '<div class="card-info">' +
              '<div class="card-value">{{ stats.todayAttendanceRate }}<small>%</small></div>' +
              '<div class="card-label">今日出勤率</div>' +
            '</div>' +
          '</div>' +
          '<div class="stat-card">' +
            '<div class="card-icon red">&#x1f4dd;</div>' +
            '<div class="card-info">' +
              '<div class="card-value">{{ stats.pendingHomeworkCount }}</div>' +
              '<div class="card-label">待批改作业数</div>' +
            '</div>' +
          '</div>' +
        '</div>' +

        '<!-- 图表区域 -->' +
        '<div class="chart-row">' +
          '<div class="chart-panel">' +
            '<div class="chart-title">各班级学生人数分布</div>' +
            '<div ref="classChart" class="chart-container">' +
              '<div v-if="classData.length === 0" style="text-align:center;padding-top:60px;color:#999;">暂无数据</div>' +
            '</div>' +
          '</div>' +
          '<div class="chart-panel">' +
            '<div class="chart-title">近 7 天出勤趋势</div>' +
            '<div ref="attendanceChart" class="chart-container">' +
              '<div v-if="attendanceTrend.length === 0" style="text-align:center;padding-top:60px;color:#999;">暂无数据</div>' +
            '</div>' +
          '</div>' +
        '</div>' +
      '</div>',

    data: function () {
      return {
        loading: false,
        stats: {
          studentCount: 0,
          classCount: 0,
          todayAttendanceRate: 0,
          pendingHomeworkCount: 0
        },
        classData: [],
        attendanceTrend: [],
        classChartInstance: null,
        attendanceChartInstance: null
      };
    },

    mounted: function () {
      var self = this;
      self.loading = true;

      // 并行请求三个接口
      Promise.all([
        fetch(API_BASE + '/api/dashboard/stats', {
          headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }).then(function (r) { return r.json(); }),
        fetch(API_BASE + '/api/dashboard/class-distribution', {
          headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }).then(function (r) { return r.json(); }),
        fetch(API_BASE + '/api/dashboard/attendance-trend', {
          headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }).then(function (r) { return r.json(); })
      ]).then(function (results) {
        var statsRes = results[0];
        var classRes = results[1];
        var trendRes = results[2];

        // 更新统计卡片
        if (statsRes && statsRes.code === 200 && statsRes.data) {
          self.stats.studentCount = statsRes.data.studentCount || 0;
          self.stats.classCount = statsRes.data.classCount || 0;
          self.stats.todayAttendanceRate = statsRes.data.todayAttendanceRate || 0;
          self.stats.pendingHomeworkCount = statsRes.data.pendingHomeworkCount || 0;
        }

        // 更新班级分布
        if (classRes && classRes.code === 200 && classRes.data) {
          self.classData = classRes.data.map(function (item) {
            return { name: item.className, count: item.studentCount };
          });
        }

        // 更新出勤趋势
        if (trendRes && trendRes.code === 200 && trendRes.data) {
          self.attendanceTrend = trendRes.data.map(function (item) {
            return { date: item.date, rate: item.rate };
          });
        }

        // 渲染图表
        self.$nextTick(function () {
          self.initClassChart();
          self.initAttendanceChart();
        });
      }).catch(function () {
        console.error('Dashboard API request failed');
      }).finally(function () {
        self.loading = false;
      });

      window.addEventListener('resize', self._handleResize);
    },

    beforeUnmount: function () {
      window.removeEventListener('resize', this._handleResize);
      if (this.classChartInstance) {
        this.classChartInstance.dispose();
        this.classChartInstance = null;
      }
      if (this.attendanceChartInstance) {
        this.attendanceChartInstance.dispose();
        this.attendanceChartInstance = null;
      }
    },

    methods: {
      _handleResize: function () {
        if (this.classChartInstance) {
          this.classChartInstance.resize();
        }
        if (this.attendanceChartInstance) {
          this.attendanceChartInstance.resize();
        }
      },

      initClassChart: function () {
        var dom = this.$refs.classChart;
        if (!dom || typeof echarts === 'undefined') return;
        if (this.classData.length === 0) return;

        var self = this;
        if (this.classChartInstance) {
          this.classChartInstance.dispose();
        }
        this.classChartInstance = echarts.init(dom);
        this.classChartInstance.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            top: '8%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: self.classData.map(function (d) { return d.name; }),
            axisLabel: {
              rotate: 15,
              fontSize: 12
            }
          },
          yAxis: {
            type: 'value',
            name: '学生人数',
            minInterval: 1
          },
          series: [{
            name: '学生人数',
            type: 'bar',
            data: self.classData.map(function (d) { return d.count; }),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#409eff' },
                { offset: 1, color: '#79bbff' }
              ]),
              borderRadius: [4, 4, 0, 0]
            },
            barWidth: '50%',
            label: {
              show: true,
              position: 'top',
              fontSize: 12,
              color: '#303133'
            }
          }]
        });
      },

      initAttendanceChart: function () {
        var dom = this.$refs.attendanceChart;
        if (!dom || typeof echarts === 'undefined') return;
        if (this.attendanceTrend.length === 0) return;

        var self = this;
        if (this.attendanceChartInstance) {
          this.attendanceChartInstance.dispose();
        }
        this.attendanceChartInstance = echarts.init(dom);
        this.attendanceChartInstance.setOption({
          tooltip: {
            trigger: 'axis',
            formatter: function (params) {
              return params[0].axisValue + '<br/>出勤率：' + params[0].value + '%';
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '8%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: self.attendanceTrend.map(function (d) { return d.date; }),
            axisLabel: { fontSize: 12 }
          },
          yAxis: {
            type: 'value',
            name: '出勤率(%)',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          series: [{
            name: '出勤率',
            type: 'line',
            data: self.attendanceTrend.map(function (d) { return d.rate; }),
            smooth: true,
            lineStyle: {
              color: '#67c23a',
              width: 3
            },
            itemStyle: {
              color: '#67c23a'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
              ])
            },
            symbol: 'circle',
            symbolSize: 8,
            label: {
              show: true,
              fontSize: 12,
              color: '#303133',
              formatter: '{c}%'
            }
          }]
        });
      }
    }
  };

  window.DashboardPage = DashboardPage;
})();
