package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.dto.ContactDTO;
import java.util.List;

public interface ContactService {
    List<ContactDTO> getContactList();
    List<ContactDTO> searchContacts(String keyword);
}
