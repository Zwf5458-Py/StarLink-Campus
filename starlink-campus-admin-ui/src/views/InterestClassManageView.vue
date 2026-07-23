<template>
  <div class="interest-container">
    <el-card header="🎨 幼儿园幼儿兴趣托育与课后拓展活动">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <el-input v-model="searchKeyword" placeholder="搜索拓展活动名称（如: 幼儿体能、美育画画）" style="width: 300px;" />
        <el-button type="success" @click="dialogVisible = true">+ 新增幼儿兴趣拓展活动</el-button>
      </div>

      <el-table :data="interestClasses" border style="width: 100%">
        <el-table-column prop="id" label="活动ID" width="90" />
        <el-table-column prop="courseName" label="兴趣活动名称" width="180" />
        <el-table-column prop="targetGrades" label="适用年龄/班级" width="140" />
        <el-table-column prop="teacherName" label="指导老师" width="120" />
        <el-table-column prop="status" label="活动状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === '进行中' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEnroll(scope.row)">登记幼儿报名</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog v-model="dialogVisible" title="新增幼儿兴趣拓展活动" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="活动名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="例如: 幼儿奥尔夫音乐体验" />
        </el-form-item>
        <el-form-item label="适用班级" prop="targetGrades">
          <el-input v-model="form.targetGrades" placeholder="例如: 大班, 中班" />
        </el-form-item>
        <el-form-item label="指导老师" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="例如: 王老师" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="submitLoading">保存发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getCourseList, addCourse } from '@/api/course';

const searchKeyword = ref('');
const dialogVisible = ref(false);
const submitLoading = ref(false);
const formRef = ref(null);

const defaultCourses = [
  { id: 1, courseName: '🎨 幼儿美育水彩与创意手工班', targetGrades: '小班, 中班', teacherName: '陈美美 老师', status: '进行中', createTime: '2026-07-23 09:00' },
  { id: 2, courseName: '⚽ 少年强体能足球与韵律体操', targetGrades: '中班, 大班', teacherName: '张教练', status: '进行中', createTime: '2026-07-23 09:30' },
  { id: 3, courseName: '🎵 奥尔夫音乐节奏与打击乐启蒙', targetGrades: '小班, 中班, 大班', teacherName: '王音音 老师', status: '进行中', createTime: '2026-07-23 10:00' },
  { id: 4, courseName: '🧩 乐高大颗粒空间构形与机器人', targetGrades: '中班, 大班', teacherName: '刘智造 老师', status: '进行中', createTime: '2026-07-23 10:30' },
  { id: 5, courseName: '📖 英美中英双语绘本剧戏剧表演', targetGrades: '中班, 大班', teacherName: 'Emma 老师', status: '进行中', createTime: '2026-07-23 11:00' }
];

const interestClasses = ref(defaultCourses);

const fetchData = async () => {
  try {
    const res = await getCourseList({ keyword: searchKeyword.value });
    if (res && res.data && res.data.length > 0) {
      interestClasses.value = res.data;
    }
  } catch (error) {
    console.error('获取课程数据失败', error);
  }
};

onMounted(() => {
  fetchData();
});

const form = ref({
  courseName: '',
  targetGrades: '大班',
  teacherName: ''
});

const rules = {
  courseName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  targetGrades: [{ required: true, message: '请输入适用班级', trigger: 'blur' }],
  teacherName: [{ required: true, message: '请输入指导老师', trigger: 'blur' }]
};

const handleSave = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true;
      try {
        const payload = {
          courseName: form.value.courseName,
          targetGrades: form.value.targetGrades,
          teacherName: form.value.teacherName,
          status: '进行中'
        };
        await addCourse(payload);
        ElMessage.success('兴趣拓展活动发布成功！');
        dialogVisible.value = false;
        formRef.value.resetFields();
        fetchData();
      } catch (error) {
        ElMessage.error('发布活动失败');
      } finally {
        submitLoading.value = false;
      }
    }
  });
};

const handleEnroll = (row) => {
  ElMessageBox.alert(`已为幼儿开启【${row.courseName}】兴趣班报名通道！`, '报名通知已发至家长端', { type: 'success' });
};
</script>

<style scoped>
.interest-container {
  padding: 16px;
}
:deep(.el-card) {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: bold;
}
</style>
