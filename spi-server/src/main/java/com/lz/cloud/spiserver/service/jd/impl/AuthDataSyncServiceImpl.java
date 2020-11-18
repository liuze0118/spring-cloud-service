package com.lz.cloud.spiserver.service.jd.impl;

import com.lz.cloud.spiserver.service.AuthDataSyncService;
import org.springframework.stereotype.Service;

@Service
public class AuthDataSyncServiceImpl implements AuthDataSyncService {
    @Override
    public boolean synchronizationUserData() {
        System.out.println("jd-user-data");
        return false;
    }

    @Override
    public boolean synchronizationOrgData() {
        System.out.println("jd-org-data");
        return false;
    }

    @Override
    public boolean synchronizationResData() {
        System.out.println("jd-res-data");
        return false;
    }
}
