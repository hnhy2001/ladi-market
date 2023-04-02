package com.example.ladi.model;

import com.example.ladi.dto.*;
import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@NamedNativeQuery(name = "Data.statisticcalrevenueByDay",
        query = "select date_only as date, sum(price) as price from data GROUP BY date_only ORDER BY date_only DESC LIMIT 50",
        resultSetMapping = "Mapping.StatisticalRevenueByDayDto")
@SqlResultSetMapping(name = "Mapping.StatisticalRevenueByDayDto", classes = @ConstructorResult(targetClass = StatisticalRevenueByDayDto.class
        , columns = {@ColumnResult(name = "date", type = Long.class),
                     @ColumnResult(name = "price", type = Double.class)}))

@NamedNativeQuery(name = "Data.thongKeUtmTheoThoiGian_admin",
                  query = "SELECT date_only as date, utm_medium as utmName,COUNT(id) as count  FROM `data` WHERE date_only <= :endDate AND date_only >= :startDate   GROUP BY utm_medium, date_only",
                  resultSetMapping = "Mapping.KetQuaThongKeUtmDto")

@NamedNativeQuery(name = "Data.thongKeUtmTheoThoiGian",
        query = "SELECT date_only as date, utm_medium as utmName,COUNT(id) as count  FROM `data` WHERE date_only <= :endDate AND date_only >= :startDate AND utm_medium in (:list) GROUP BY utm_medium, date_only",
        resultSetMapping = "Mapping.KetQuaThongKeUtmDto")
@SqlResultSetMapping(name = "Mapping.KetQuaThongKeUtmDto", classes = @ConstructorResult(targetClass = KetQuaThongKeUtmDto.class,
        columns = {@ColumnResult(name = "date", type = Long.class),
                @ColumnResult(name = "utmName", type = String.class),
                @ColumnResult(name = "count", type = int.class)}))

//@NamedNativeQuery(name = "Data.thongKeTopUtmTheoThoiGian",
//        query = "SELECT utm_medium as utmName, COUNT(id) as count From `data` WHERE date_only <= :endDate AND  date_only >= :startDate GROUP BY utm_medium ORDER BY count DESC",
//        resultSetMapping = "Mapping.KetQuaThongKeTopUtmDto")
@NamedNativeQuery(name = "Data.thongKeTopUtmTheoThoiGian",
        query = "SELECT a.username as utmName, COUNT(*) as count From `data` d,utm_medium um,account a  WHERE d.utm_medium = um.code AND um.nhanvienid = a.id AND date_only <= :endDate AND  date_only >= :startDate GROUP BY a.username ORDER BY count DESC",
        resultSetMapping = "Mapping.KetQuaThongKeTopUtmDto")
@SqlResultSetMapping(name = "Mapping.KetQuaThongKeTopUtmDto", classes = @ConstructorResult(targetClass = KetQuaThongKeTopUtmDto.class,
        columns = {
                @ColumnResult(name = "utmName", type = String.class),
                @ColumnResult(name = "count", type = int.class)}))

@NamedNativeQuery(name = "Data.statisticalRevenueByDate",
        query = "SELECT date_only as date, SUM(price) as revenue  from `data` WHERE date_only >= :startDate AND date_only <= :endDate AND status in (7,8) AND shopcode = :codeShop GROUP BY date_only",
        resultSetMapping = "Mapping.StatisticalRevenueByDateDto")
@SqlResultSetMapping(name = "Mapping.StatisticalRevenueByDateDto", classes = @ConstructorResult(targetClass = StatisticalRevenueByDateDto.class,
        columns = {@ColumnResult(name = "date", type = Long.class),
                @ColumnResult(name = "revenue", type = Long.class)}))

@NamedNativeQuery(name = "Data.statisticUtmByDate",
        query = "SELECT COUNT(id) as count, date_only as date, utm_medium as utmMedium, SUM(price) as price From `data` WHERE date_only <= :endDate AND date_only >= :startDate AND shopcode = :shopCode And status in (7,8) GROUP By date_only, utm_medium  ",
        resultSetMapping = "Mapping.StatisticUtmByDateDto")
@SqlResultSetMapping(name = "Mapping.StatisticUtmByDateDto", classes = @ConstructorResult(targetClass = StatisticUtmByDateDto.class,
        columns = {@ColumnResult(name = "count", type = Integer.class),
                @ColumnResult(name = "date", type = Long.class),
                @ColumnResult(name = "utmMedium", type = String.class),
                @ColumnResult(name = "price", type = Long.class),
        }))
@NamedNativeQuery(name = "Data.statisticDataByDateAndStatus",
        query = "SELECT COUNT(id) as count, SUM(price) as sum, status  from `data` WHERE date_only <= :endDate and date_only >= :startDate AND shopcode = :shopCode GROUP BY status ",
        resultSetMapping = "Mapping.StatisticDataByDateAndStatusDto")
@SqlResultSetMapping(name = "Mapping.StatisticDataByDateAndStatusDto", classes = @ConstructorResult(targetClass = StatisticDataByDateAndStatusDto.class,
        columns = {@ColumnResult(name = "count", type = Integer.class),
                @ColumnResult(name = "sum", type = long.class),
                @ColumnResult(name = "status", type = Integer.class)}))

@NamedNativeQuery(name = "Data.statisticQuantityDataByDateAndStatus",
        query = "SELECT COUNT(id) as count, status  from `data` WHERE date <= :endDate and date >= :startDate GROUP BY status ",
        resultSetMapping = "Mapping.StatisticDataByDateAndStatusDto")

@Entity
@Table(name = "data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Data {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "street")
    private String street;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "product")
    private String product;

    @Column(name = "status")
    private  int status;

    @Column(name = "date")
    private Long date;

    @Column(name = "date_only")
    private Long dateOnly;

    @Column(name = "date_changed_only")
    private Long dateChangedOnly;

    @Column(name = "link")
    private String link;

    @Column(name = "ipaddress")
    private String ipAddress;

    @Column(name = "datechanged")
    private Long dateChanged;

    @Column(name = "utm_source")
    private String utm_source;

    @Column(name = "utm_medium")
    private String utm_medium;

    @Column(name = "utm_campaign")
    private String utm_campation;

    @Column(name = "utm_term")
    private String utm_term;

    @Column(name = "utm_content")
    private String utm_content;

    @Column(name = "variant_url ")
    private String variant_url;

    @Column(name = "price")
    private Double price;

    @Column(name = "message")
    private String message;

    @Column(name = "note")
    private String note;

    @Column(name = "shopcode")
    private String shopCode;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "saleid")
    Account saleAccount;

    @ManyToOne
    @JoinColumn(name = "nhanvienid")
    Account account;

    @ManyToOne
    @JoinColumn(name = "productid")
    Product productDto;
}
