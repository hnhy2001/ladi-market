package com.example.ladi.service.impl;

import com.example.ladi.model.CallInfo;
import com.example.ladi.repository.BaseRepository;
import com.example.ladi.repository.CallInfoRepository;
import com.example.ladi.service.BaseService;
import com.example.ladi.service.CallInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallInfoServiceImpl extends BaseServiceImpl<CallInfo> implements CallInfoService {
    @Autowired
    CallInfoRepository callInfoRepository;
    @Override
    protected BaseRepository<CallInfo> getRepository() {
        return callInfoRepository;
    }
}
