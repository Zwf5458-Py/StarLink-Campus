package com.starlink.campus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;

/**
 * 全局统一异常处理器 (Global Exception Handler)
 * 遵循代码分析与优化报告 3.1 节规范
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        log.error("[全局异常捕获] 系统运行异常: {}", e.getMessage(), e);
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R<String> handleRuntimeException(RuntimeException e) {
        log.warn("[运行时异常拦截]: {}", e.getMessage());
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() != null ? 
                e.getBindingResult().getFieldError().getDefaultMessage() : e.getMessage();
        log.warn("[DTO参数校验失败]: {}", msg);
        return R.fail(msg);
    }

    @ExceptionHandler(NotLoginException.class)
    public R<String> handleNotLoginException(NotLoginException e) {
        log.warn("[鉴权拦截] 未登录或Token无效: {}", e.getMessage());
        return R.fail(401, "请先登录系统: " + e.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    public R<String> handleNotPermissionException(NotPermissionException e) {
        log.warn("[权限拦截] 无对应权限: {}", e.getMessage());
        return R.fail(403, "权限不足: " + e.getMessage());
    }
}
