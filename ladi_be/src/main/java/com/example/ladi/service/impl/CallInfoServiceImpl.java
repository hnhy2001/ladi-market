package com.example.ladi.service.impl;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.model.CallInfo;
import com.example.ladi.repository.BaseRepository;
import com.example.ladi.repository.CallInfoRepository;
import com.example.ladi.service.CallInfoService;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallInfoServiceImpl extends BaseServiceImpl<CallInfo> implements CallInfoService {
    @Autowired
    CallInfoRepository callInfoRepository;
    @Override
    protected BaseRepository<CallInfo> getRepository() {
        return callInfoRepository;
    }

    @Override
    public BaseResponse getCallActive() {
        Pageable pageable = PageRequest.of(0,100);
        List<CallInfo> callList = callInfoRepository.findAllByIsActive(1);

        if (callList.isEmpty()){
            return new BaseResponse(500, "FAIL", "Không tồn tại!");
        }
        double randomDouble = Math.random();
        randomDouble = randomDouble * callList.size() ;
        int index = (int) randomDouble;
        return new BaseResponse(200, "OK", callInfoRepository.findAllByIsActive(1).get(index));
    }

    @Override
    public BaseResponse active(Long id) {
        CallInfo callInfo = callInfoRepository.findAllById(id);
        if (callInfo == null){
            return new BaseResponse(500, "FAIL", "Không tồn tại bản ghi!");
        }
        callInfo.setIsActive(1);
        callInfoRepository.save(callInfo);
        return new BaseResponse(200, "OK", callInfo);
    }

    @Override
    public BaseResponse deActive(Long id) {
        CallInfo callInfo = callInfoRepository.findAllById(id);
        if (callInfo == null){
            return new BaseResponse(500, "FAIL", "Không tồn tại bản ghi!");
        }
        callInfo.setIsActive(0);
        callInfoRepository.save(callInfo);
        return new BaseResponse(200, "OK", callInfo);
    }

    @Override
    public BaseResponse heathCheck() {
        if (callInfoRepository.heathCheck() == 1){
            return new BaseResponse(200, "Ok", "Kết nối thành công!" );
        }else {
            return new BaseResponse(500, "FAIL", "Kết nối thất bại!");
        }
    }
}
