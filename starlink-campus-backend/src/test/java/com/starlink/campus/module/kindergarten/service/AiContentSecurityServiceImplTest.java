package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.ai.service.AiGatewayService;
import com.starlink.campus.module.kindergarten.service.impl.AiContentSecurityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AiContentSecurityServiceImplTest {

    @Mock
    private AiGatewayService aiGatewayService;

    @InjectMocks
    private AiContentSecurityServiceImpl securityService;

    @Test
    public void testCheckTextSecurity_Safe() {
        when(aiGatewayService.checkTextSecurityAsync(anyString()))
                .thenReturn(CompletableFuture.completedFuture(true));
        
        boolean result = securityService.checkTextSecurity("你好");
        assertTrue(result);
    }

    @Test
    public void testCheckTextSecurity_Unsafe() {
        when(aiGatewayService.checkTextSecurityAsync(anyString()))
                .thenReturn(CompletableFuture.completedFuture(false));
        
        boolean result = securityService.checkTextSecurity("危险言论");
        assertFalse(result);
    }

    @Test
    public void testCheckTextSecurity_Empty() {
        boolean result = securityService.checkTextSecurity("");
        assertTrue(result);
    }

    @Test
    public void testCheckTextSecurity_TimeoutFallback() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        // Simulate a timeout by not completing the future
        when(aiGatewayService.checkTextSecurityAsync(anyString())).thenReturn(future);
        
        // This will trigger TimeoutException inside the service which falls back to true
        boolean result = securityService.checkTextSecurity("测试超时");
        assertTrue(result);
    }
}
