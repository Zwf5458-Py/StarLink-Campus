<template>
  <div class="course-manage-container" v-loading="loading">
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header-bar">
          <div class="header-left">
            <span class="icon">📚</span>
            <span class="title">校本选课与选修活动管理平台 (参考师悦未来校园)</span>
          </div>
          <el-button type="primary" class="create-btn" @click="openAddDialog">+ 新建选课活动</el-button>
        </div>
      </template>

      <!-- 检索与筛选栏 -->
      <div class="filter-toolbar">
        <el-select v-model="term" style="width: 180px;">
          <el-option label="2022-2023 第二学期" value="2022-2023-2" />
          <el-option label="2023-2024 第一学期" value="2023-2024-1" />
        </el-select>

        <el-select v-model="selectedCampus" style="width: 140px;">
          <el-option label="校本部" value="校本部" />
          <el-option label="科技园分校区" value="科技园分校区" />
        </el-select>

        <el-input v-model="searchKeyword" placeholder="请输入活动名称" style="width: 220px;" clearable />
        <el-button type="primary" @click="fetchData">查询</el-button>
      </div>

      <!-- 数据大表 (参考师悦未来校园样式与丰富操作) -->
      <el-table :data="courseList" border stripe style="width: 100%" class="custom-table">
        <el-table-column prop="courseName" label="选修活动内容" min-width="200" />
        <el-table-column prop="campusName" label="所属校区" width="110" />
        <el-table-column prop="targetGrades" label="参加选修的年级" width="180" />
        <el-table-column prop="teacherSubmitTime" label="教师申报申报时间" width="170">
          <template #default="scope">
            <span>2023-04-20 13:48:17</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentSelectTime" label="学生选课时间" width="170">
          <template #default="scope">
            <span>2023-04-23 05:00:00</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="选课状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已开始' ? 'success' : scope.row.status === '未开始' ? 'warning' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作按钮组 (师悦未来经典矩阵)" width="320" fixed="right">
          <template #default="scope">
            <div class="action-grid">
              <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              <el-button link type="primary" size="small" @click="handleAction('远程选课')">远程选课</el-button>
              <el-button link type="primary" size="small" @click="handleAction('结束选课')">结束选课</el-button>
              <el-button link type="primary" size="small" @click="handleAction('复制选课')">复制选课</el-button>
              <el-button link type="primary" size="small" @click="handleAction('同步选课数据')">同步选课数据</el-button>
              <el-button link type="primary" size="small" @click="handleAction('编辑')">编辑</el-button>
              <el-button link type="primary" size="small" @click="handleAction('课程列表')">课程列表</el-button>
              <el-button link type="primary" size="small" @click="handleAction('统计报表')">统计报表</el-button>
              <el-button link type="primary" size="small" @click="handleAction('选果导入')">选果导入</el-button>
              <el-button link type="primary" size="small" @click="handleAction('导入选课信息')">导入选课信息</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建选课活动对话框 -->
    <el-dialog v-model="dialogVisible" title="新建校本选课活动" width="550px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="选修活动名称">
          <el-input v-model="form.courseName" placeholder="例如: 幼小衔接趣味数学思维" />
        </el-form-item>
        <el-form-item label="所属校区">
          <el-input v-model="form.campusName" />
        </el-form-item>
        <el-form-item label="参加年级">
          <el-input v-model="form.targetGrades" placeholder="例如: 大班, 中班" />
        </el-form-item>
        <el-form-item label="任课教师">
          <el-input v-model="form.teacherName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCourseList, addCourse, deleteCourse } from '@/api/course';
import { ElMessage, ElMessageBox } from 'element-plus';

const loading = ref(false);
const term = ref('2022-2023-2');
const selectedCampus = ref('校本部');
const searchKeyword = ref('');
const courseList = ref([]);
const dialogVisible = ref(false);

const form = ref({
  courseName: '',
  campusName: '校本部',
  targetGrades: '大班,中班',
  teacherName: '李老师',
  status: '已开始'
});

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getCourseList();
    courseList.value = res.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const openAddDialog = () => {
  dialogVisible.value = true;
};

const handleSave = async () => {
  if (!form.value.courseName) {
    ElMessage.warning('请输入选修活动名称');
    return;
  }
  try {
    await addCourse(form.value);
    ElMessage.success('选课活动新建成功');
    dialogVisible.value = false;
    fetchData();
  } catch (e) {
    ElMessage.error('保存失败');
  }
};

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除选修活动【${row.courseName}】吗？`, '删除确认', { type: 'warning' })
    .then(async () => {
      await deleteCourse(row.id);
      ElMessage.success('已成功删除');
      fetchData();
    });
};

const handleAction = (actionName) => {
  ElMessage.info(`【${actionName}】功能开发中...`);
};
</script>

<style scoped>
.card-header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  font-size: 16px;
  color: #1e293b;
}
.filter-toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  background: #f8fafc;
  padding: 12px;
  border-radius: 8px;
}
.action-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 4px 8px;
}
</style>
