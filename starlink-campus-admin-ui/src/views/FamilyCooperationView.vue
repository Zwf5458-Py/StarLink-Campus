<template>
  <div class="apple-family-cooperation">
    <div class="apple-card-panel">
      <div class="panel-header">
        <div class="panel-title">
          <span class="icon">🌟</span>
          <span>家园共育平台 (家长微信端互联)</span>
        </div>
        <button class="apple-btn-primary" @click="dialogPost = true">+ 发起班级圈动态</button>
      </div>

      <el-tabs v-model="activeTab">
        <!-- 模块 12: 班级圈照片与视频动态 -->
        <el-tab-pane label="📷 班级圈动态 (照片/视频)" name="circle">
          <div class="circle-posts-grid">
            <div v-for="post in circlePosts" :key="post.id" class="post-card">
              <div class="post-header">
                <span class="avatar">👩‍🏫</span>
                <div class="post-meta">
                  <span class="author">{{ post.author }}</span>
                  <span class="time">{{ post.time }} · {{ post.className }}</span>
                </div>
              </div>
              <p class="post-text">{{ post.content }}</p>
              <div v-if="post.photoUrls" style="margin-bottom: 10px;">
                <el-image style="width: 100px; height: 100px; border-radius: 8px; margin-right: 5px;" 
                          v-for="(url, idx) in post.photoUrls.split(',')" :key="idx" 
                          :src="url" :preview-src-list="post.photoUrls.split(',')" fit="cover" />
              </div>
              <div class="post-actions">
                <button class="action-like-btn" @click="handleLike(post)">❤️ {{ post.likesCount || 0 }} 点赞</button>
                <span>💬 {{ post.comments }} 评论</span>
                <span class="status-clean">🟢 内容经 AI 敏感词过滤合规</span>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 模块 13: 隐私通讯录 -->
        <el-tab-pane label="📖 园区加密通讯录" name="contacts">
          <el-table :data="contacts" class="apple-table" style="width: 100%">
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="role" label="身份/关系" width="140" />
            <el-table-column prop="className" label="关联班级" width="160" />
            <el-table-column prop="phone" label="联系电话 (隐私加密)" width="180" />
            <el-table-column label="一键拨号" width="140">
              <template #default="scope">
                <button class="action-btn pass" @click="handleCall(scope.row)">📞 虚拟加密呼叫</button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 模块 14: 定向通知公告 -->
        <el-tab-pane label="📢 定向通知与家长签收" name="notice">
          <el-table :data="notices" class="apple-table" style="width: 100%">
            <el-table-column prop="title" label="通知标题" min-width="240" />
            <el-table-column prop="target" label="定向范围" width="140" />
            <el-table-column prop="publishTime" label="发送时间" width="160" />
            <el-table-column prop="stats" label="已读签收状态" width="160">
              <template #default="scope">
                <span class="read-bold">已签收 {{ scope.row.readCount }}/{{ scope.row.totalCount }} 人</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="scope">
                <button class="action-btn pass" @click="handleRemind(scope.row)">微信模板一键催读</button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 家长端：孩子月度成长与体温膳食档案 -->
        <el-tab-pane label="👦 幼儿月度成长与体温膳食档案" name="growth">
          <div class="growth-card-grid">
            <div class="growth-metric-box blue">
              <span class="g-lbl">月度出勤率</span>
              <span class="g-val blue-text">95.4%</span>
              <span class="g-sub">出勤 21 天 / 应到 22 天</span>
            </div>
            <div class="growth-metric-box green">
              <span class="g-lbl">晨检平均体温</span>
              <span class="g-val green-text">36.5 ℃</span>
              <span class="g-sub">全月体温正常无发热</span>
            </div>
            <div class="growth-metric-box yellow">
              <span class="g-lbl">膳食营养摄入评估</span>
              <span class="g-val yellow-text">良好 (特级)</span>
              <span class="g-sub">已完成全员过敏源避险</span>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 发起班级圈动态弹窗 -->
    <el-dialog v-model="dialogPost" title="发布班级圈动态" width="480px" custom-class="apple-dialog">
      <el-form ref="postFormRef" :model="postForm" :rules="postRules" label-width="80px">
        <el-form-item label="动态内容" prop="content">
          <el-input v-model="postForm.content" type="textarea" rows="4" placeholder="分享今天的幼儿园趣事..." />
        </el-form-item>
        <el-form-item label="关联班级">
          <el-input v-model="postForm.className" placeholder="如: 大(1)班" />
        </el-form-item>
        <el-form-item label="照片URL">
          <el-input v-model="postForm.photoUrls" placeholder="多张照片以逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="apple-btn-secondary" @click="dialogPost = false" style="margin-right: 10px;">取消</button>
        <button class="apple-btn-primary" @click="submitPost" :disabled="submitLoading">发布至班级圈</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getCircleList, publishCircle, likeCircle } from '@/api/classCircle';

