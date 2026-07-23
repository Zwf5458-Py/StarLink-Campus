package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.entity.KgStaff;
import com.starlink.campus.module.kindergarten.entity.KgOaApproval;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffMapper;
import com.starlink.campus.module.kindergarten.mapper.KgOaApprovalMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired(required = false)
    private KgStudentMapper studentMapper;

    @Autowired(required = false)
    private KgStaffMapper staffMapper;

    @Autowired(required = false)
    private KgOaApprovalMapper oaApprovalMapper;

    @Autowired(required = false)
    private KgPatrolRecordMapper patrolRecordMapper;

    @GetMapping
    public Map<String, Object> globalSearch(@RequestParam("q") String q) {
        List<Map<String, Object>> results = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            String keyword = "%" + q.trim() + "%";

            // 搜索学生
            if (studentMapper != null) {
                List<KgStudent> students = studentMapper.selectList(new QueryWrapper<KgStudent>().like("name", q).or().like("guardian_phone", q).last("LIMIT 5"));
                for (KgStudent s : students) {
                    results.add(createResult("幼儿档案", s.getName() + " (" + s.getGender() + ")", "/student"));
                }
            }

            // 搜索教职工
            if (staffMapper != null) {
                List<KgStaff> staffs = staffMapper.selectList(new QueryWrapper<KgStaff>().like("name", q).or().like("phone", q).last("LIMIT 5"));
                for (KgStaff s : staffs) {
                    results.add(createResult("教职工", s.getName() + " - " + s.getRoleType(), "/system"));
                }
            }

            // 搜索 OA 审批单
            if (oaApprovalMapper != null) {
                try {
                    List<KgOaApproval> oas = oaApprovalMapper.selectList(new QueryWrapper<KgOaApproval>().like("approval_type", q).or().like("applicant_name", q).last("LIMIT 5"));
                    for (KgOaApproval oa : oas) {
                        results.add(createResult("OA审批", oa.getApprovalType() + " - " + oa.getApplicantName(), "/oa"));
                    }
                } catch (Exception ignore) {}
            }

            // 搜索安防巡检单
            if (patrolRecordMapper != null) {
                try {
                    List<KgPatrolRecord> patrols = patrolRecordMapper.selectList(new QueryWrapper<KgPatrolRecord>().like("patrol_point_name", q).last("LIMIT 5"));
                    for (KgPatrolRecord p : patrols) {
                        results.add(createResult("安防巡检", p.getPatrolPointName(), "/patrol"));
                    }
                } catch (Exception ignore) {}
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", results);
        return res;
    }

    private Map<String, Object> createResult(String category, String text, String path) {
        Map<String, Object> r = new HashMap<>();
        r.put("category", category);
        r.put("text", text);
        r.put("path", path);
        return r;
    }
}
