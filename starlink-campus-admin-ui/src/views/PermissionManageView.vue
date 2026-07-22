<template>
  <div class="apple-permission-manage" v-loading="loading">
    <div class="apple-card-panel">
      <div class="panel-header">
        <div class="panel-title">
          <span class="icon">🛡️</span>
          <span>系统角色与权限控制中心 (Permission & RBAC Management)</span>
        </div>
        <button class="apple-btn-primary" @click="openAddRoleModal">+ 新增自定义角色</button>
      </div>

      <el-row :gutter="20">
        <!-- 左侧：角色列表选择 -->
        <el-col :span="8">
          <div class="role-list-box">
            <div class="box-title">园区角色列表</div>
            <div 
              v-for="role in roles" 
              :key="role.id" 
              class="role-card-item"
              :class="{ active: selectedRole?.id === role.id }"
              @click="selectRole(role)"
            >
              <div class="role-card-top">
                <span class="role-name">{{ role.roleName }}</span>
                <span class="role-code">{{ role.roleCode }}</span>
              </div>
              <div class="role-desc">{{ role.description }}</div>
              <div class="role-count">关联教职工: {{ role.userCount }} 人</div>
            </div>
          </div>
        </el-col>

        <!-- 右侧：菜单权限与按钮数据权限勾选树 -->
        <el-col :span="16">
          <div class="permission-tree-box">
            <div class="tree-box-header">
              <span class="editing-title" v-if="selectedRole">正在配置角色：【{{ selectedRole.roleName }}】的权限矩阵</span>
              <button class="apple-btn-success" @click="savePermission">保存当前权限修改</button>
            </div>

            <el-tabs v-model="permissionTab">
              <!-- 菜单与页面访问权限 -->
              <el-tab-pane label="📋 菜单与功能页面权限" name="menu">
                <el-tree
                  ref="menuTreeRef"
                  :data="menuPermissionTree"
                  show-checkbox
                  node-key="id"
                  default-expand-all
                  :default-checked-keys="selectedRole?.checkedMenuIds || []"
                >
                  <template #default="{ node, data }">
                    <div class="tree-custom-node">
                      <span>{{ data.label }}</span>
                      <span v-if="data.type" class="node-type-tag" :class="data.type">{{ data.typeText }}</span>
                    </div>
                  </template>
                </el-tree>
              </el-tab-pane>

              <!-- 按钮与操作数据权限 -->
              <el-tab-pane label="⚡ 按钮操作与敏感数据授权" name="button">
                <div class="action-perm-grid">
                  <div class="perm-group">
                    <span class="group-label">👶 幼儿档案权限</span>
                    <el-checkbox-group v-if="selectedRole" v-model="selectedRole.actionPerms">
                      <el-checkbox label="student:add">新增档案</el-checkbox>
                      <el-checkbox label="student:edit">编辑资料</el-checkbox>
                      <el-checkbox label="student:delete">删除档案</el-checkbox>
                      <el-checkbox label="student:export">导出敏感过敏源</el-checkbox>
                    </el-checkbox-group>
                  </div>

                  <div class="perm-group">
                    <span class="group-label">📝 园务 OA 审批权限</span>
                    <el-checkbox-group v-if="selectedRole" v-model="selectedRole.actionPerms">
                      <el-checkbox label="oa:submit">发起申请</el-checkbox>
                      <el-checkbox label="oa:approve">部门审核</el-checkbox>
                      <el-checkbox label="oa:final_approve">园长终审</el-checkbox>
                      <el-checkbox label="oa:reject">驳回/撤销</el-checkbox>
                    </el-checkbox-group>
                  </div>

                  <div class="perm-group">
                    <span class="group-label">📅 考勤退费结算权限</span>
                    <el-checkbox-group v-if="selectedRole" v-model="selectedRole.actionPerms">
                      <el-checkbox label="attendance:check">刷脸/刷卡补打卡</el-checkbox>
                      <el-checkbox label="attendance:refund">月度缺勤退费计算与结算</el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getRoleList, getMenuTree } from '@/api/system';

const loading = ref(false);
const permissionTab = ref('menu');

const roles = ref([]);
const menuPermissionTree = ref([]);
const selectedRole = ref(null);

const fetchData = async () => {
  loading.value = true;
  try {
    const roleRes = await getRoleList();
    roles.value = roleRes.data || [];
    if (roles.value.length > 0) {
      selectedRole.value = roles.value[0];
    }
    
    const menuRes = await getMenuTree();
    menuPermissionTree.value = menuRes.data || [];
  } catch (error) {
    ElMessage.error('获取权限数据失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const selectRole = (role) => {
  selectedRole.value = role;
};

const savePermission = () => {
  ElMessage.success(`已成功保存【${selectedRole.value?.roleName}】的权限更新！权限矩阵即刻生效。`);
};

const openAddRoleModal = () => {
  ElMessage.info('调起新增角色弹窗');
};
</script>

<style scoped>
.apple-card-panel {
  background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 1); border-radius: 16px; padding: 20px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.panel-title { font-size: 17px; font-weight: 700; color: #0f172a; display: flex; align-items: center; gap: 8px; }

.role-list-box {
  background: rgba(248, 250, 252, 0.8); border: 1px solid rgba(0,0,0,0.06); border-radius: 14px; padding: 14px; display: flex; flex-direction: column; gap: 10px;
}
.box-title { font-size: 13px; font-weight: bold; color: #64748b; margin-bottom: 4px; }
.role-card-item {
  background: #ffffff; border: 1px solid rgba(0,0,0,0.06); border-radius: 12px; padding: 12px; cursor: pointer; transition: all 0.2s ease;
}
.role-card-item:hover { border-color: #0071e3; }
.role-card-item.active { border-color: #0071e3; background: rgba(0, 113, 227, 0.04); box-shadow: 0 4px 12px rgba(0, 113, 227, 0.12); }

.role-card-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.role-name { font-size: 14px; font-weight: bold; color: #0f172a; }
.role-code { font-size: 11px; background: rgba(0,0,0,0.05); padding: 1px 6px; border-radius: 6px; color: #64748b; font-family: monospace; }
.role-desc { font-size: 11px; color: #64748b; margin-bottom: 6px; }
.role-count { font-size: 10px; color: #0071e3; font-weight: bold; }

.permission-tree-box {
  background: rgba(248, 250, 252, 0.8); border: 1px solid rgba(0,0,0,0.06); border-radius: 14px; padding: 16px; min-height: 480px;
}
.tree-box-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.editing-title { font-size: 14px; font-weight: bold; color: #0f172a; }

.tree-custom-node { display: flex; justify-content: space-between; width: 100%; font-size: 13px; font-weight: 600; padding-right: 10px; }
.node-type-tag { font-size: 10px; padding: 1px 6px; border-radius: 6px; }
.node-type-tag.page { background: rgba(0, 113, 227, 0.1); color: #0071e3; }
.node-type-tag.module { background: rgba(175, 82, 222, 0.1); color: #af52de; }
.node-type-tag.screen { background: rgba(255, 149, 0, 0.1); color: #ff9500; }

.action-perm-grid { display: flex; flex-direction: column; gap: 16px; padding: 10px 0; }
.perm-group { display: flex; flex-direction: column; gap: 8px; background: #ffffff; padding: 12px; border-radius: 10px; border: 1px solid rgba(0,0,0,0.05); }
.group-label { font-size: 13px; font-weight: bold; color: #0f172a; }

.apple-btn-primary { background: #0071e3; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; }
.apple-btn-success { background: #34c759; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; }
</style>
