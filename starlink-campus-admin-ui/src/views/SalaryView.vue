<template>
  <div class="apple-card-panel">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="全员工资管理" name="admin">
        <div class="header">
          <h2>工资管理</h2>
          <div class="actions">
            <el-button type="primary" @click="handleAdd">新增工资单</el-button>
            <el-button type="success" @click="handlePublishAll">批量发布</el-button>
          </div>
        </div>
        <el-table :data="adminTable" v-loading="loading">
          <el-table-column prop="empName" label="员工" />
          <el-table-column prop="month" label="月份" />
          <el-table-column prop="baseSalary" label="基本工资" />
          <el-table-column prop="bonus" label="奖金" />
          <el-table-column prop="allowance" label="补贴" />
          <el-table-column prop="deduction" label="扣款" />
          <el-table-column prop="insurance" label="社保" />
          <el-table-column prop="actualSalary" label="实发" />
          <el-table-column prop="status" label="状态" />
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('admin')
const loading = ref(false)
const adminTable = ref([])
const myTable = ref([])

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

const handleAdd = () => ElMessage.info('新增')
const handlePublishAll = () => ElMessage.success('发布成功')

onMounted(() => { getAdminList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
