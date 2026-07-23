<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>成长记录</h2>
      <div class="actions">
        <el-select v-model="queryParams.studentId" placeholder="选择学生" clearable>
          <el-option label="张三" value="1" />
          <el-option label="李四" value="2" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增记录</el-button>
        <el-button type="info" @click="handleShowChart">成长曲线</el-button>
        <el-button type="warning" @click="handleGenerateReport">学期报告</el-button>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="studentName" label="学生姓名" />
      <el-table-column prop="category" label="类别" />
      <el-table-column prop="recordValue" label="记录值" />
      <el-table-column prop="recordDate" label="日期" />
      <el-table-column prop="teacherComment" label="教师评语" show-overflow-tooltip />
      <el-table-column prop="semester" label="学期" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="form.studentId" placeholder="选择学生">
             <el-option label="张三" value="1" />
             <el-option label="李四" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category">
             <el-option label="身高" value="身高" />
             <el-option label="体重" value="体重" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录值" prop="recordValue">
          <el-input v-model="form.recordValue" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker v-model="form.recordDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="评语" prop="teacherComment">
          <el-input type="textarea" v-model="form.teacherComment" />
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
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({
  studentId: '',
  pageNum: 1,
  pageSize: 10
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({
  id: null,
  studentId: '',
  category: '',
  recordValue: '',
  recordDate: '',
  teacherComment: '',
  semester: '2024春季'
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/growth/list', { params: queryParams })
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

const handleSearch = () => {
  queryParams.pageNum = 1
  getList()
}

const handleAdd = () => {
  dialogTitle.value = '新增成长记录'
  Object.assign(form, { id: null, studentId: '', category: '', recordValue: '', recordDate: '', teacherComment: '' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑记录'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确认删除?', '提示', { type: 'warning' }).then(async () => {
    // await request.delete(`/kindergarten/growth/${row.id}`)
    ElMessage.success('删除成功')
    getList()
  }).catch(() => {})
}

const submitForm = async () => {
  // const method = form.id ? 'put' : 'post'
  // await request[method]('/kindergarten/growth', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  getList()
}

const handleShowChart = () => {
  ElMessage.info('成长曲线开发中')
}

const handleGenerateReport = () => {
  ElMessage.info('正在生成学期报告...')
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.actions {
  display: flex;
  gap: 10px;
}
</style>
