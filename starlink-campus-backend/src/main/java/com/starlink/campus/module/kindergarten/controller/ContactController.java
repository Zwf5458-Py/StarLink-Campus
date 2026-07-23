package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.dto.ContactDTO;
import com.starlink.campus.module.kindergarten.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/api/kindergarten/contacts")
@Tag(name = "通讯录管理", description = "教职工及家长通讯录接口")
@CrossOrigin
public class ContactController {

    @Autowired
    private ContactService contactService;
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Operation(summary = "获取通讯录列表")
    @GetMapping("/list")
    public R<List<ContactDTO>> getContactList() {
        return R.ok(contactService.getContactList());
    }

    @Operation(summary = "搜索通讯录")
    @GetMapping("/search")
    public R<List<ContactDTO>> searchContacts(@RequestParam(required = false) String keyword) {
        return R.ok(contactService.searchContacts(keyword));
    }

    @Operation(summary = "安全虚拟呼叫")
    @PostMapping("/virtual-call")
    public R<String> virtualCall(@RequestBody ContactDTO contactDTO) {
        log.info("Initiating virtual call to {}, masked phone: {}", contactDTO.getName(), contactDTO.getMaskedPhone());
        return R.ok("呼叫已记录");
    }
}
