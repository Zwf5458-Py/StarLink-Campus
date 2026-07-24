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
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status === 'PUBLISHED' ? '已发布' : (scope.row.status === 'CLOSED' ? '已关闭' : '草稿') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="participants" label="参与人数" />
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <el-button link type="primary" @click="manageQuestions(scope.row)">题目管理</el-button>
          <el-button link type="success" @click="viewStats(scope.row)">统计结果</el-button>
          <el-button link type="warning" @click="toggleStatus(scope.row)">发布/关闭</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增问卷对话框 -->
    <el-dialog v-model="dialogVisible" title="新增问卷" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type">
            <el-option label="满意度调查" value="满意度" />
            <el-option label="活动反馈" value="活动" />
            <el-option label="家园共育" value="家园" />
          </el-select>
        </el-form-item>
        <el-form-item label="范围">
          <el-select v-model="form.scope">
            <el-option label="全园" value="ALL" />
            <el-option label="大班" value="GRADE_3" />
            <el-option label="中班" value="GRADE_2" />
            <el-option label="小班" value="GRADE_1" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
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

const dialogVisible = ref(false)
const form = reactive({ title: '', description: '', type: '满意度', scope: 'ALL', startTime: '', endTime: '' })

const getList = async () => {
  loading.value = true
  try {
    // 修复无参数调用导致后端 400 异常的问题
    const res = await request.get('/kindergarten/survey/list', { params: { pageNum: 1, pageSize: 10 } })
    if (res.code === 200) tableData.value = res.data.records || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(form, { title: '', description: '', type: '满意度', scope: 'ALL', startTime: '', endTime: '' })
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const res = await request.post('/kindergarten/survey/add', form)
    if (res.code === 200) {
      ElMessage.success('新增问卷成功')
      dialogVisible.value = false
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const toggleStatus = async (row: any) => {
  try {
    const url = row.status === 'PUBLISHED' ? `/kindergarten/survey/close/${row.id}` : `/kindergarten/survey/publish/${row.id}`
    const res = await request.post(url)
    if (res.code === 200) {
      ElMessage.success('操作成功')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const manageQuestions = (row: any) => ElMessage.info('题目管理功能开发中')
const viewStats = (row: any) => ElMessage.info('统计结果功能开发中')

const getStatusType = (status: string) => {
  switch (status) {
    case 'PUBLISHED': return 'success'
    case 'CLOSED': return 'info'
    default: return 'warning'
  }
}

onMounted(() => { getList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
</style>
