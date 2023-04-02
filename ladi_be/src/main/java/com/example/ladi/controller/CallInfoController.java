package com.example.ladi.controller;

import com.example.ladi.model.CallInfo;
import com.example.ladi.service.BaseService;
import com.example.ladi.service.CallInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("call_info")
@CrossOrigin
public class CallInfoController extends BaseController<CallInfo>{
    @Autowired
    CallInfoService callInfoService;
    @Override
    protected BaseService<CallInfo> getService() {
        return callInfoService;
    }
}
