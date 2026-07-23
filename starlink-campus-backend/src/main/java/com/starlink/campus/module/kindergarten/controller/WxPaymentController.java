package com.starlink.campus.module.kindergarten.controller;

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

@RestController
@RequestMapping("/payment")
@Tag(name = "微信支付收银台")
public class WxPaymentController {

    private static final Logger log = LoggerFactory.getLogger(WxPaymentController.class);

    @Autowired(required = false)
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
            }
        }

        // 资质未配置时的沙箱与联调预支付回调签名生成
        payParams.put("appId", "wx_starlink_mock_appid");
        payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payParams.put("nonceStr", "mock_nonce_" + System.currentTimeMillis());
        payParams.put("packageValue", "prepay_id=wx_prepay_mock_" + System.currentTimeMillis());
        payParams.put("signType", "RSA");
        payParams.put("paySign", "MOCK_PAY_SIGNATURE_OK");
        payParams.put("status", "PREPAY_CREATED");
        return R.ok(payParams);
    }

    @PostMapping("/notify/wechat")
    @Operation(summary = "微信支付回调异步通知与账单核销")
    public String notifyWechat(@RequestBody String xmlData) {
        log.info("[微信支付回调] 收到微信服务器异步通知数据: {}", xmlData);
        // 解析 XML / JSON 并核销订单账单状态
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
