package com.example.ladi.controller;

import com.example.ladi.configurations.jdbc.JDBCConnection;
import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.AssignJobRequest;
import com.example.ladi.controller.request.AssignWorkRequest;
import com.example.ladi.dto.DataDto;
import com.example.ladi.extentions.datetimeExtention;
import com.example.ladi.model.Data;
import com.example.ladi.repository.CustomDataRepository;
import com.example.ladi.service.BaseService;
import com.example.ladi.service.DataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/data")
@CrossOrigin
public class DataController extends BaseController<Data>{

    @Autowired
    DataService dataService;

    @Autowired
    CustomDataRepository customDataRepository;
    
    @Override
    protected BaseService<Data> getService() {
        return dataService;
    }


    @GetMapping("")
    public BaseResponse getAllData(@RequestHeader(name = "Authorization") String jwt, @RequestParam String status, @RequestParam String startDate, @RequestParam String endDate, @RequestParam int page, @RequestParam int size, @RequestParam(name = "shopCode", required = false) String shopCode){

        return dataService.getAllData(jwt, status, startDate, endDate, shopCode, page, size);
    }

    @PostMapping("createData")
    public BaseResponse createData(@RequestBody Data data, @RequestParam(name = "shopCode", required = false) String shopCode){
        return dataService.createData(data, shopCode);
    }

    @PostMapping("assignWork")
    public BaseResponse assignWork(@RequestBody AssignJobRequest assignJobRequest){
        return dataService.assignWork(assignJobRequest);
    }

    @GetMapping("getAllDataAccountNull")
    public BaseResponse getAllBaseResponse(@RequestParam String status, @RequestParam(name = "shopCode", required = false) String shopCode){
        return dataService.getAllDataAccountNull(status, shopCode);
    }

    @GetMapping("statisticByUtmMedium")
    public BaseResponse statisticByUtmMedium(){
        return dataService.statisticByUtmMedium();
    }

    @GetMapping("statisticalRevenueByDay")
    public BaseResponse statisticalRevenueByDay(){
        return dataService.statisticcalrevenueByDay();
    }

    @GetMapping("thongKeUtm")
    public BaseResponse thongKeUtm(@RequestParam String startDate, @RequestParam String endDate, @RequestHeader(name = "Authorization") String jwt){
        return dataService.ketQuaThongKeUtm(startDate, endDate, jwt);
    }

    @GetMapping("thongKeTopUtm")
    public BaseResponse thongKeTopUtm(@RequestParam String startDate, @RequestParam String endDate){
        return dataService.ketQuaThongKeTopUtm(startDate, endDate);
    }

    @GetMapping("thongkedoanhthutheongay")
    public BaseResponse statisticalRevenueByDate(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String shopCode){
        return dataService.statisticalRevenueByDate(startDate, endDate, shopCode);
    }

    @GetMapping("thongkeutm")
    public BaseResponse statisticUtmByDate(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String shopCode){
        return dataService.statisticUtmByDate(startDate, endDate, shopCode);
    }

    @GetMapping("statisticdatabydateandstatus")
    public BaseResponse statisticDataByDateAndStatus(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String shopCode){
        return dataService.statisticDataByDateAndStatus(startDate, endDate, shopCode);
    }

    @GetMapping("stcquantity")
    public BaseResponse statisticQuantityDataByDateAndStatus(@RequestParam String startDate, @RequestParam String endDate){
        return dataService.statisticQuantityDataByDateAndStatus(startDate, endDate);
    }
}
