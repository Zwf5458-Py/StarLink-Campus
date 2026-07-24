<template>
  <div class="apple-card-panel">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="全员工资管理" name="admin">
        <div class="header">
          <h2>工资管理</h2>
          <div class="actions">
            <el-button type="primary" @click="handleAdd">新增工资单</el-button>
            <el-button type="success" :disabled="selectedIds.length === 0" @click="handlePublishBatch">批量发布</el-button>
          </div>
        </div>
        <el-table :data="adminTable" v-loading="loading" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" :selectable="row => row.status === 'DRAFT'" />
          <el-table-column prop="empName" label="员工" />
          <el-table-column prop="month" label="月份" />
          <el-table-column prop="baseSalary" label="基本工资" />
          <el-table-column prop="bonus" label="奖金" />
          <el-table-column prop="allowance" label="补贴" />
          <el-table-column prop="deduction" label="扣款" />
          <el-table-column prop="insurance" label="社保" />
          <el-table-column prop="actualSalary" label="实发" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'PUBLISHED' ? 'success' : 'info'">{{ scope.row.status === 'PUBLISHED' ? '已发布' : '草稿' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button link type="primary" v-if="scope.row.status === 'DRAFT'" @click="handlePublish(scope.row)">发布</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="我的工资条" name="my">
        <el-table :data="myTable" v-loading="loading">
          <el-table-column prop="month" label="月份" />
          <el-table-column prop="baseSalary" label="基本工资" />
          <el-table-column prop="bonus" label="奖金" />
          <el-table-column prop="allowance" label="补贴" />
          <el-table-column prop="deduction" label="扣款" />
          <el-table-column prop="insurance" label="社保" />
          <el-table-column prop="actualSalary" label="实发" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 新增工资单对话框 -->
    <el-dialog v-model="dialogVisible" title="新增工资单" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="员工ID"><el-input v-model="form.staffId" /></el-form-item>
        <el-form-item label="发薪月份">
          <el-date-picker v-model="form.month" type="month" value-format="YYYY-MM" />
        </el-form-item>
        <el-form-item label="基本工资"><el-input-number v-model="form.baseSalary" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="奖金"><el-input-number v-model="form.bonus" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="补贴"><el-input-number v-model="form.allowance" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="扣款"><el-input-number v-model="form.deduction" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="社保"><el-input-number v-model="form.insurance" :min="0" :precision="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存为草稿</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('admin')
const loading = ref(false)
const adminTable = ref<any[]>([])
const myTable = ref([])
const selectedIds = ref<number[]>([])

const dialogVisible = ref(false)
const form = reactive({ staffId: '', month: '', baseSalary: 0, bonus: 0, allowance: 0, deduction: 0, insurance: 0 })

const getAdminList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/salary/admin/list')
    if (res.code === 200) adminTable.value = res.data.records || []
  } catch (e) { console.error(e) } finally { loading.value = false }
}

const getMyList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/salary/my-list')
    if (res.code === 200) myTable.value = res.data.records || []
  } catch (e) { console.error(e) } finally { loading.value = false }
}

const handleTabClick = () => {
  if (activeTab.value === 'admin') getAdminList()
  else getMyList()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleAdd = () => {
  Object.assign(form, { staffId: '', month: '', baseSalary: 0, bonus: 0, allowance: 0, deduction: 0, insurance: 0 })
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const res = await request.post('/kindergarten/salary/add', form)
    if (res.code === 200) {
      ElMessage.success('新增工资草稿成功')
      dialogVisible.value = false
      getAdminList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handlePublish = async (row: any) => {
  try {
    const res = await request.put(`/kindergarten/salary/publish/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('发布成功')
      getAdminList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handlePublishBatch = async () => {
  if (selectedIds.value.length === 0) return
  try {
    const res = await request.put('/kindergarten/salary/batch-publish', selectedIds.value)
    if (res.code === 200) {
      ElMessage.success('批量发布成功')
      selectedIds.value = []
      getAdminList()
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => { getAdminList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
