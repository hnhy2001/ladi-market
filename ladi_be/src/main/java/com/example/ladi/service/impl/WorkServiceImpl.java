package com.example.ladi.service.impl;

import com.example.ladi.configurations.JwtTokenProvider;
import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.CheckOutRequest;
import com.example.ladi.controller.request.CheckWorkActiveRequest;
import com.example.ladi.controller.request.CreateWorkRequest;
import com.example.ladi.dto.AccountDto;
import com.example.ladi.dto.StatisticPerformanceSaleDto;
import com.example.ladi.dto.StatisticUtmByDateDto;
import com.example.ladi.dto.WorkDto;
import com.example.ladi.model.Account;
import com.example.ladi.model.Data;
import com.example.ladi.model.Work;
import com.example.ladi.repository.*;
import com.example.ladi.service.WorkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class WorkServiceImpl extends BaseServiceImpl<Work> implements WorkService {
    @Autowired
    WorkRepository workRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomWorkRepository customWorkRepository;

    @Autowired
    CustomDataRepository customDataRepository;

    @Autowired
    DataRepository dataRepository;

    @Override
    protected BaseRepository<Work> getRepository() {
        return workRepository;
    }

    @Override
    public BaseResponse createWork(CreateWorkRequest createWorkRequest) {
        Account account = accountRepository.findAllById(createWorkRequest.getNhanVienId());
        if (account == null){
            return new BaseResponse(500, "Account not found", "Create Fail");
        }
        if(workRepository.findAllByIsActiveAndAccount(1, account) != null) {
            return new BaseResponse(500, "Tài khoản hiện đã đăng nhập ở thiết bị khác", "Create Fail");
        }

        Work work = new Work(createWorkRequest.getTimeIn(), createWorkRequest.getTimeOut(), createWorkRequest.getDonGiao(), createWorkRequest.getDonHoanThanh(), createWorkRequest.getDonXuLy(), createWorkRequest.getGhiChu(), 1, createWorkRequest.getDonThanhCong(),  account);
        workRepository.save(work);
        return new BaseResponse(200, "OK", work.getId());
    }

    @Override
    public BaseResponse getAllWork(String jwt, String startDate, String endDate) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String bearerToken = getJwtFromRequest(jwt);
        String userName = jwtTokenProvider.getAccountUserNameFromJWT(bearerToken);
        Account account = accountRepository.findByUserName(userName);
        List<WorkDto> workDtoList = new ArrayList<>();
        if (account.getRole().equals("admin")){
            List<Work> workList = customWorkRepository.finWorkByConditions(startDate, endDate, null, null, 0);
            for (int i = 0; i<workList.size(); i++){
                AccountDto accountDto = modelMapper.map(workList.get(i).getAccount(), AccountDto.class);
                WorkDto workDto = modelMapper.map(workList.get(i),WorkDto.class);
                workDto.setAcount(accountDto);
                workDtoList.add(workDto);
            }
        }
        else{
            List<Work> workList = customWorkRepository.finWorkByConditions(startDate, endDate, account, null, 0);
            for (int i = 0 ; i<workList.size(); i++){
                AccountDto accountDto = modelMapper.map(workList.get(i).getAccount(),AccountDto.class);
                WorkDto workDto = modelMapper.map(workList.get(i),WorkDto.class);
                workDto.setAcount(accountDto);
                workDtoList.add(workDto);
            }
        }
        return new BaseResponse(200, "OK", workDtoList);

    }

    @Override
    public BaseResponse checkOut(CheckOutRequest checkOutRequest, String shopCode) {
        Work work = workRepository.findAllById(checkOutRequest.getId());
        if (work == null){
            return new BaseResponse(500, "Work not found", "Checkout Fail");
        }
        List<Data> dataList = customDataRepository.checkOut("0,1,2,3,4,5,6,7", "0", String.valueOf(checkOutRequest.getTimeOut()), work.getAccount(), shopCode);
        for (int i = 0; i<dataList.size(); i++){
            if (dataList.get(i).getStatus() == 1 || dataList.get(i).getStatus() == 2){
                dataList.get(i).setStatus(0);
            }
            dataList.get(i).setAccount(null);
        }
//
        work.setTimeOut(checkOutRequest.getTimeOut());
        work.setDonGiao(checkOutRequest.getDonGiao());
        work.setDonHoanThanh(checkOutRequest.getDonHoanThanh());
        work.setDonXuLy(checkOutRequest.getDonXuLy());
        work.setDonThanhCong(checkOutRequest.getDonThanhCong());
        work.setIsActive(-1);
        workRepository.save(work);
        return new BaseResponse(200, "OK", "Checkout Success");
    }

    @Override
    public BaseResponse getAllActive(String shopCode) {
        List<Work> workList = customWorkRepository.finWorkByConditions(null, null, null, shopCode, 1);
        List<WorkDto> workDtoList = new ArrayList<>();
        for (int i = 0 ; i<workList.size(); i++){
            AccountDto accountDto = new AccountDto(workList.get(i).getAccount().getId(), workList.get(i).getAccount().getUserName(), workList.get(i).getAccount().getUserName(), workList.get(i).getAccount().getShop(), workList.get(i).getAccount().getRole());
            WorkDto workDto = new WorkDto(workList.get(i).getId(), workList.get(i).getTimeIn(), workList.get(i).getTimeOut(), workList.get(i).getDonGiao(), workList.get(i).getDonHoanThanh(), workList.get(i).getDonXuLy(), workList.get(i).getGhiChu(),  workList.get(i).getDonThanhCong(),accountDto);
            workDtoList.add(workDto);
        }
        return new BaseResponse(200, "OK", workDtoList);
    }

    @Override
    public BaseResponse checkWorkActive(CheckWorkActiveRequest checkWorkActive, String shopCode) {
        Account account = accountRepository.findAllById(checkWorkActive.getNhanVienId());
        if (account == null){
            return new BaseResponse(500, "Account Not Found!", null);
        }
        Work work = workRepository.findAllByIsActiveAndAccount(1, account);
        if (work == null){
            return new BaseResponse(500, "NOT ACCOUNT CHECKING!", null);
        }
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Long date = Long.parseLong(formatter.format(nowDate));
        Long startDate = work.getTimeIn();
        Long endDate = date;
        int donGiao = customDataRepository.checkOut("0,1,2,3,4,5,6,7,8", String.valueOf(startDate), String.valueOf(endDate), account, shopCode).size();
        int donHoanThanh = customDataRepository.checkOut("2,3,4,5,6,7,8", String.valueOf(startDate), String.valueOf(endDate), account, shopCode).size();
        int donXuLy = customDataRepository.checkOut("2", String.valueOf(startDate), String.valueOf(endDate), account, shopCode).size();
        int donThanhCong = customDataRepository.checkOut("7,8", String.valueOf(startDate), String.valueOf(endDate), account, null).size();
        work.setDonGiao(donGiao);
        work.setDonHoanThanh(donHoanThanh);
        work.setDonXuLy(donXuLy);
        work.setDonThanhCong(donThanhCong);
        work.setTimeOut(endDate);
        AccountDto accountDto = modelMapper.map(work.getAccount(), AccountDto.class);
        WorkDto workDto = modelMapper.map(work, WorkDto.class);
        workDto.setAcount(accountDto);
        return new BaseResponse(200, "OK", workDto);
    }

    @Override
    public BaseResponse infoCheckout(Long id) {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Long date = Long.parseLong(formatter.format(nowDate));
        Work work = workRepository.findById(id).get();
        Long startDate = work.getTimeIn();
        Long endDate = date;
//        int donGiao = customDataRepository.finDataByConditions("0,1,2,3,4,5,6,7", String.valueOf(startDate), String.valueOf(endDate), work.getAccount()).size();
//        int donHoanThanh = customDataRepository.finDataByConditions("2", String.valueOf(startDate), String.valueOf(endDate), work.getAccount()).size();
//        work.setDonGiao(donGiao);
//        work.setDonHoanThanh(donHoanThanh);
        work.setTimeOut(endDate);
        AccountDto accountDto = modelMapper.map(work.getAccount(), AccountDto.class);
        WorkDto workDto = modelMapper.map(work, WorkDto.class);
        workDto.setAcount(accountDto);
        return new BaseResponse(200, "OK", workDto);
    }

    @Override
    public BaseResponse statisticPerformanceSale(String startDate, String endDate) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<StatisticPerformanceSaleDto> statisticPerformanceSaleDto = workRepository.statisticPerformanceSale(Long.parseLong(startDate), Long.parseLong(endDate));
        List<StatisticPerformanceSaleDto> resultList = statisticPerformanceSaleDto.stream().map((item) -> {
            item.setPercentDonThanhCong(df.format((item.getDonThanhCong()/(item.getDonHoanThanh()*1.0))*100));
            item.setPercentDonHoanThanh(df.format((item.getDonHoanThanh()/(item.getDonGiao()*1.0))*100));
            return item;
        }).collect(Collectors.toList());

        return new BaseResponse(200, "OK", resultList);
    }

    private String getJwtFromRequest(String bearerToken) {
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
