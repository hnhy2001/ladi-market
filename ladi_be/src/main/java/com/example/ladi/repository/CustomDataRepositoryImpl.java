package com.example.ladi.repository;

import com.example.ladi.model.Account;
import com.example.ladi.model.Data;
import com.example.ladi.model.QData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.util.List;

@Repository
public class CustomDataRepositoryImpl implements CustomDataRepository{

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Data> finDataByConditions(String status, String startDate, String endDate, Account account, String shopCode, int page, int size) {
        JPAQuery<Data> queryData = new JPAQuery<>(entityManager);
        QData qData = QData.data;
        BooleanBuilder filter = new BooleanBuilder();
        String[] statusArray = status.split(",");

        for (String item : statusArray){
            filter.or(qData.status.eq(Integer.parseInt(item)));
        }
        if (shopCode != null){
            filter.and(qData.shopCode.eq(shopCode));
        }
        if (startDate != null && endDate != null){
            filter.and(qData.date.loe(Long.parseLong(endDate)));
            filter.and(qData.date.goe(Long.parseLong(startDate)));
        }
        if (account!= null){
            filter.and(qData.account.eq(account));
        }
        List<Data> dataList = queryData.from(qData).where(filter).orderBy(qData.id.desc()).offset(page*size).limit(size).fetch();
        return dataList;
    }

    @Override
    public List<Data> checkOut(String status, String startDate, String endDate, Account account, String shopCode){
        JPAQuery<Data> queryData = new JPAQuery<>(entityManager);
        QData qData = QData.data;
        BooleanBuilder filter = new BooleanBuilder();
        String[] statusArray = status.split(",");

        for (String item : statusArray){
            filter.or(qData.status.eq(Integer.parseInt(item)));
        }
        if (shopCode != null){
            filter.and(qData.shopCode.eq(shopCode));
        }
        filter.and(qData.dateChanged.loe(Long.parseLong(endDate)));
        filter.and(qData.dateChanged.goe(Long.parseLong(startDate)));
        if (account != null) {
            filter.and(qData.account.eq(account));
        }
        List<Data> dataList = queryData.from(qData).where(filter).orderBy(qData.id.desc()).fetch();
        return dataList;
    }

    @Override
    public List findDataByAccountNull(String status, String shopCode) {
        JPAQuery<Data> queryData = new JPAQuery<>(entityManager);
        QData qData = QData.data;
        BooleanBuilder filter = new BooleanBuilder();
        String[] statusArray = status.split(",");

        for (String item : statusArray){
            filter.or(qData.status.eq(Integer.parseInt(item)));
        }
        if (shopCode != null){
            filter.and(qData.shopCode.eq(shopCode));
        }
        filter.and(qData.account.isNull());
        List<Data> dataList = queryData.from(qData).where(filter).orderBy(qData.id.desc()).fetch();
        return dataList;
    }


    @Override
    public String value() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
