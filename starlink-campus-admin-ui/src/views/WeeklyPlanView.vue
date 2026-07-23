<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>周计划</h2>
      <div class="actions">
        <el-select v-model="queryParams.classId" placeholder="选择班级" clearable>
          <el-option label="小一班" value="1" />
        </el-select>
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增周计划</el-button>
        <el-button type="warning" @click="handlePublish">一键发布</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="className" label="班级" />
      <el-table-column prop="weekStart" label="周起始日" />
      <el-table-column prop="theme" label="主题" />
      <el-table-column prop="goals" label="目标" show-overflow-tooltip />
      <el-table-column prop="parentCoop" label="家长配合" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const queryParams = reactive({ classId: '', pageNum: 1, pageSize: 20 })

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/plan/list', { params: queryParams })
    if (res.code === 200) tableData.value = res.data.records || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => ElMessage.info('新增')
const handleEdit = (row: any) => ElMessage.info('编辑')
const handlePublish = () => ElMessage.success('发布成功')

onMounted(() => { getList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
