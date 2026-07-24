<template>
  <div class="apple-card-panel">
    <div class="funnel-stats">
      <el-card shadow="hover"><h3>总咨询: {{ funnelStats.TOTAL_INQUIRY || 0 }}</h3></el-card>
      <el-card shadow="hover"><h3>参观: {{ funnelStats.CAMPUS_TOUR || 0 }}</h3></el-card>
      <el-card shadow="hover"><h3>体验: {{ funnelStats.TRIAL_CLASS || 0 }}</h3></el-card>
      <el-card shadow="hover"><h3>报名: {{ funnelStats.ENROLLED || 0 }}</h3></el-card>
    </div>

    <div class="header">
      <h2>招生管理</h2>
      <el-button type="primary" @click="handleAdd">新增意向</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="childName" label="幼儿姓名" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="parentName" label="家长" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="intendedClass" label="意向班级" />
      <el-table-column prop="source" label="来源" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="handleUpdateStatus(scope.row)">更新状态</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="新增招生意向" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="幼儿姓名"><el-input v-model="form.childName" /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="家长姓名"><el-input v-model="form.parentName" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="意向班级"><el-input v-model="form.intendedClass" /></el-form-item>
        <el-form-item label="渠道来源"><el-input v-model="form.source" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 状态更新对话框 -->
    <el-dialog v-model="statusDialogVisible" title="更新状态" width="400px">
      <el-form :model="statusForm">
        <el-form-item label="当前状态">
          <el-select v-model="statusForm.status">
            <el-option label="咨询 (TOTAL_INQUIRY)" value="TOTAL_INQUIRY" />
            <el-option label="参观 (CAMPUS_TOUR)" value="CAMPUS_TOUR" />
            <el-option label="体验 (TRIAL_CLASS)" value="TRIAL_CLASS" />
            <el-option label="已报名 (ENROLLED)" value="ENROLLED" />
            <el-option label="流失 (LOST)" value="LOST" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStatus">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const funnelStats = ref<any>({})

const dialogVisible = ref(false)
const form = reactive({ childName: '', gender: '', parentName: '', phone: '', intendedClass: '', source: '', status: 'TOTAL_INQUIRY' })

const statusDialogVisible = ref(false)
const statusForm = reactive({ id: null, status: '' })

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/enrollment/list')
    if (res.code === 200) tableData.value = res.data.records || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const getFunnelStats = async () => {
  try {
    const res = await request.get('/kindergarten/enrollment/funnel-stats')
    if (res.code === 200) funnelStats.value = res.data || {}
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  Object.assign(form, { childName: '', gender: '男', parentName: '', phone: '', intendedClass: '', source: '', status: 'TOTAL_INQUIRY' })
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const res = await request.post('/kindergarten/enrollment/add', form)
    if (res.code === 200) {
      ElMessage.success('新增意向成功')
      dialogVisible.value = false
      getList()
      getFunnelStats()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleUpdateStatus = (row: any) => {
  statusForm.id = row.id
  statusForm.status = row.status
  statusDialogVisible.value = true
}

const submitStatus = async () => {
  try {
    const res = await request.put(`/kindergarten/enrollment/status/${statusForm.id}?status=${statusForm.status}`)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      statusDialogVisible.value = false
      getList()
      getFunnelStats()
    }
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'ENROLLED': return 'success'
    case 'LOST': return 'info'
    case 'TRIAL_CLASS': return 'warning'
    default: return ''
  }
}

onMounted(() => {
  getList()
  getFunnelStats()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.funnel-stats { display: flex; gap: 20px; margin-bottom: 20px; }
.funnel-stats .el-card { flex: 1; text-align: center; border-radius: 12px; }
.funnel-stats h3 { margin: 0; color: var(--el-color-primary); }
</style>
