package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.entity.KgStaff;
import com.starlink.campus.module.kindergarten.entity.KgOaApproval;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffMapper;
import com.starlink.campus.module.kindergarten.mapper.KgOaApprovalMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import com.starlink.campus.module.kindergarten.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired(required = false)
    private KgStudentMapper studentMapper;

    @Autowired(required = false)
    private KgStaffMapper staffMapper;

    @Autowired(required = false)
    private KgOaApprovalMapper oaApprovalMapper;

    @Autowired(required = false)
    private KgPatrolRecordMapper patrolRecordMapper;

    @Override
    public List<Map<String, Object>> globalSearch(String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String q = "%" + keyword.trim() + "%";

            // 搜索学生
            if (studentMapper != null) {
                List<KgStudent> students = studentMapper.selectList(new QueryWrapper<KgStudent>()
                        .like("name", keyword.trim())
                        .or().like("guardian_phone", keyword.trim())
                        .last("LIMIT 5"));
                for (KgStudent s : students) {
                    results.add(createResult("幼儿档案", s.getName() + " (" + s.getGender() + ")", "/student"));
                }
            }

            // 搜索教职工
            if (staffMapper != null) {
                List<KgStaff> staffs = staffMapper.selectList(new QueryWrapper<KgStaff>()
                        .like("name", keyword.trim())
                        .or().like("phone", keyword.trim())
                        .last("LIMIT 5"));
                for (KgStaff s : staffs) {
                    results.add(createResult("教职工", s.getName() + " - " + s.getRoleType(), "/system"));
                }
            }

            // 搜索 OA 审批单
            if (oaApprovalMapper != null) {
                List<KgOaApproval> oas = oaApprovalMapper.selectList(new QueryWrapper<KgOaApproval>()
                        .like("approval_type", keyword.trim())
                        .or().like("applicant_name", keyword.trim())
                        .last("LIMIT 5"));
                for (KgOaApproval oa : oas) {
                    results.add(createResult("OA审批", oa.getApprovalType() + " - " + oa.getApplicantName(), "/oa"));
                }
            }

            // 搜索安防巡检单
            if (patrolRecordMapper != null) {
                List<KgPatrolRecord> patrols = patrolRecordMapper.selectList(new QueryWrapper<KgPatrolRecord>()
                        .like("patrol_point_name", keyword.trim())
                        .last("LIMIT 5"));
                for (KgPatrolRecord p : patrols) {
                    results.add(createResult("安防巡检", p.getPatrolPointName(), "/patrol"));
                }
            }
        }

        return results;
    }

    private Map<String, Object> createResult(String category, String text, String path) {
        Map<String, Object> r = new HashMap<>();
        r.put("category", category);
        r.put("text", text);
        r.put("path", path);
        return r;
    }
}