const activeTab = ref('circle');
const dialogPost = ref(false);
const submitLoading = ref(false);
const postFormRef = ref(null);

const circlePosts = ref([]);

const postForm = ref({
  content: '',
  className: '',
  photoUrls: ''
});

const postRules = {
  content: [{ required: true, message: '请输入动态内容', trigger: 'blur' }]
};

const fetchData = async () => {
  try {
    const res = await getCircleList();
    circlePosts.value = res.data || [];
  } catch (error) {
    console.error('拉取班级圈动态失败', error);
  }
};

onMounted(() => {
  fetchData();
});

const submitPost = () => {
  postFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true;
      try {
        const payload = {
          content: postForm.value.content,
          className: postForm.value.className || '大(1)班',
          photoUrls: postForm.value.photoUrls,
          authorId: 201 // Mock authorId, in reality comes from login info
        };
        await publishCircle(payload);
        ElMessage.success('动态发布成功！');
        dialogPost.value = false;
        postFormRef.value.resetFields();
        fetchData();
      } catch (error) {
        ElMessage.error('发布失败');
      } finally {
        submitLoading.value = false;
      }
    }
  });
};

const handleLike = async (post) => {
  try {
    await likeCircle(post.id);
    post.likesCount = (post.likesCount || 0) + 1;
    ElMessage.success('点赞成功 ❤️！已同步至微信家园共育通知。');
  } catch (error) {
    ElMessage.error('点赞失败');
  }
};

const contacts = ref([
  { name: '李老师', role: '大班班主任', className: '大(1)班', phone: '138****8888' },
  { name: '张小明家长', role: '张小明 (父亲)', className: '大(1)班', phone: '139****9999' },
  { name: '王医生', role: '保健医师', className: '园区保健室', phone: '137****6666' }
]);

const notices = ref([
  { title: '关于大(1)班秋季户外体能拓展活动的家长告知书', target: '大(1)班全员家长', publishTime: '2026-07-22', readCount: 26, totalCount: 28 },
  { title: '关于开展秋季幼儿园手足口与传染病防控卫生的通知', target: '全园家长与教职工', publishTime: '2026-07-20', readCount: 390, totalCount: 400 }
]);

const handleCall = (row) => {
  ElMessage.success(`正在拉起安全虚拟拨号呼叫: ${row.name}`);
};

const handleRemind = (row) => {
  ElMessage.success(`已成功向未读家长（${row.totalCount - row.readCount}人）发送微信模板一键催读提醒！`);
};
</script>

<style scoped>
.apple-card-panel {
  background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 1); border-radius: 16px; padding: 20px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.panel-title { font-size: 17px; font-weight: 700; color: #0f172a; display: flex; align-items: center; gap: 8px; }
.circle-posts-grid { display: flex; flex-direction: column; gap: 12px; }
.post-card { background: rgba(0,0,0,0.02); border: 1px solid rgba(0,0,0,0.06); border-radius: 12px; padding: 14px; }
.post-header { display: flex; gap: 10px; align-items: center; margin-bottom: 8px; }
.avatar { font-size: 24px; }
.author { font-weight: 700; font-size: 14px; }
.time { font-size: 12px; color: #94a3b8; }
.post-text { font-size: 13px; color: #334155; line-height: 1.5; margin: 0 0 10px 0; }
.post-actions { display: flex; gap: 16px; font-size: 12px; color: #64748b; font-weight: 600; align-items: center; }
.action-like-btn { border: none; background: rgba(255,45,85,0.1); color: #ff2d55; padding: 4px 10px; border-radius: 8px; font-weight: bold; cursor: pointer; }
.status-clean { color: #34c759; margin-left: auto; }

.growth-card-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-top: 10px; }
.growth-metric-box { background: rgba(248, 250, 252, 0.8); border: 1px solid rgba(0,0,0,0.06); border-radius: 14px; padding: 18px; display: flex; flex-direction: column; gap: 6px; }
.growth-metric-box.blue { border-left: 4px solid #0071e3; }
.growth-metric-box.green { border-left: 4px solid #34c759; }
.growth-metric-box.yellow { border-left: 4px solid #ffbd2e; }

.g-lbl { font-size: 12px; font-weight: bold; color: #64748b; }
.g-val { font-size: 26px; font-weight: bold; font-family: monospace; }
.blue-text { color: #0071e3; }
.green-text { color: #34c759; }
.yellow-text { color: #d97706; }
.g-sub { font-size: 11px; color: #94a3b8; }

.apple-btn-primary { background: #0071e3; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; }
.action-btn.pass { background: #34c759; color: #fff; border: none; padding: 5px 10px; border-radius: 8px; font-size: 12px; font-weight: 600; cursor: pointer; }
.read-bold { font-weight: 700; color: #0071e3; }
</style>
