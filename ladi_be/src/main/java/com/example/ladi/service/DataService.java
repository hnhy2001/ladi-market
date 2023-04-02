package com.example.ladi.service;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.AssignJobRequest;
import com.example.ladi.controller.request.AssignWorkRequest;
import com.example.ladi.model.Data;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public interface DataService extends BaseService<Data> {

    public BaseResponse getAllData(String jwt, String status, String startDate, String endDate, String shopCode, int page, int size);
    public BaseResponse createData(Data data, String shopCode);
    public BaseResponse assignWork(AssignJobRequest assignJobRequest);
    public BaseResponse getAllDataAccountNull(String status, String shopCode);
    public BaseResponse statisticByUtmMedium();
    public BaseResponse statisticcalrevenueByDay();
    public BaseResponse ketQuaThongKeUtm(String startDate, String endDate, String jwt);
    public BaseResponse ketQuaThongKeTopUtm(String startDate, String endDate);
    public BaseResponse statisticalRevenueByDate(String startDate, String endDate, String shopCode);
    public BaseResponse statisticUtmByDate(String startDate, String endDate, String shopCode);
    public BaseResponse statisticDataByDateAndStatus(String startDate, String endDate, String shopCode);
    public BaseResponse statisticQuantityDataByDateAndStatus(String startDate, String endDate);
}
