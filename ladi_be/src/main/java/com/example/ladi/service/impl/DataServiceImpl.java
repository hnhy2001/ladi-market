package com.example.ladi.service.impl;

import com.example.ladi.configurations.JwtTokenProvider;
import com.example.ladi.controller.reponse.BaseResponse;
import com.example.ladi.controller.request.AssignJobRequest;
import com.example.ladi.controller.request.DataRequest;
import com.example.ladi.dto.*;
import com.example.ladi.model.*;
import com.example.ladi.repository.*;
import com.example.ladi.service.DataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DataServiceImpl extends BaseServiceImpl<Data> implements DataService {
    @Autowired
    DataRepository dataRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UtmMediumRepository utmMediumRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomDataRepository customDataRepository;

    @Autowired
    CustomWorkRepository customWorkRepository;

    @Autowired
    ConfigRepository configRepository;
    @Override
    protected BaseRepository<Data> getRepository() {
        return dataRepository;
    }

    @Override
    public BaseResponse getAllData(String jwt, String status, String startDate, String endDate, String shopCode, int page, int size) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String bearerToken = getJwtFromRequest(jwt);
        String userName = jwtTokenProvider.getAccountUserNameFromJWT(bearerToken);
        Account account = accountRepository.findByUserName(userName);
        List<DataDto> dataDtoList = new ArrayList<>();
        if (account.getRole().equals("admin")) {
            List<Data> dataList = customDataRepository.finDataByConditions(status, startDate, endDate, null, shopCode, page, size);
//            for (int i = 0; i < dataList.size(); i++) {
//                if (dataList.get(i).getAccount() == null){
//                    DataDto dataDto = modelMapper.map(dataList.get(i), DataDto.class);
//                    dataDtoList.add(dataDto);
//                }
//                else {
//                    AccountDto accountDto = new AccountDto(dataList.get(i).getAccount().getId(), dataList.get(i).getAccount().getUserName(), dataList.get(i).getAccount().getFullName(), dataList.get(i).getAccount().getShop(), dataList.get(i).getAccount().getRole());
//                    DataDto dataDto = modelMapper.map(dataList.get(i), DataDto.class);
//                    dataDto.setAccount(accountDto);
//                    dataDtoList.add(dataDto);
//                }
//            }
            return new BaseResponse(200, "OK", dataList);
        }else {
            List<Data> dataList = customDataRepository.finDataByConditions(status, startDate, endDate, account, shopCode, page, size);
//            for (int i = 0; i<dataList.size(); i++){
//                AccountDto accountDto = modelMapper.map(dataList.get(i).getAccount(), AccountDto.class);
//                DataDto dataDto = modelMapper.map(dataList.get(i), DataDto.class);
//                dataDto.setAccount(accountDto);
//                dataDtoList.add(dataDto);
//            }
            return new BaseResponse(200, "OK", dataList);
        }

    }

    @Override
    public BaseResponse  createData(Data data, String shopCode) {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat dateOnlyFormatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        dateOnlyFormatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Long date = Long.parseLong(formatter.format(nowDate));
        Long dateOnly = Long.parseLong(dateOnlyFormatter.format(nowDate));
        data.setDate(date);
        data.setDateOnly(dateOnly);
        data.setDateChangedOnly(dateOnly);
        data.setShopCode(shopCode);
        if(data.getUtm_medium() == null || data.getUtm_medium().equals("")){
            data.setUtm_medium("UNDEFINE");
        }
        List<Work> workList = customWorkRepository.finWorkByConditions(null, null, null, shopCode, 1);
        DataDto dataDto = new DataDto();
        if (!workList.isEmpty()){
            Random rand = new Random();
            int ranNum = rand.nextInt(workList.size());
            data.setAccount(workList.get(ranNum).getAccount());
            data.setStatus(1);
            data.setDateChanged(date);
            AccountDto accountDto = modelMapper.map(workList.get(ranNum).getAccount(), AccountDto.class);
            dataDto = modelMapper.map(data, DataDto.class);
            dataDto.setAccount(accountDto);
        }
        dataRepository.save(data);
        return new BaseResponse(200, "OK", dataDto);
    }
    
    @Override
    public BaseResponse assignWork(AssignJobRequest assignJobRequest) {
        ArrayList<Data> datas = new ArrayList<>();
        for (DataRequest data : assignJobRequest.getDataList()){
            Account account = accountRepository.findAllById(data.getNhanVienId());
            Data dataResult = modelMapper.map(data, Data.class);
            dataResult.setAccount(account);
            if (data.getStatus() == 7){
                Account saleAccount  = accountRepository.findAllById(data.getSaleId());
                dataResult.setSaleAccount(saleAccount);
                Config config = configRepository.findAllByCode("CPVC");
                if (dataResult.getDateOnly() < config.getFromDate() || dataResult.getDateOnly() > config.getToDate()){
                     dataResult.setCost(Double.parseDouble(config.getDefaultValue()));
                }else {
                    dataResult.setCost(Double.parseDouble(config.getValue()));
                }
            }
            datas.add(dataResult);

        }
        dataRepository.saveAll(datas);
        return new BaseResponse(200, "Success!", null);
    }

    @Override
    public BaseResponse getAllDataAccountNull(String status, String shopCode) {
        List<Data> dataList = customDataRepository.findDataByAccountNull(status, shopCode);
        return new BaseResponse(200, "OK", dataList);
    }

    private String getJwtFromRequest(String bearerToken) {
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public BaseResponse statisticByUtmMedium() {
        List <UtmMedium> utmMediumList = utmMediumRepository.findAll();
        List <String> codeList = new ArrayList<>();
        codeList.add("ALL");
        List <List<Object>> dataList = new ArrayList<>();
        dataList.add(dataRepository.statisticUtmMedium());
        Map<String,List> map = new HashMap<String,List>();
        for (int i = 0;i<utmMediumList.size();i++){
            codeList.add(utmMediumList.get(i).getCode());
            dataList.add(dataRepository.statisticUtmMediumByMedium(utmMediumList.get(i).getCode()));
        }
        map.put("code",codeList);
        map.put("data",dataList);
        return new BaseResponse(200, "OK", map);
    }

    @Override
    public BaseResponse statisticcalrevenueByDay() {
        List<StatisticalRevenueByDayDto> dataList = dataRepository.statisticcalrevenueByDay();
        List<StatisticalRevenueByDayResult> resultList = new ArrayList<>();
        for(StatisticalRevenueByDayDto item : dataList){
            StatisticalRevenueByDayResult statisticalRevenueByDayResult = new StatisticalRevenueByDayResult();
            statisticalRevenueByDayResult.setDate(item.getDate());
            if (item.getPrice() != null){
                statisticalRevenueByDayResult.setPrice(String.format("%.0f", item.getPrice()));
            }else {
                statisticalRevenueByDayResult.setPrice("0");
            }
            resultList.add(statisticalRevenueByDayResult);
        }
        return new BaseResponse(200, "Test", resultList);
    }

    @Override
    public BaseResponse ketQuaThongKeUtm(String startDate, String endDate, String jwt) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String bearerToken = getJwtFromRequest(jwt);
        String userName = jwtTokenProvider.getAccountUserNameFromJWT(bearerToken);
        Account account = accountRepository.findByUserName(userName);
        List<String> utmCodeList = new ArrayList<>();
        List<KetQuaThongKeUtmDto> listResult = new ArrayList<>();
        if (account.getRole().equals("admin") || account.getRole().equals("marketing")) {
            listResult = dataRepository.thongKeUtmTheoThoiGian_admin(Long.parseLong(startDate), Long.parseLong(endDate));
        }
//        else if (account.getRole().equals("marketing")) {
//            List<UtmMedium> utmMediumList = utmMediumRepository.findAllByAccount(account);
//            if (utmMediumList.isEmpty()){
//                return new BaseResponse(200, "OK", null);
//            }
//            for (UtmMedium item : utmMediumList){
//                utmCodeList.add(item.getCode().toLowerCase());
//            }
//            listResult = dataRepository.thongKeUtmTheoThoiGian(Long.parseLong(startDate), Long.parseLong(endDate), utmCodeList);
//        }
        else {
            return new BaseResponse(200, "OK", null);
        }
        return new BaseResponse(200, "OK", listResult);
    }

    @Override
    public BaseResponse ketQuaThongKeTopUtm(String startDate, String endDate) {

        return new BaseResponse(200, "OK", dataRepository.thongKeTopUtmTheoThoiGian(Long.parseLong(startDate), Long.parseLong(endDate)));
    }

    @Override
    public BaseResponse statisticalRevenueByDate(String startDate, String endDate, String shopCode) {
        return new BaseResponse(200, "OK", dataRepository.statisticalRevenueByDate(Long.parseLong(startDate), Long.parseLong(endDate), shopCode));
    }

    @Override
    public BaseResponse statisticUtmByDate(String startDate, String endDate, String shopCode) {
        return new BaseResponse(200, "OK", dataRepository.statisticUtmByDate(Long.parseLong(startDate), Long.parseLong(endDate), shopCode));
    }

    @Override
    public BaseResponse statisticDataByDateAndStatus(String startDate, String endDate, String shopCode) {
        return new BaseResponse(200, "OK", dataRepository.statisticDataByDateAndStatus(Long.parseLong(startDate), Long.parseLong(endDate), shopCode));
    }

    @Override
    public BaseResponse statisticQuantityDataByDateAndStatus(String startDate, String endDate) {
        return new BaseResponse(200, "OK", dataRepository.statisticQuantityDataByDateAndStatus(Long.parseLong(startDate), Long.parseLong(endDate)));
    }
}
