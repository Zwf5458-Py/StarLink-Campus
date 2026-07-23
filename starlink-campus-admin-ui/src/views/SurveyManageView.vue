<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>问卷管理</h2>
      <el-button type="primary" @click="handleAdd">新增问卷</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="scope" label="范围" />
      <el-table-column prop="startTime" label="开始时间" />
      <el-table-column prop="endTime" label="结束时间" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="participants" label="参与人数" />
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <el-button link type="primary" @click="manageQuestions(scope.row)">题目管理</el-button>
          <el-button link type="success" @click="viewStats(scope.row)">统计结果</el-button>
          <el-button link type="warning" @click="toggleStatus(scope.row)">发布/关闭</el-button>
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
    const res = await request.get('/kindergarten/survey/list')
    if (res.code === 200) tableData.value = res.data.records || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => ElMessage.info('新增问卷')
const manageQuestions = (row: any) => ElMessage.info('题目管理')
const viewStats = (row: any) => ElMessage.info('统计结果')
const toggleStatus = (row: any) => ElMessage.success('操作成功')

onMounted(() => { getList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
</style>
