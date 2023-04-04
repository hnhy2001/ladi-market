package com.example.ladi.service;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.model.CallInfo;

public interface CallInfoService extends BaseService<CallInfo>{
    public BaseResponse getCallActive();
    public BaseResponse active(Long id);
    public BaseResponse deActive(Long id);
    public BaseResponse heathCheck();
}
