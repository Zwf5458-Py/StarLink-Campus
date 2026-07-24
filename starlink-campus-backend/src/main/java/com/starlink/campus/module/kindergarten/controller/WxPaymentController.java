package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import com.starlink.campus.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SaCheckLogin
@RestController
@RequestMapping("/payment")
@Tag(name = "微信支付收银台")
public class WxPaymentController {

    private static final Logger log = LoggerFactory.getLogger(WxPaymentController.class);

    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/create-order")
    @Operation(summary = "创建微信 JSAPI 预支付订单并签名")
    public R<Map<String, Object>> createOrder(@RequestParam String outTradeNo,
                                            @RequestParam BigDecimal amount,
                                            @RequestParam String description,
                                            @RequestParam(required = false) String openId) {
        log.info("[微信支付] 下单请求, 单号={}, 金额={}, 描述={}", outTradeNo, amount, description);

        Map<String, Object> payParams = new HashMap<>();
        payParams.put("outTradeNo", outTradeNo);
        payParams.put("amount", amount);

        if (wxPayService != null) {
            try {
                WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
                orderRequest.setOutTradeNo(outTradeNo);
                orderRequest.setTotalFee(amount.multiply(new BigDecimal("100")).intValue());
                orderRequest.setBody(description);
                orderRequest.setTradeType("JSAPI");
                orderRequest.setOpenid(openId != null ? openId : "oMockUserOpenId123456");
                orderRequest.setSpbillCreateIp("127.0.0.1");

                WxPayMpOrderResult result = wxPayService.createOrder(orderRequest);
                payParams.put("appId", result.getAppId());
                payParams.put("timeStamp", result.getTimeStamp());
                payParams.put("nonceStr", result.getNonceStr());
                payParams.put("packageValue", result.getPackageValue());
                payParams.put("signType", result.getSignType());
                payParams.put("paySign", result.getPaySign());
                payParams.put("status", "SUCCESS");
                return R.ok(payParams);
            } catch (Exception e) {
                log.error("[微信支付] 统一下单异常", e);
                return R.fail("微信支付下单异常: " + e.getMessage());
            }
        }

        log.warn("[微信支付] 系统未配置微信商户或 SDK 未激活，拒绝下发虚假签名单！");
        return R.fail("系统未配置微信支付资质或 SDK 未激活，无法生成预支付单");
    }

    @PostMapping("/notify/wechat")
    @Operation(summary = "微信支付回调异步通知与账单核销")
    public String notifyWechat(@Valid @RequestBody String xmlData) {
        log.info("[微信支付回调] 收到微信服务器异步通知数据: {}", xmlData);
        if (wxPayService != null) {
            try {
                // 安全验证并解析回调数据，微信 SDK 内部自动处理签名验证
                WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
                log.info("[微信支付回调] 解析与签名验证成功，商户单号={}", notifyResult.getOutTradeNo());
                // TODO: 进行业务核销...
            } catch (Exception e) {
                log.error("[微信支付回调] 签名验证或解析失败", e);
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[Signature validation failed]]></return_msg></xml>";
            }
        } else {
            log.warn("[微信支付回调] 当前环境未挂载 SDK，正在跳过微信真实回调安全验签...");
        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
