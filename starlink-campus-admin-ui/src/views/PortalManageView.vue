<template>
  <div class="apple-portal-manage">
    <div class="apple-card-panel">
      <div class="panel-header">
        <div class="panel-title"><span>🌐 模块 5: 校园门户网站/微官网 CMS</span></div>
        <button class="apple-btn-success" @click="showAddDialog = true">+ 发布微官网新闻</button>
      </div>

      <el-tabs v-model="activeTab">
        <!-- 微官网新闻发布 -->
        <el-tab-pane label="微官网新闻与通告" name="news">
          <el-table :data="newsList" v-loading="loading" class="apple-table" style="width: 100%">
            <el-table-column prop="title" label="新闻/动态标题" min-width="240" />
            <el-table-column prop="category" label="所属栏目" width="130" />
            <el-table-column prop="author" label="发布人" width="120" />
            <el-table-column prop="views" label="浏览量" width="110" />
            <el-table-column prop="publishTime" label="发布时间" width="180">
              <template #default="scope">
                {{ formatTime(scope.row.publishTime || scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 发布新闻弹窗 -->
    <el-dialog v-model="showAddDialog" title="发布微官网新闻" width="600px" custom-class="apple-dialog">
      <el-form ref="articleFormRef" :model="articleForm" :rules="rules" label-width="100px">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="articleForm.title" placeholder="请输入新闻标题"></el-input>
        </el-form-item>
        <el-form-item label="所属栏目">
          <el-select v-model="articleForm.category" placeholder="请选择栏目" style="width: 100%">
            <el-option label="招生公告" value="招生公告" />
            <el-option label="园所动态" value="园所动态" />
            <el-option label="荣誉成果" value="荣誉成果" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布人">
          <el-input v-model="articleForm.author" placeholder="请输入发布人姓名"></el-input>
        </el-form-item>
        <el-form-item label="文章内容" prop="content">
          <el-input type="textarea" :rows="6" v-model="articleForm.content" placeholder="请输入文章正文内容..."></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取 消</el-button>
          <el-button type="primary" @click="submitArticle" :loading="submitLoading">发 布</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getArticleList, addArticle } from '@/api/article';
import { ElMessage } from 'element-plus';

const activeTab = ref('news');
const loading = ref(false);
const showAddDialog = ref(false);
const submitLoading = ref(false);
const articleFormRef = ref(null);

const articleForm = ref({
  title: '',
  category: '园所动态',
  author: '园长室',
  content: ''
});

const rules = {
  title: [{ required: true, message: '请输入新闻标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
};

const formatTime = (timeStr) => {
  if (!timeStr) return '';
  return timeStr.replace('T', ' ').substring(0, 16);
};

const defaultNews = [
  { id: 1, title: '海星幼儿园 2026 年秋季招生简章与开放日预约通知', category: '招生资讯', author: '园长室', views: 1280, publishTime: '2026-07-20' },
  { id: 2, title: '关于表彰 2026 年度海星幼儿园“优秀教师”与“优秀班主任”的决定', category: '园所动态', author: '行政办', views: 850, publishTime: '2026-07-18' },
  { id: 3, title: '海星幼儿园夏季传染病预防与幼儿营养膳食避险指南', category: '健康科普', author: '医务室', views: 620, publishTime: '2026-07-15' }
];

const newsList = ref(defaultNews);

const fetchArticles = async () => {
  loading.value = true;
  try {
    const res = await getArticleList();
    if (res && res.data && res.data.length > 0) {
      newsList.value = res.data;
    }
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const submitArticle = () => {
  articleFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.warning('请检查必填项');
      return;
    }
    submitLoading.value = true;
    try {
      await addArticle(articleForm.value);
      ElMessage.success('发布成功');
      showAddDialog.value = false;
      articleFormRef.value.resetFields();
      fetchArticles();
    } catch (err) {
      ElMessage.error('发布请求异常');
    } finally {
      submitLoading.value = false;
    }
  });
};

onMounted(() => {
  fetchArticles();
});
</script>

<style scoped>
.apple-card-panel {
  background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 1); border-radius: 16px; padding: 20px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.panel-title { font-size: 17px; font-weight: 700; color: #0f172a; }
.apple-btn-success { background: #34c759; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.apple-btn-success:hover { background: #28a745; transform: scale(1.02); }
.apple-badge.success { background: rgba(52, 199, 89, 0.15); color: #248a3d; padding: 4px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; }
:deep(.apple-dialog) { border-radius: 16px; overflow: hidden; }
</style>
