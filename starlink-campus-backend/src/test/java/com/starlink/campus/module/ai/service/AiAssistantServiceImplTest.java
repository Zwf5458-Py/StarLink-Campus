package com.starlink.campus.module.ai.service;

import com.starlink.campus.module.ai.mapper.KgAiLogMapper;
import com.starlink.campus.module.ai.service.impl.AiAssistantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AiAssistantServiceImplTest {

    @InjectMocks
    private AiAssistantServiceImpl aiAssistantService;

    @Mock
    private AiGatewayService aiGatewayService;

    @Mock
    private KgAiLogMapper aiLogMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateBatchDailyComments() {
        String mockResponse = "[\n" +
                "  {\"studentId\": 1, \"comment\": \"宝宝今天吃饭很乖\"},\n" +
                "  {\"studentId\": 2, \"comment\": \"宝宝午睡很快入睡\"}\n" +
                "]";
        when(aiGatewayService.generateTextAsync(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));

        List<Map<String, Object>> input = new ArrayList<>();
        Map<String, Object> s1 = new HashMap<>();
        s1.put("studentId", 1L);
        s1.put("diet", "好");
        input.add(s1);

        Map<String, Object> s2 = new HashMap<>();
        s2.put("studentId", 2L);
        s2.put("sleep", "好");
        input.add(s2);

        List<Map<String, Object>> result = aiAssistantService.generateBatchDailyComments(input);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("宝宝今天吃饭很乖", result.get(0).get("comment"));
    }

    @Test
    void testGenerateParentMessageReplyDraft() {
        when(aiGatewayService.generateTextAsync(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture("收到您的请假申请，祝宝宝早日康复。"));

        String draft = aiAssistantService.generateParentMessageReplyDraft("老师，孩子今天发烧请假一天。", "请假");
        assertNotNull(draft);
        assertTrue(draft.contains("康复"));
    }

    @Test
    void testGenerateNoticeDraft() {
        String mockResponse = "{\"title\": \"端午节放假通知\", \"content\": \"放假三天\"}";
        when(aiGatewayService.generateTextAsync(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));

        Map<String, String> notice = aiAssistantService.generateNoticeDraft("端午放假", "全体家长");
        assertNotNull(notice);
        assertEquals("端午节放假通知", notice.get("title"));
        assertEquals("放假三天", notice.get("content"));
    }
}
