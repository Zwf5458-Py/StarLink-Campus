package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgFeedbackTicket;
import com.starlink.campus.module.kindergarten.mapper.KgFeedbackTicketMapper;
import com.starlink.campus.module.kindergarten.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired(required = false)
    private KgFeedbackTicketMapper feedbackMapper;

    @Override
    public KgFeedbackTicket submitFeedback(KgFeedbackTicket ticket) {
        ticket.setStatus("SUBMITTED");
        feedbackMapper.insert(ticket);
        return ticket;
    }

    @Override
    public boolean handleFeedback(Long ticketId, Long handlerId, String process) {
        KgFeedbackTicket ticket = feedbackMapper.selectById(ticketId);
        if (ticket != null) {
            ticket.setHandlerId(handlerId);
            ticket.setHandlingProcess(process);
            ticket.setStatus("PROCESSING");
            return feedbackMapper.updateById(ticket) > 0;
        }
        return false;
    }

    @Override
    public boolean closeFeedback(Long ticketId, Integer score) {
        KgFeedbackTicket ticket = feedbackMapper.selectById(ticketId);
        if (ticket != null) {
            ticket.setSatisfactionScore(score);
            ticket.setStatus("CLOSED");
            return feedbackMapper.updateById(ticket) > 0;
        }
        return false;
    }

    @Override
    public List<KgFeedbackTicket> listAllFeedback(String status) {
        QueryWrapper<KgFeedbackTicket> qw = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            qw.eq("status", status);
        }
        qw.orderByDesc("create_time");
        return feedbackMapper.selectList(qw);
    }
}
