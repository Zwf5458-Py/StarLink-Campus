<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>周计划</h2>
      <div class="actions">
        <el-select v-model="queryParams.classId" placeholder="选择班级" clearable>
          <el-option label="小一班 (ID:1)" value="1" />
          <el-option label="小二班 (ID:2)" value="2" />
        </el-select>
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增周计划</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="className" label="班级" />
      <el-table-column prop="weekStart" label="周起始日" />
      <el-table-column prop="theme" label="主题" />
      <el-table-column prop="goals" label="目标" show-overflow-tooltip />
      <el-table-column prop="parentCoop" label="家长配合" show-overflow-tooltip />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'PUBLISHED' ? 'success' : 'info'">{{ scope.row.status === 'PUBLISHED' ? '已发布' : '草稿' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="success" v-if="scope.row.status === 'DRAFT'" @click="handlePublish(scope.row)">发布</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑周计划' : '新增周计划'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="班级">
          <el-select v-model="form.classId">
            <el-option label="小一班" :value="1" />
            <el-option label="小二班" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="周起始日">
          <el-date-picker v-model="form.weekStart" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="主题"><el-input v-model="form.theme" /></el-form-item>
        <el-form-item label="教学目标"><el-input v-model="form.goals" type="textarea" /></el-form-item>
        <el-form-item label="详细内容"><el-input v-model="form.content" type="textarea" /></el-form-item>
        <el-form-item label="家长配合"><el-input v-model="form.parentCoop" type="textarea" /></el-form-item>
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
const tableData = ref<any[]>([])
const queryParams = reactive({ classId: '', pageNum: 1, pageSize: 20 })

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, classId: 1, className: '小一班', weekStart: '', theme: '', goals: '', content: '', parentCoop: '', status: 'DRAFT' })

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

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, classId: 1, className: '小一班', weekStart: '', theme: '', goals: '', content: '', parentCoop: '', status: 'DRAFT' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    form.className = form.classId === 1 ? '小一班' : '小二班'
    const url = isEdit.value ? '/kindergarten/plan/update' : '/kindergarten/plan/add'
    const method = isEdit.value ? 'put' : 'post'
    const res = await request[method](url, form)
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handlePublish = async (row: any) => {
  try {
    const res = await request.put(`/kindergarten/plan/publish/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('发布成功')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => { getList() })
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
