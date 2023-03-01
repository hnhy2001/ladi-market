package com.example.ladi.controller;

import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.CheckOutRequest;
import com.example.ladi.controller.request.CheckWorkActiveRequest;
import com.example.ladi.controller.request.CreateWorkRequest;
import com.example.ladi.model.Work;
import com.example.ladi.service.BaseService;
import com.example.ladi.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("work")
@CrossOrigin
public class WorkController extends BaseController<Work>{
    @Autowired
    WorkService workService;
    @Override
    protected BaseService<Work> getService() {
        return workService;
    }

    @PostMapping("")
    public BaseResponse createWork(@RequestBody CreateWorkRequest createWorkRequest){
        return workService.createWork(createWorkRequest);
    }

    @GetMapping("")
    public BaseResponse getAllWork(@RequestHeader(name = "Authorization") String jwt, @RequestParam String startDate, @RequestParam String endDate){

        return workService.getAllWork(jwt, startDate, endDate);
    }

    @PostMapping("/checkOut")
    public BaseResponse checkOut(@RequestBody CheckOutRequest checkOutRequest, @RequestParam(name = "shopCode", required = false) String shopCode){
        return workService.checkOut(checkOutRequest, shopCode);
    }

    @GetMapping("getAllActive")
    public BaseResponse getAllIsActive(@RequestParam(name = "shopCode", required = false) String shopCode){
        return workService.getAllActive(shopCode);
    }

    @PostMapping("checkWorkActive")
    public BaseResponse getAllIsActive(@RequestBody CheckWorkActiveRequest checkWorkActiveRequest, @RequestParam(name = "shopCode", required = false) String shopCode){
        return workService.checkWorkActive(checkWorkActiveRequest, shopCode);
    }

    @PostMapping("infoCheckout")
    public BaseResponse infoCheckout(@RequestParam Long id){
//        return new BaseResponse(200, "Ok", "hello");
        return workService.infoCheckout(id);
    }

    @GetMapping("statisticperformancesale")
    public BaseResponse statisticPerformanceSale(@RequestParam String startDate, @RequestParam String endDate){
        return workService.statisticPerformanceSale(startDate, endDate);
    }
}
