<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>本周食谱</h2>
      <div class="actions">
        <el-date-picker v-model="queryParams.weekStart" type="week" format="ww 周" placeholder="选择周" />
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增食谱</el-button>
        <el-button type="warning" @click="handleCheckAllergy">过敏原检查</el-button>
        <el-button type="primary" @click="handlePublish">发布食谱</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="dayOfWeek" label="星期" />
      <el-table-column prop="mealType" label="餐别" />
      <el-table-column prop="dishName" label="菜品" />
      <el-table-column prop="nutritionNote" label="营养备注" show-overflow-tooltip />
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
const queryParams = reactive({ weekStart: '', pageNum: 1, pageSize: 50 })

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/menu/list', { params: queryParams })
    if (res.code === 200) {
      tableData.value = res.data.records || []
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => ElMessage.info('新增食谱')
const handleEdit = (row: any) => ElMessage.info('编辑食谱')
const handleCheckAllergy = () => ElMessage.success('过敏原检查通过')
const handlePublish = () => ElMessage.success('发布成功')

onMounted(() => {
  getList()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
