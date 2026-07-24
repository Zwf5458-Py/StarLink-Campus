<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>接送管理</h2>
      <div class="actions">
        <el-input v-model="queryParams.studentId" placeholder="学生ID" clearable style="width: 150px" />
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增接送人</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="studentName" label="学生姓名" />
      <el-table-column prop="pickupName" label="接送人" />
      <el-table-column prop="relation" label="关系" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="isPrimary" label="主要/辅助">
        <template #default="scope">
          <el-tag :type="scope.row.isPrimary ? 'primary' : 'info'">{{ scope.row.isPrimary ? '主要' : '辅助' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" v-if="scope.row.status === 'PENDING'" @click="handleApprove(scope.row)">审批</el-button>
          <el-button link type="warning" v-if="scope.row.status === 'ACTIVE'" @click="handleDisable(scope.row)">停用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="getList"
      style="margin-top: 20px; justify-content: flex-end;"
    />

    <!-- 新增接送人对话框 -->
    <el-dialog v-model="dialogVisible" title="新增接送人" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="学生ID"><el-input v-model="form.studentId" /></el-form-item>
        <el-form-item label="接送人姓名"><el-input v-model="form.pickupName" /></el-form-item>
        <el-form-item label="关系">
          <el-select v-model="form.relation">
            <el-option label="父亲" value="父亲" />
            <el-option label="母亲" value="母亲" />
            <el-option label="爷爷" value="爷爷" />
            <el-option label="奶奶" value="奶奶" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="类型">
          <el-switch v-model="form.isPrimary" active-text="主要" inactive-text="辅助" />
        </el-form-item>
        <el-form-item label="人脸识别特征"><el-input v-model="form.faceFeatureId" placeholder="特征ID" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
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
const total = ref(0)
const queryParams = reactive({ studentId: '', pageNum: 1, pageSize: 10 })

const dialogVisible = ref(false)
const form = reactive({ studentId: '', pickupName: '', relation: '父亲', phone: '', isPrimary: false, faceFeatureId: '' })

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/pickup/list', { params: queryParams })
    if (res.code === 200) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(form, { studentId: '', pickupName: '', relation: '父亲', phone: '', isPrimary: false, faceFeatureId: '' })
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const res = await request.post('/kindergarten/pickup/add', form)
    if (res.code === 200) {
      ElMessage.success('新增接送人成功')
      dialogVisible.value = false
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleApprove = async (row: any) => {
  try {
    const res = await request.post(`/kindergarten/pickup/approve/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('审批成功')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleDisable = async (row: any) => {
  try {
    const res = await request.post(`/kindergarten/pickup/disable/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('已停用')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'ACTIVE': return 'success'
    case 'PENDING': return 'warning'
    case 'DISABLED': return 'danger'
    default: return ''
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
