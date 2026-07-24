<template>
  <div class="apple-card-panel">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="费用项管理" name="items">
        <div class="header">
          <h2>费用项</h2>
          <el-button type="primary" @click="handleAddFee">新增费用项</el-button>
        </div>
        <el-table :data="feeItems" v-loading="loading">
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="type" label="类型" />
          <el-table-column prop="amount" label="金额" />
          <el-table-column prop="deadline" label="截止日期" />
          <el-table-column prop="semester" label="学期" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button link type="primary" @click="handleEditFee(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="缴费记录" name="payments">
        <div class="header">
          <div class="stats-card">
            <span>总体收缴率：{{ collectionRate }}%</span>
          </div>
          <el-button type="success">导出明细</el-button>
        </div>
        <el-table :data="payments" v-loading="loading">
          <el-table-column prop="studentName" label="学生" />
          <el-table-column prop="itemName" label="费用项" />
          <el-table-column prop="amount" label="金额" />
          <el-table-column prop="channel" label="支付渠道" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'PAID' ? 'success' : 'danger'">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payTime" label="时间" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 新增/编辑费用项对话框 -->
    <el-dialog v-model="feeDialogVisible" :title="isEdit ? '编辑费用项' : '新增费用项'" width="500px">
      <el-form :model="feeForm" label-width="100px">
        <el-form-item label="名称"><el-input v-model="feeForm.name" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="feeForm.type">
            <el-option label="学费" value="TUITION" />
            <el-option label="伙食费" value="MEAL" />
            <el-option label="杂费" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额"><el-input-number v-model="feeForm.amount" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="feeForm.deadline" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="学期"><el-input v-model="feeForm.semester" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="feeForm.status">
            <el-option label="生效" value="ACTIVE" />
            <el-option label="关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="feeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFeeForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('items')
const loading = ref(false)
const feeItems = ref<any[]>([])
const payments = ref([])
const collectionRate = ref('0.00')

const feeDialogVisible = ref(false)
const isEdit = ref(false)
const feeForm = reactive({ id: null, name: '', type: 'TUITION', amount: 0, deadline: '', semester: '', status: 'ACTIVE' })

const getFeeItems = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/fee/item/list')
    if (res.code === 200) feeItems.value = res.data.records || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const getPayments = async () => {
  loading.value = true
  try {
    const res = await request.get('/kindergarten/fee/payment/list')
    if (res.code === 200) payments.value = res.data.records || []
    
    // 获取收缴率统计
    const feeId = feeItems.value.length > 0 ? feeItems.value[0].id : 1
    const statsRes = await request.get('/kindergarten/fee/stats', { params: { feeItemId: feeId } })
    if (statsRes.code === 200 && statsRes.data) {
      collectionRate.value = statsRes.data.collectionRate || '0.00'
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleTabClick = () => {
  if (activeTab.value === 'items') getFeeItems()
  else getPayments()
}

const handleAddFee = () => {
  isEdit.value = false
  Object.assign(feeForm, { id: null, name: '', type: 'TUITION', amount: 0, deadline: '', semester: '', status: 'ACTIVE' })
  feeDialogVisible.value = true
}

const handleEditFee = (row: any) => {
  isEdit.value = true
  Object.assign(feeForm, row)
  feeDialogVisible.value = true
}

const submitFeeForm = async () => {
  try {
    const url = isEdit.value ? '/kindergarten/fee/item/update' : '/kindergarten/fee/item/add'
    const res = await request.post(url, feeForm)
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      feeDialogVisible.value = false
      getFeeItems()
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  getFeeItems()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.stats-card {
  padding: 10px 20px;
  background: #f0f9eb;
  border-radius: 8px;
  color: #67c23a;
  font-weight: bold;
}
</style>
