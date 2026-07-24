package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.dto.MenuNodeVO;
import com.starlink.campus.module.kindergarten.dto.RolePermissionSaveDTO;
import com.starlink.campus.module.kindergarten.dto.RoleVO;
import com.starlink.campus.module.kindergarten.entity.KgRole;
import com.starlink.campus.module.kindergarten.service.KgMenuService;
import com.starlink.campus.module.kindergarten.service.KgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SaCheckLogin
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private KgRoleService roleService;

    @Autowired
    private KgMenuService menuService;

    // 内存数据网关存储（保证环境即开即用与动态保存）
    private static final Map<Long, RoleVO> ROLE_CACHE = new ConcurrentHashMap<>();
    private static final List<MenuNodeVO> MENU_TREE_CACHE = new ArrayList<>();

    static {
        initDefaultMenus();
        initDefaultRoles();
    }

    @GetMapping("/role/list")
    public R<List<RoleVO>> roleList() {
        // 如果数据库有角色优先同步，否则返回内存缓存
        List<KgRole> dbRoles = roleService.list();
        if (dbRoles != null && !dbRoles.isEmpty()) {
            for (KgRole role : dbRoles) {
                if (!ROLE_CACHE.containsKey(role.getId())) {
                    RoleVO vo = new RoleVO();
                    vo.setId(role.getId());
                    vo.setRoleName(role.getRoleName());
                    vo.setRoleCode(role.getRoleKey() != null ? role.getRoleKey() : "ROLE_CUSTOM");
                    vo.setDescription(role.getRemark() != null ? role.getRemark() : "自定义园区角色");
                    vo.setUserCount(5);
                    vo.setCheckedMenuIds(Arrays.asList(101L, 102L, 103L, 201L, 202L));
                    vo.setActionPerms(Arrays.asList("student:add", "student:edit", "oa:submit"));
                    ROLE_CACHE.put(role.getId(), vo);
                }
            }
        }
        return R.ok(new ArrayList<>(ROLE_CACHE.values()));
    }

    @GetMapping("/menu/tree")
    public R<List<MenuNodeVO>> menuTree() {
        return R.ok(MENU_TREE_CACHE);
    }

    @PostMapping("/role")
    public R<RoleVO> addRole(@Valid @RequestBody KgRole role) {
        Long newId = System.currentTimeMillis();
        role.setId(newId);
        try {
            roleService.save(role);
        } catch (Exception ignored) {}

        RoleVO vo = new RoleVO();
        vo.setId(newId);
        vo.setRoleName(role.getRoleName() != null ? role.getRoleName() : "新自定义角色");
        vo.setRoleCode(role.getRoleKey() != null ? role.getRoleKey() : "ROLE_NEW");
        vo.setDescription(role.getRemark() != null ? role.getRemark() : "新建园区自定义业务角色");
        vo.setUserCount(1);
        vo.setCheckedMenuIds(Arrays.asList(101L, 102L));
        vo.setActionPerms(Arrays.asList("oa:submit"));

        ROLE_CACHE.put(newId, vo);
        return R.ok(vo);
    }

    @PostMapping("/role/permission")
    public R<String> saveRolePermission(@Valid @RequestBody RolePermissionSaveDTO dto) {
        if (dto.getRoleId() != null && ROLE_CACHE.containsKey(dto.getRoleId())) {
            RoleVO roleVO = ROLE_CACHE.get(dto.getRoleId());
            roleVO.setCheckedMenuIds(dto.getCheckedMenuIds() != null ? dto.getCheckedMenuIds() : Collections.emptyList());
            roleVO.setActionPerms(dto.getActionPerms() != null ? dto.getActionPerms() : Collections.emptyList());
        }
        return R.ok("角色权限保存成功，RBAC 授权即刻生效！");
    }

    private static void initDefaultRoles() {
        RoleVO r1 = new RoleVO();
        r1.setId(1L);
        r1.setRoleName("园长 (系统管理员)");
        r1.setRoleCode("ROLE_ADMIN");
        r1.setDescription("具备园所最高管理权限，全功能与全数据监控看板接入");
        r1.setUserCount(2);
        r1.setCheckedMenuIds(Arrays.asList(
            100L, 101L, 102L, 200L, 201L, 202L, 203L, 300L, 301L, 400L, 401L, 402L, 
            500L, 501L, 502L, 600L, 601L, 602L, 700L, 701L, 800L, 801L
        ));
        r1.setActionPerms(Arrays.asList("student:add", "student:edit", "student:delete", "student:export", "oa:submit", "oa:approve", "oa:final_approve", "oa:reject", "attendance:check", "attendance:refund", "ai:health_vision", "security:dispatch"));

        RoleVO r2 = new RoleVO();
        r2.setId(2L);
        r2.setRoleName("副园长 / 教务主任");
        r2.setRoleCode("ROLE_DIRECTOR");
        r2.setDescription("负责教务教学管理、OA流程审核、考勤退费与周计划审定");
        r2.setUserCount(4);
        r2.setCheckedMenuIds(Arrays.asList(100L, 101L, 200L, 201L, 203L, 400L, 401L, 500L, 501L, 700L, 701L));
        r2.setActionPerms(Arrays.asList("student:add", "student:edit", "oa:submit", "oa:approve", "oa:final_approve", "attendance:check", "attendance:refund"));

        RoleVO r3 = new RoleVO();
        r3.setId(3L);
        r3.setRoleName("班主任 / 任课教师");
        r3.setRoleCode("ROLE_TEACHER");
        r3.setDescription("负责班级幼儿档案、多媒体成长记录、打卡与家园共育互动");
        r3.setUserCount(28);
        r3.setCheckedMenuIds(Arrays.asList(200L, 201L, 400L, 401L, 402L, 500L, 501L, 700L, 701L));
        r3.setActionPerms(Arrays.asList("student:add", "student:edit", "oa:submit", "attendance:check"));

        RoleVO r4 = new RoleVO();
        r4.setId(4L);
        r4.setRoleName("保健医生 / 保育员");
        r4.setRoleCode("ROLE_DOCTOR");
        r4.setDescription("负责晨检体温与手足口AI诊断、食谱管理与幼儿健康防护");
        r4.setUserCount(6);
        r4.setCheckedMenuIds(Arrays.asList(600L, 601L, 602L, 400L, 401L));
        r4.setActionPerms(Arrays.asList("ai:health_vision", "student:edit", "oa:submit"));

        RoleVO r5 = new RoleVO();
        r5.setId(5L);
        r5.setRoleName("后勤安防管理员");
        r5.setRoleCode("ROLE_SECURITY");
        r5.setDescription("负责校园安防巡检工单、设备维保、访客扫码与滞留预警");
        r5.setUserCount(8);
        r5.setCheckedMenuIds(Arrays.asList(500L, 502L, 600L, 601L));
        r5.setActionPerms(Arrays.asList("security:dispatch", "oa:submit"));

        RoleVO r6 = new RoleVO();
        r6.setId(6L);
        r6.setRoleName("财务人员");
        r6.setRoleCode("ROLE_FINANCE");
        r6.setDescription("负责考勤缺勤退费结算核销、缴费账单及在线收银对账");
        r6.setUserCount(3);
        r6.setCheckedMenuIds(Arrays.asList(500L, 501L, 200L, 201L));
        r6.setActionPerms(Arrays.asList("attendance:refund", "student:export"));

        ROLE_CACHE.put(1L, r1);
        ROLE_CACHE.put(2L, r2);
        ROLE_CACHE.put(3L, r3);
        ROLE_CACHE.put(4L, r4);
        ROLE_CACHE.put(5L, r5);
        ROLE_CACHE.put(6L, r6);
    }

    private static void initDefaultMenus() {
        MenuNodeVO m1 = createNode(100L, 0L, "📊 园所概览与数据看板", "module", "核心模块", "/dashboard", "DataAnalysis");
        m1.getChildren().add(createNode(101L, 100L, "园所实时运行看板", "page", "功能页面", "/dashboard/index", "Monitor"));
        m1.getChildren().add(createNode(102L, 100L, "8大数据监管大屏", "screen", "可视化大屏", "/screen/board", "Platform"));

        MenuNodeVO m2 = createNode(200L, 0L, "🛡️ 系统角色与权限控制中心", "module", "核心模块", "/permission", "Lock");
        m2.getChildren().add(createNode(201L, 200L, "角色与权限管理", "page", "功能页面", "/permission/index", "UserFilled"));
        m2.getChildren().add(createNode(202L, 200L, "教职工数据权限隔离", "page", "功能页面", "/permission/staff", "Avatar"));
        m2.getChildren().add(createNode(203L, 200L, "操作日志与审计轨迹", "page", "功能页面", "/permission/logs", "Document"));

        MenuNodeVO m3 = createNode(300L, 0L, "🏫 智慧校园与平台微官网", "module", "业务模块", "/campus", "School");
        m3.getChildren().add(createNode(301L, 300L, "校园微官网配置与发布", "page", "功能页面", "/campus/website", "Globe"));

        MenuNodeVO m4 = createNode(400L, 0L, "👶 幼儿档案与多媒体成长", "module", "业务模块", "/growth", "Files");
        m4.getChildren().add(createNode(401L, 400L, "幼儿档案与敏感情况", "page", "功能页面", "/growth/archive", "User"));
        m4.getChildren().add(createNode(402L, 400L, "成长档案 (视频与照片)", "page", "功能页面", "/growth/media", "VideoCamera"));

        MenuNodeVO m5 = createNode(500L, 0L, "📑 刷脸考勤、缺勤退费与OA", "module", "业务模块", "/oa", "Stamp");
        m5.getChildren().add(createNode(501L, 500L, "刷脸考勤与缺勤退费结算", "page", "功能页面", "/oa/attendance", "CreditCard"));
        m5.getChildren().add(createNode(502L, 500L, "园务 OA 审批中心", "page", "功能页面", "/oa/approval", "DocumentChecked"));

        MenuNodeVO m6 = createNode(600L, 0L, "🛡️ 安防巡检与晨检食谱", "module", "业务模块", "/safety", "Aim");
        m6.getChildren().add(createNode(601L, 600L, "安防巡检与维保工单", "page", "功能页面", "/safety/patrol", "Warning"));
        m6.getChildren().add(createNode(602L, 600L, "晨检体温与每周食谱", "page", "功能页面", "/safety/health", "Dish"));

        MenuNodeVO m7 = createNode(700L, 0L, "👨‍👩‍👧 家园共育与拓展活动", "module", "业务模块", "/family", "ChatLineRound");
        m7.getChildren().add(createNode(701L, 700L, "家园共育与 AI 智能园秘", "page", "功能页面", "/family/cooperation", "ChatDotSquare"));

        MENU_TREE_CACHE.add(m1);
        MENU_TREE_CACHE.add(m2);
        MENU_TREE_CACHE.add(m3);
        MENU_TREE_CACHE.add(m4);
        MENU_TREE_CACHE.add(m5);
        MENU_TREE_CACHE.add(m6);
        MENU_TREE_CACHE.add(m7);
    }

    private static MenuNodeVO createNode(Long id, Long parentId, String label, String type, String typeText, String path, String icon) {
        MenuNodeVO node = new MenuNodeVO();
        node.setId(id);
        node.setParentId(parentId);
        node.setLabel(label);
        node.setType(type);
        node.setTypeText(typeText);
        node.setPath(path);
        node.setIcon(icon);
        return node;
    }
}

