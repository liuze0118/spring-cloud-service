package com.lz.cloud.spiserver.service.cbsp.impl;

import com.lz.cloud.spiserver.service.AuthDataSyncService;
import org.springframework.stereotype.Service;

@Service
public class AuthDataSyncServiceImpl implements AuthDataSyncService {
    @Override
    public boolean synchronizationUserData() {
        System.out.println("cbsp-user-data");
        return false;
    }

    @Override
    public boolean synchronizationOrgData() {
        System.out.println("cbsp-org-data");
        return false;
    }

    @Override
    public boolean synchronizationResData() {
        System.out.println("cbsp-res-data");
        return false;
    }
}
