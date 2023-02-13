package com.example.ladi.service;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.PostCostRequest;
import com.example.ladi.model.Cost;

public interface CostService extends BaseService<Cost>{
    public BaseResponse postCost(PostCostRequest postCostRequest);
    public BaseResponse getCost();

    public BaseResponse laySoDonTheoThoiGian(String startDate, String endDate);
}
