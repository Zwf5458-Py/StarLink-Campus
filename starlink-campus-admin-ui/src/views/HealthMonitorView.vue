<template>
  <div class="health-container" v-loading="loading">
    <el-card header="🩺 全园晨检监控与每日食谱过敏源比对引擎">
      <div style="margin-bottom: 20px;">
        <h4>每周食谱过敏源比对</h4>
        <div style="display: flex; gap: 12px; align-items: center;">
          <el-input v-model="recipeInput" placeholder="输入今日食材（逗号分割，如: 花生, 牛奶, 鸡蛋）" style="width: 400px;" />
          <el-button type="warning" @click="handleCheckRecipe">运行过敏源比对引擎</el-button>
        </div>
      </div>

      <div style="margin-bottom: 20px;">
        <h4>晨检智能辅助视觉分析</h4>
        <div style="display: flex; gap: 12px; align-items: center; background: #fff8f1; padding: 16px; border-radius: 12px; border: 1px dashed #f59e0b;">
          <span style="font-size: 24px;">📷</span>
          <div style="flex: 1;">
            <div style="font-weight: 600; color: #b45309; margin-bottom: 4px;">接入阿里云视觉多模态大模型 (Qwen-VL-Max)，辅助识别手足口病（口腔疱疹、手掌红点）</div>
            <div style="font-size: 13px; color: #d97706; display: flex; align-items: center; gap: 10px; margin-top: 8px;">
              <span>影像URL:</span>
              <el-input v-model="visualImageUrl" placeholder="输入患儿手掌/口腔图片链接进行检测" style="width: 400px;" size="small" />
            </div>
          </div>
          <el-button type="primary" @click="handleAiVisualCheck" :loading="visualChecking">✨ 启动 AI 视觉分析</el-button>
        </div>
      </div>

      <el-divider />

      <h4>今日晨检体温表</h4>
      <el-table :data="morningChecks" border style="width: 100%">
        <el-table-column prop="studentId" label="学号" width="110" />
        <el-table-column prop="checkDate" label="检测日期" width="130" />
        <el-table-column prop="temperature" label="体温(℃)" width="120">
          <template #default="scope">
            <span :style="{ color: scope.row.temperature > 37.3 ? '#ef4444' : '#22c55e', fontWeight: 'bold' }">
              {{ scope.row.temperature }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="isFever" label="发热状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.isFever === 1 ? 'danger' : 'success'">
              {{ scope.row.isFever === 1 ? '发热预警' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="healthTags" label="异常标签" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMorningSummary, checkRecipeAllergies } from '@/api/health';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

const loading = ref(false);
const recipeInput = ref('花生, 牛奶, 鸡蛋');
const morningChecks = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const today = new Date().toISOString().substring(0, 10);
    const res = await getMorningSummary(today);
    morningChecks.value = res.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const handleCheckRecipe = async () => {
  if (!recipeInput.value.trim()) {
    ElMessage.warning('请输入食材');
    return;
  }
  try {
    const ingredients = recipeInput.value.split(',').map(s => s.trim());
    const res = await checkRecipeAllergies(ingredients);
    const alertList = res.data || [];
    if (alertList.length === 0) {
      ElMessageBox.alert('经过全园幼儿数据库过敏档案比对：今日食谱未触发任何过敏风险！', '比对完成', { type: 'success' });
    } else {
      ElMessageBox.alert(`⚠️ 发现 ${alertList.length} 位幼儿可能对今日食谱中的食材过敏，请特别注意！`, '过敏风险警报', { type: 'warning' });
    }
  } catch (error) {
    ElMessage.error('比对失败');
  }
};

const visualChecking = ref(false);
const visualImageUrl = ref('https://example.com/sample_hand_rash.jpg'); // 默认占位图

const handleAiVisualCheck = async () => {
  if (!visualImageUrl.value.trim()) {
    ElMessage.warning('请输入待检测的图片 URL');
    return;
  }
  visualChecking.value = true;
  try {
    const res = await request.post('/kindergarten/ai/health-analyze', null, {
      params: { imageUrl: visualImageUrl.value }
    });
    
    if (res.code === 200 && res.data) {
      const data = res.data;
      const warningHtml = data.hasWarning 
        ? `<p style="color: red; font-weight: bold;">⚠️ 警告：检测到该幼儿影像存在疑似异常，具体症状：${data.symptoms || '无描述'}</p>`
        : `<p style="color: green; font-weight: bold;">✅ AI 检查正常：未发现明显异常。</p>`;

      ElMessageBox.alert(`
        <div style="font-size: 14px; line-height: 1.6;">
          <p><b>AI 视觉识别分析报告：</b></p>
          <p>识别图片：<a href="${data.imageUrl}" target="_blank">查看原图</a></p>
          <p>置信度：${data.confidence || '未知'}</p>
          ${warningHtml}
          <p>结论建议：${data.conclusion || '无'}</p>
        </div>
      `, 'AI 多模态视觉分析完成', { dangerouslyUseHTMLString: true, type: data.hasWarning ? 'warning' : 'success' });
    } else {
      ElMessage.error(res.msg || 'AI 服务响应异常');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('调用 AI 服务失败，请稍后重试。');
  } finally {
    visualChecking.value = false;
  }
};
</script>

<style scoped>
.health-container {
  padding: 24px;
}
:deep(.el-card) {
  border-radius: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 700;
  font-size: 16px;
}
:deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}
</style>
