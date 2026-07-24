package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgFeedbackTicket;
import java.util.List;

public interface FeedbackService {
    KgFeedbackTicket submitFeedback(KgFeedbackTicket ticket);
    boolean handleFeedback(Long ticketId, Long handlerId, String process);
    boolean closeFeedback(Long ticketId, Integer score);
    List<KgFeedbackTicket> listAllFeedback(String status);
}
