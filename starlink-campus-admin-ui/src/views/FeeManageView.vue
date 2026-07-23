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
          <el-table-column prop="status" label="状态" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button link type="primary">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="缴费记录" name="payments">
        <div class="header">
          <div class="stats-card">
            <span>总体收缴率：85%</span>
          </div>
          <el-button type="success">导出明细</el-button>
        </div>
        <el-table :data="payments" v-loading="loading">
          <el-table-column prop="studentName" label="学生" />
          <el-table-column prop="itemName" label="费用项" />
          <el-table-column prop="amount" label="金额" />
          <el-table-column prop="channel" label="支付渠道" />
          <el-table-column prop="status" label="状态" />
          <el-table-column prop="payTime" label="时间" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('items')
const loading = ref(false)
const feeItems = ref([])
const payments = ref([])

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
  ElMessage.info('开发中')
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
