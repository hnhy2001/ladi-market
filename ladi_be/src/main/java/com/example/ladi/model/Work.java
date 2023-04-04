package com.example.ladi.model;
import com.example.ladi.dto.StatisticPerformanceSaleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NamedNativeQuery(name = "Work.statisticPerformanceSale",
        query = "SELECT a.fullname as fullName, SUM(w.don_giao) as donGiao , SUM(w.don_hoan_thanh) as donHoanThanh , SUM(w.don_thanh_cong) as donThanhCong FROM `work` w INNER JOIN account a ON w.nhanvienid = a.id " +
                "WHERE w.timeint >= :startDate AND w.timeout <= :endDate GROUP BY a.fullname ",
        resultSetMapping = "Mapping.StatisticPerformanceSaleDto")
@SqlResultSetMapping(name = "Mapping.StatisticPerformanceSaleDto", classes = @ConstructorResult(targetClass = StatisticPerformanceSaleDto.class,
        columns = {@ColumnResult(name = "fullName", type = String.class),
                @ColumnResult(name = "donGiao", type = Integer.class),
                @ColumnResult(name = "donHoanThanh", type = Integer.class),
                @ColumnResult(name = "donThanhCong", type = Integer.class)}))
@Entity
@Table(name = "work")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Work extends BaseEntity{

    @Column(name = "timeint")
    private Long timeIn;

    @Column(name = "timeout")
    private Long timeOut;

    @Column(name = "don_giao")
    private int donGiao;

    @Column(name = "don_hoan_thanh")
    private int donHoanThanh;

    @Column(name = "don_xu_ly")
    private int donXuLy;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "active")
    private int isActive;

    @Column(name = "don_thanh_cong")
    private int donThanhCong;

    @ManyToOne
    @JoinColumn(name = "nhanvienid", nullable = false)
    Account account;
}
