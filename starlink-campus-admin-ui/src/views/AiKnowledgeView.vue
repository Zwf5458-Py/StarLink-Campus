<template>
  <div class="apple-card-panel">
    <div class="header-action-bar">
      <h2>🤖 AI 园务知识库管理</h2>
      <el-button type="primary" class="glass-btn" @click="showAddDialog = true">+ 新增知识条目</el-button>
    </div>

    <el-table :data="knowledgeList" style="width: 100%" class="apple-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="知识标题" width="220" />
      <el-table-column prop="category" label="分类" width="140">
        <template #default="{ row }">
          <el-tag effect="dark" type="info">{{ row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="知识正文内容" show-overflow-tooltip />
      <el-table-column prop="tags" label="关键词标签" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '启用' ? 'success' : 'danger'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增弹窗 -->
    <el-dialog v-model="showAddDialog" title="新增 AI 园务知识库条目" width="550px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="知识标题">
          <el-input v-model="form.title" placeholder="如：缺勤退费与伙食费计算标准" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类">
            <el-option label="作息规则" value="作息规则" />
            <el-option label="请假退费" value="请假退费" />
            <el-option label="入园须知" value="入园须知" />
            <el-option label="缴费说明" value="缴费说明" />
            <el-option label="安全防护" value="安全防护" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词标签">
          <el-input v-model="form.tags" placeholder="逗号分隔，如：退费,请假,伙食费" />
        </el-form-item>
        <el-form-item label="知识正文">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入精准详细的规则说明，供 AI RAG 客服调用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const knowledgeList = ref([])
const showAddDialog = ref(false)

const form = reactive({
  title: '',
  category: '作息规则',
  tags: '',
  content: '',
  status: '启用'
})

const fetchKnowledge = async () => {
  try {
    const res = await request.get('/kindergarten/ai/knowledge/list')
    knowledgeList.value = res.data?.records || res.records || []
  } catch (err) {
    console.error('获取 AI 知识库失败', err)
  }
}

const submitAdd = async () => {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  try {
    await request.post('/kindergarten/ai/knowledge/add', form)
    ElMessage.success('新增知识库成功！')
    showAddDialog.value = false
    fetchKnowledge()
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定删除该知识条目吗？', '提示', { type: 'warning' })
    await request.delete(`/kindergarten/ai/knowledge/delete/${id}`)
    ElMessage.success('已删除')
    fetchKnowledge()
  } catch (err) {}
}

onMounted(() => {
  fetchKnowledge()
})
</script>

<style scoped>
.apple-card-panel {
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}
.header-action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
