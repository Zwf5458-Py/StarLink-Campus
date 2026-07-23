<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>接送管理</h2>
      <div class="actions">
        <el-select v-model="queryParams.studentId" placeholder="选择学生" clearable>
          <el-option label="张三" value="1" />
        </el-select>
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
          {{ scope.row.isPrimary ? '主要' : '辅助' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" @click="handleApprove(scope.row)">审批</el-button>
          <el-button link type="warning" @click="handleDisable(scope.row)">停用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="getList"
    />
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
  ElMessage.info('新增接送人弹窗开发中')
}

const handleApprove = (row: any) => {
  ElMessage.success('审批成功')
}

const handleDisable = (row: any) => {
  ElMessage.success('已停用')
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
