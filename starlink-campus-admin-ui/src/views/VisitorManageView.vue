<template>
  <div class="visitor-container" v-loading="loading">
    <el-card header="🎫 访客预约通行与超时滞留警报控制台">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <el-button type="danger" @click="handleCheckOvertime">🚨 一键扫描超时滞留访客</el-button>
        <el-button type="success" @click="dialogVisible = true">+ 新增访客登记</el-button>
        <el-button type="success" @click="handleExportVisitor">📥 导出访客记录</el-button>
      </div>

      <el-table :data="visitors" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="visitorName" label="访客姓名" width="120" />
        <el-table-column prop="visitorPhone" label="联系电话" width="140" />
        <el-table-column prop="visitReason" label="来访事由" />
        <el-table-column prop="passCode" label="通行二维码Code" width="180" />
        <el-table-column prop="status" label="通行状态" width="130">
          <template #default="scope">
            <el-tag :type="scope.row.status === '在园中' ? 'warning' : scope.row.status === '已离园' ? 'info' : 'success'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="overtimeAlerted" label="超时滞留警报" width="140">
          <template #default="scope">
            <el-tag v-if="scope.row.overtimeAlerted === 1" type="danger">🚨 超时滞留</el-tag>
            <span v-else style="color: #94a3b8; font-size: 13px;">正常</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建访客弹窗 -->
    <el-dialog v-model="dialogVisible" title="新增访客预约登记" width="500px">
      <el-form ref="visitorFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="访客姓名" prop="visitorName">
          <el-input v-model="form.visitorName" />
        </el-form-item>
        <el-form-item label="联系电话" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" />
        </el-form-item>
        <el-form-item label="来访事由" prop="visitReason">
          <el-input v-model="form.visitReason" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreatePass">生成通行二维码</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getVisitorList, createVisitorPass, checkOvertime } from '@/api/visitor';
import { ElMessage, ElMessageBox } from 'element-plus';

const loading = ref(false);
const dialogVisible = ref(false);
const visitorFormRef = ref(null);

const form = ref({
  visitorName: '',
  visitorPhone: '',
  visitReason: '商讨合作'
});

const rules = {
  visitorName: [{ required: true, message: '请输入访客姓名', trigger: 'blur' }],
  visitorPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  visitReason: [{ required: true, message: '请输入来访事由', trigger: 'blur' }]
};

const defaultVisitors = [
  { id: 101, visitorName: '张建军 (设备维护)', visitorPhone: '13811112222', visitReason: '定期巡检校园闸机与安防监控网络', passCode: 'PASS-20260723-881', status: '在园中', overtimeAlerted: 0 },
  { id: 102, visitorName: '王莉莉 (家长询园)', visitorPhone: '13833334444', visitReason: '参观小(1)班教室与食堂膳食环境', passCode: 'PASS-20260723-882', status: '已离园', overtimeAlerted: 0 },
  { id: 103, visitorName: '刘工 (消防检测)', visitorPhone: '13855556666', visitReason: '年度消防水压与感烟探测器例检', passCode: 'PASS-20260723-883', status: '在园中', overtimeAlerted: 1 }
];

const visitors = ref(defaultVisitors);

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getVisitorList();
    if (res && res.data && res.data.length > 0) {
      visitors.value = res.data;
    }
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const handleCreatePass = () => {
  visitorFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.warning('请检查必填项');
      return;
    }
  try {
    const res = await createVisitorPass(form.value);
    ElMessageBox.alert(`通行码生成成功！<br>Code: <b>${res.data}</b>`, '生成二维码', { dangerouslyUseHTMLString: true });
    dialogVisible.value = false;
    visitorFormRef.value.resetFields();
    fetchData();
  } catch (error) {
    ElMessage.error('生成失败');
  }
  });
};

const handleCheckOvertime = async () => {
  try {
    const res = await checkOvertime();
    const overList = res.data || [];
    if (overList.length === 0) {
      ElMessageBox.alert('扫描完成：目前园区内无任何超时滞留访客。', '安防扫描结果', { type: 'success' });
    } else {
      ElMessageBox.alert(`🚨 预警：检测到 ${overList.length} 位访客已超过预定停留时间 15 分钟以上未离园！`, '安防滞留红色告警', { type: 'warning' });
    }
  } catch (error) {
    ElMessage.error('扫描失败');
  }
};

const handleExportVisitor = () => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || '/api';
  window.open(`${baseUrl}/kindergarten/visitor/export`, '_blank');
  ElMessage.success('访客记录正在导出...');
};
</script>

<style scoped>
.visitor-container {
  padding: 24px;
}
:deep(.el-card) {
  border-radius: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 700;
  font-size: 16px;
}
:deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}
</style>
