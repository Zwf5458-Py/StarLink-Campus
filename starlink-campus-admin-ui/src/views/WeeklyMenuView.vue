<template>
  <div class="apple-card-panel">
    <div class="header">
      <h2>本周食谱</h2>
      <div class="actions">
        <el-date-picker v-model="queryParams.weekStart" type="week" format="ww 周" placeholder="选择周" />
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增食谱</el-button>
        <el-button type="warning" @click="handleCheckAllergy">过敏原检查</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="dayOfWeek" label="星期" />
      <el-table-column prop="mealType" label="餐别" />
      <el-table-column prop="dishName" label="菜品" />
      <el-table-column prop="nutritionNote" label="营养备注" show-overflow-tooltip />
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

    <!-- 新增/编辑食谱对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑食谱' : '新增食谱'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="星期">
          <el-select v-model="form.dayOfWeek">
            <el-option label="星期一" value="星期一" />
            <el-option label="星期二" value="星期二" />
            <el-option label="星期三" value="星期三" />
            <el-option label="星期四" value="星期四" />
            <el-option label="星期五" value="星期五" />
          </el-select>
        </el-form-item>
        <el-form-item label="餐别">
          <el-select v-model="form.mealType">
            <el-option label="早餐" value="早餐" />
            <el-option label="早点" value="早点" />
            <el-option label="午餐" value="午餐" />
            <el-option label="午点" value="午点" />
            <el-option label="晚餐" value="晚餐" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜品名称"><el-input v-model="form.dishName" /></el-form-item>
        <el-form-item label="主要食材"><el-input v-model="form.ingredients" /></el-form-item>
        <el-form-item label="营养备注"><el-input v-model="form.nutritionNote" /></el-form-item>
        <el-form-item label="可能过敏原"><el-input v-model="form.allergens" placeholder="如：花生、海鲜、鸡蛋等" /></el-form-item>
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
const tableData = ref<any[]>([])
const queryParams = reactive({ weekStart: '', pageNum: 1, pageSize: 50 })
const selectedRows = ref<any[]>([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, dayOfWeek: '星期一', mealType: '午餐', dishName: '', ingredients: '', nutritionNote: '', allergens: '', status: 'DRAFT' })

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

const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, dayOfWeek: '星期一', mealType: '午餐', dishName: '', ingredients: '', nutritionNote: '', allergens: '', status: 'DRAFT' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const url = isEdit.value ? '/kindergarten/menu/update' : '/kindergarten/menu/add'
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
    const res = await request.put(`/kindergarten/menu/publish/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('发布成功')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleCheckAllergy = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要检查过敏原的食谱项')
    return
  }
  const row = selectedRows.value[0]
  try {
    const res = await request.get('/kindergarten/menu/check-allergen', { params: { menuId: row.id, classId: 1 } })
    if (res.code === 200) {
      ElMessageBox.alert(res.data || '未检测到冲突，安全通过！', '过敏原检查结果', { confirmButtonText: '确定' })
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
.actions { display: flex; gap: 10px; }
</style>
