package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgOaApproval;
import com.starlink.campus.module.kindergarten.mapper.KgOaApprovalMapper;
import com.starlink.campus.module.kindergarten.service.KgOaApprovalService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KgOaApprovalServiceImpl extends ServiceImpl<KgOaApprovalMapper, KgOaApproval> implements KgOaApprovalService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private com.starlink.campus.module.kindergarten.mapper.KgStudentAttendanceMapper attendanceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submit(KgOaApproval approval) {
        approval.setCreateTime(LocalDateTime.now());
        approval.setStatus("待审批");
        boolean saved = this.save(approval);
        if (saved) {
            String processKey = getProcessKeyByType(approval.getApprovalType());
            Map<String, Object> variables = new HashMap<>();
            variables.put("approvalId", approval.getId());
            variables.put("applicantName", approval.getApplicantName());
            
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, String.valueOf(approval.getId()), variables);
            approval.setProcessInstanceId(processInstance.getId());
            this.updateById(approval);
            return true;
        }
        return false;
    }

    private String getProcessKeyByType(String type) {
        if ("采购申请".equals(type)) return "kindergarten_oa_purchase";
        if ("加班申请".equals(type)) return "kindergarten_oa_overtime";
        if ("补卡申请".equals(type)) return "kindergarten_oa_attendance_appeal";
        return "kindergarten_oa_leave";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long id) {
        KgOaApproval oa = this.getById(id);
        if (oa != null && oa.getProcessInstanceId() != null) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(oa.getProcessInstanceId()).list();
            if (!tasks.isEmpty()) {
                Task task = tasks.get(0);
                taskService.complete(task.getId());
                
                // 检查流程是否结束
                ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(oa.getProcessInstanceId()).singleResult();
                if (pi == null) {
                    oa.setStatus("已通过");
                    oa.setApproveTime(LocalDateTime.now());
                    oa.setApprovalComment("同意");
                    
                    // 业务闭环：家长请假申请自动生成请假考勤记录
                    if ("请假申请".equals(oa.getApprovalType()) && "家长".equals(oa.getApplicantRole()) && oa.getApplicantId() != null) {
                        try {
                            // 尝试从 reason 中解析出请假日期，例如: 【事假】 2026-07-24 至 2026-07-25，原因：...
                            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d{4}-\\d{2}-\\d{2}) 至 (\\d{4}-\\d{2}-\\d{2})");
                            java.util.regex.Matcher matcher = pattern.matcher(oa.getReason() != null ? oa.getReason() : "");
                            if (matcher.find()) {
                                java.time.LocalDate startDate = java.time.LocalDate.parse(matcher.group(1));
                                java.time.LocalDate endDate = java.time.LocalDate.parse(matcher.group(2));
                                
                                if (attendanceMapper != null) {
                                    java.time.LocalDate d = startDate;
                                    while (!d.isAfter(endDate)) {
                                        com.starlink.campus.module.kindergarten.entity.KgStudentAttendance att = new com.starlink.campus.module.kindergarten.entity.KgStudentAttendance();
                                        att.setStudentId(oa.getApplicantId());
                                        att.setAttendanceDate(java.sql.Date.valueOf(d));
                                        att.setStatus("LEAVE");
                                        att.setCheckInTime(null);
                                        att.setCheckOutTime(null);
                                        att.setCreateTime(new java.util.Date());
                                        attendanceMapper.insert(att);
                                        d = d.plusDays(1);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("自动创建请假考勤记录失败", e);
                        }
                    }
                } else {
                    oa.setStatus("审批中");
                }
                return this.updateById(oa);
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reject(Long id) {
        KgOaApproval oa = this.getById(id);
        if (oa != null && oa.getProcessInstanceId() != null) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(oa.getProcessInstanceId()).list();
            if (!tasks.isEmpty()) {
                // 驳回时直接删除流程实例或走驳回分支
                runtimeService.deleteProcessInstance(oa.getProcessInstanceId(), "Rejected by user");
                oa.setStatus("已驳回");
                oa.setApproveTime(LocalDateTime.now());
                oa.setApprovalComment("拒绝");
                return this.updateById(oa);
            }
        }
        return false;
    }
}
