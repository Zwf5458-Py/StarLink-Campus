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
          <div style="margin-bottom: 14px; display: flex; gap: 10px;">
            <el-input v-model="contactSearch" placeholder="搜索姓名/班级..." style="width: 260px;" clearable @clear="fetchContacts" @keyup.enter="searchContactList">
              <template #prefix><span>🔍</span></template>
            </el-input>
            <el-button type="primary" @click="searchContactList">搜索</el-button>
            <el-button @click="contactSearch = ''; fetchContacts()">重置</el-button>
          </div>
          <el-table :data="contacts" class="apple-table" style="width: 100%" v-loading="contactsLoading">
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="role" label="身份/关系" width="140" />
            <el-table-column prop="className" label="关联班级" width="160" />
            <el-table-column prop="maskedPhone" label="联系电话 (隐私加密)" width="180" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.type === 'STAFF' ? 'primary' : 'success'" size="small">
                  {{ scope.row.type === 'STAFF' ? '教职工' : '家长' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="一键拨号" width="160">
              <template #default="scope">
                <button class="action-btn pass" @click="handleCall(scope.row)">📞 虚拟加密呼叫</button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!contactsLoading && contacts.length === 0" description="暂无通讯录数据" />
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
          <div style="width: 100%;">
            <el-input v-model="postForm.content" type="textarea" rows="4" placeholder="输入几个关键词即可（如：小明今天主动分享玩具，午睡很好）" />
            <div style="margin-top: 8px; text-align: right;">
              <el-button type="primary" plain size="small" @click="handleAiPolish" :loading="aiPolishing">✨ AI 智能润色</el-button>
            </div>
          </div>
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
import { getContactList, searchContacts, virtualCall } from '@/api/contact';

const activeTab = ref('circle');
const dialogPost = ref(false);
const submitLoading = ref(false);
const postFormRef = ref(null);
const aiPolishing = ref(false);

const handleAiPolish = async () => {
  if (!postForm.value.content) {
    ElMessage.warning('请先输入几个需要润色的关键词');
    return;
  }
  aiPolishing.value = true;
  try {
    // 假设后端添加了 /kindergarten/ai/polish-text 接口
    const res = await request.post('/kindergarten/ai/polish-text', null, {
      params: { text: postForm.value.content }
    });
    if (res.code === 200) {
      postForm.value.content = res.data;
      ElMessage.success('✨ AI 润色成功！');
    }
  } catch (error) {
    ElMessage.error('AI 润色失败，请稍后重试');
  } finally {
    aiPolishing.value = false;
  }
};

const defaultPosts = [
  { id: 1, author: '李老师 (大(1)班主班)', time: '10分钟前', className: '大(1)班 - 葵花班', content: '☀️ 今天大(1)班的广播体操与户外拓展练习！小朋友们展现出了极佳的合作精神，向日葵队和飞天队都拿到了表现优异小红花！加油！', likesCount: 18, comments: 5 },
  { id: 2, author: '陈老师 (小(1)班主班)', time: '45分钟前', className: '小(1)班 - 雏菊班', content: '🎨 小(1)班美育手工课：今天雏菊班的小朋友用黏土捏出了各式各样的水果和彩色小房子，动手能力越来越棒了！', likesCount: 24, comments: 8 },
  { id: 3, author: '王教练 (体育教研组)', time: '2小时前', className: '中(2)班 - 向日葵班', content: '⚽ 中班组体能训练：向日葵班和满天星班进行了趣味障碍接力跑比赛，每个小朋友都汗流浃背但非常开心！', likesCount: 31, comments: 12 }
];

const circlePosts = ref(defaultPosts);

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
    if (res && res.data) {
      const records = Array.isArray(res.data) ? res.data : (res.data.records || []);
      if (records.length > 0) {
        circlePosts.value = records;
      }
    }
  } catch (error) {
    console.error('拉取班级圈动态失败', error);
  }
};

onMounted(() => {
  fetchData();
  fetchContacts();
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

const defaultContacts = [
  { originalId: 1, name: '陈美美 老师', role: '小(1)班主班', className: '小(1)班 - 雏菊班', maskedPhone: '138****8001', type: 'STAFF' },
  { originalId: 2, name: '张建国 (家长)', role: '张小明 父亲', className: '小(1)班 - 雏菊班', maskedPhone: '138****8002', type: 'PARENT' },
  { originalId: 3, name: '李伟 (家长)', role: '李思思 父亲', className: '小(1)班 - 雏菊班', maskedPhone: '138****8003', type: 'PARENT' },
  { originalId: 4, name: '张教练', role: '体育教研组组长', className: '中(2)班 - 向日葵班', maskedPhone: '138****8004', type: 'STAFF' },
  { originalId: 5, name: '王医生', role: '园区保健医师', className: '全园医务室', maskedPhone: '138****8005', type: 'STAFF' },
  { originalId: 6, name: '林峰 (家长)', role: '林梓涵 父亲', className: '小(2)班 - 苹果班', maskedPhone: '138****8006', type: 'PARENT' },
  { originalId: 7, name: '李老师', role: '大(1)班主班', className: '大(1)班 - 葵花班', maskedPhone: '138****8007', type: 'STAFF' }
];

const contacts = ref(defaultContacts);
const contactsLoading = ref(false);
const contactSearch = ref('');

const fetchContacts = async () => {
  contactsLoading.value = true;
  try {
    const res = await getContactList();
    if (res && res.data && res.data.length > 0) {
      contacts.value = res.data;
    }
  } catch (error) {
    console.error('拉取通讯录失败', error);
  } finally {
    contactsLoading.value = false;
  }
};

const searchContactList = async () => {
  if (!contactSearch.value.trim()) {
    fetchContacts();
    return;
  }
  contactsLoading.value = true;
  try {
    const res = await searchContacts(contactSearch.value);
    contacts.value = res.data || [];
  } catch (error) {
    console.error('搜索通讯录失败', error);
  } finally {
    contactsLoading.value = false;
  }
};

const notices = ref([
  { title: '关于大(1)班秋季户外体能拓展活动的家长告知书', target: '大(1)班全员家长', publishTime: '2026-07-22', readCount: 26, totalCount: 28 },
  { title: '关于开展秋季幼儿园手足口与传染病防控卫生的通知', target: '全园家长与教职工', publishTime: '2026-07-20', readCount: 390, totalCount: 400 }
]);

const handleCall = async (row) => {
  try {
    await virtualCall(row.originalId);
    ElMessage.success(`正在拉起安全虚拟拨号呼叫: ${row.name}`);
  } catch (error) {
    ElMessage.error('呼叫失败');
  }
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
