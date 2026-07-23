package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgNotice;
import com.starlink.campus.module.kindergarten.mapper.KgNoticeMapper;
import com.starlink.campus.module.kindergarten.service.KgNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KgNoticeServiceImpl extends ServiceImpl<KgNoticeMapper, KgNotice> implements KgNoticeService {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<KgNotice> getUnreadNoticeList() {
        ensureTableExists();
        try {
            if (count() == 0) {
                initDefaultNotices();
            }
            LambdaQueryWrapper<KgNotice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(KgNotice::getIsRead, 0)
                        .orderByDesc(KgNotice::getCreateTime);
            return list(queryWrapper);
        } catch (Exception e) {
            initDefaultNotices();
            LambdaQueryWrapper<KgNotice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(KgNotice::getIsRead, 0)
                        .orderByDesc(KgNotice::getCreateTime);
            return list(queryWrapper);
        }
    }

    @Override
    public boolean markAsRead(Long id) {
        ensureTableExists();
        KgNotice notice = new KgNotice();
        notice.setId(id);
        notice.setIsRead(1);
        return updateById(notice);
    }

    private void ensureTableExists() {
        if (jdbcTemplate != null) {
            try {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS kg_notice (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                        "type VARCHAR(32)," +
                        "type_text VARCHAR(128)," +
                        "title VARCHAR(255)," +
                        "content TEXT," +
                        "target_role VARCHAR(64)," +
                        "is_read INT DEFAULT 0," +
                        "create_time DATETIME" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
            } catch (Exception ignored) {}
        }
    }

    private void initDefaultNotices() {
        ensureTableExists();
        KgNotice n1 = new KgNotice();
        n1.setType("danger");
        n1.setTypeText("🩺 晨检发热预警");
        n1.setTitle("小(1)班 雏菊班 晨检异常");
        n1.setContent("检测到学生 [张小明] 体温 37.5℃，已由保健医王医生复测并引导至留观室。");
        n1.setIsRead(0);
        n1.setCreateTime(LocalDateTime.now().minusMinutes(10));
        save(n1);

        KgNotice n2 = new KgNotice();
        n2.setType("warning");
        n2.setTypeText("⏳ OA 审批提醒");
        n2.setTitle("待园长终审申请");
        n2.setContent("大班李老师提交了 [急性咽喉炎请假 1 天] 申请，请及时在线审批。");
        n2.setIsRead(0);
        n2.setCreateTime(LocalDateTime.now().minusMinutes(25));
        save(n2);

        KgNotice n3 = new KgNotice();
        n3.setType("warning");
        n3.setTypeText("🎫 访客滞留告警");
        n3.setTitle("校园安防滞留提醒");
        n3.setContent("访客 [刘工 (消防检测)] 在园区停留时间已超出 15 分钟，安防系统已发送警报。");
        n3.setIsRead(0);
        n3.setCreateTime(LocalDateTime.now().minusMinutes(40));
        save(n3);
    }
}
