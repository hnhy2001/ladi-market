package com.example.ladi.controller;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.model.CallInfo;
import com.example.ladi.service.BaseService;
import com.example.ladi.service.CallInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("call")
@CrossOrigin
public class CallInfoController extends BaseController<CallInfo>{
    @Autowired
    CallInfoService callInfoService;
    @Override
    protected BaseService<CallInfo> getService() {
        return callInfoService;
    }

    @GetMapping("get-active")
    public BaseResponse getActive(){
        return callInfoService.getCallActive();
    }

    @GetMapping("active")
    public BaseResponse active(@RequestParam Long id){
        return callInfoService.active(id);
    }

    @GetMapping("deactive")
    public BaseResponse deActive(@RequestParam Long id){
        return callInfoService.deActive(id);
    }

    @GetMapping("heath-check")
    public BaseResponse heathCheck(){
        return callInfoService.heathCheck();
    }
}
