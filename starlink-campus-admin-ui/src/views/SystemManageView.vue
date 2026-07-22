<template>
  <div class="apple-system-manage">
    <div class="apple-card-panel">
      <div class="panel-header">
        <div class="panel-title"><span>🏛️ 模块 4: 智慧校园平台系统 (组织结构与角色权限)</span></div>
        <button class="apple-btn-primary">+ 新增角色/部门</button>
      </div>

      <el-tabs v-model="activeTab">
        <!-- 组织结构树 -->
        <el-tab-pane label="园区组织结构" name="dept">
          <el-tree :data="deptTree" node-key="id" default-expand-all :expand-on-click-node="false">
            <template #default="{ node, data }">
              <div class="tree-node-row">
                <span>{{ data.label }}</span>
                <span class="node-badge" v-if="data.userCount">{{ data.userCount }} 人</span>
              </div>
            </template>
          </el-tree>
        </el-tab-pane>

        <!-- 角色权限控制 -->
        <el-tab-pane label="角色权限配置" name="role">
          <el-table :data="roleList" class="apple-table" style="width: 100%">
            <el-table-column prop="roleName" label="角色名称" width="160" />
            <el-table-column prop="roleKey" label="权限标识" width="160" />
            <el-table-column prop="description" label="权限描述/范围" min-width="220" />
            <el-table-column label="状态" width="110">
              <template #default>
                <span class="apple-badge success">正常</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getRoleList } from '@/api/system';

const activeTab = ref('dept');

const roleList = ref([]);

const fetchData = async () => {
  try {
    const res = await getRoleList();
    roleList.value = res.data || [];
  } catch (error) {
    console.error('获取角色失败', error);
  }
};

onMounted(() => {
  fetchData();
});

const deptTree = ref([
  {
    id: 1, label: '海星智慧幼儿园 (校本部)', userCount: 45,
    children: [
      { id: 2, label: '园长室 / 行政部', userCount: 5 },
      { id: 3, label: '大班教研组', userCount: 12 },
      { id: 4, label: '中班教研组', userCount: 10 },
      { id: 5, label: '小班教研组', userCount: 8 },
      { id: 6, label: '保健室与后勤组', userCount: 10 }
    ]
  }
]);
</script>

<style scoped>
.apple-card-panel {
  background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 1); border-radius: 16px; padding: 20px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.panel-title { font-size: 17px; font-weight: 700; color: #0f172a; }
.tree-node-row { display: flex; justify-content: space-between; width: 100%; font-size: 14px; font-weight: 600; }
.node-badge { background: rgba(0, 113, 227, 0.1); color: #0071e3; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.apple-btn-primary { background: #0071e3; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; }
.apple-badge.success { background: rgba(52, 199, 89, 0.15); color: #248a3d; padding: 4px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; }
</style>
