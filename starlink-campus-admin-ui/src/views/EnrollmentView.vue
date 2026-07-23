<template>
  <div class="apple-card-panel">
    <div class="funnel-stats">
      <el-card shadow="hover"><h3>总咨询: 120</h3></el-card>
      <el-card shadow="hover"><h3>参观: 80</h3></el-card>
      <el-card shadow="hover"><h3>体验: 50</h3></el-card>
      <el-card shadow="hover"><h3>报名: 30</h3></el-card>
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
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button link type="primary" @click="updateStatus(scope.row)">更新状态</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])

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

const handleAdd = () => ElMessage.info('新增意向')
const updateStatus = (row: any) => ElMessage.info('更新状态')

onMounted(() => { getList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.funnel-stats { display: flex; gap: 20px; margin-bottom: 20px; }
.funnel-stats .el-card { flex: 1; text-align: center; }
</style>
