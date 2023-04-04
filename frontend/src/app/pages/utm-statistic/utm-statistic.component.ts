import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DanhMucService } from 'app/danhmuc.service';
import { NotificationService } from 'app/notification.service';
import { jqxPivotGridComponent } from 'jqwidgets-ng/jqxpivotgrid';
import { DateRanges, TimePeriod } from 'ngx-daterangepicker-material/daterangepicker.component';
import * as moment from 'moment';
import dayjs from 'dayjs/esm';
@Component({
    selector: 'utm-statistic-cmp',
    templateUrl: 'utm-statistic.component.html',
    styleUrls: ['./utm-statistic.component.scss']
})

export class UtmStatisticComponent implements OnInit {
    @ViewChild('pivotGrid1') pivotGrid1: jqxPivotGridComponent;

    REQUEST_URL = "/api/v1/data";

    listEntity = [];
    listEntityTop = [];
    total = 0;
    height: any = $(window).height()! - 240;
    pivotDataSource: null;
    dateRange: TimePeriod = {
        startDate: dayjs(),
        endDate: dayjs()
    };;

    ranges: DateRanges = {
        ['Hôm nay']: [dayjs(), dayjs()],
        ['Hôm qua']: [dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')],
        // ['7 Ngày qua']: [dayjs().subtract(6, 'days'), dayjs()],
        // ['30 Ngày qua']: [dayjs().subtract(29, 'days'), dayjs()],
        ['Tháng này']: [dayjs().startOf('month'), dayjs().endOf('month')],
        // ['Tháng trước']: [dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')],
        // ['3 Tháng trước']: [dayjs().subtract(3, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')]
    };

    source: any;
    dataAdapter: any;


    constructor(
        private dmService: DanhMucService,
        private notificationService: NotificationService,

    ) {
        this.source = {
            localdata: [],
            datatype: 'array',

            datafields: [
                { name: 'count', type: 'number' },
                { name: 'date', type: 'number' },
                { name: 'total', type: 'number' },
                { name: 'utmName', type: 'string' },
            ],
        };

        let dataAdapter = new jqx.dataAdapter(this.source);
        dataAdapter.dataBind();

        // create a pivot data source from the dataAdapter
        let pivotDataSource = new jqx.pivot(dataAdapter, {
            pivotValuesOnRows: true,
            totals: { rows: { subtotals: true, grandtotals: true }, columns: { subtotals: false, grandtotals: true,cellsalign: 'right', text:'Tổng' } },
            rows: [{ dataField: 'date', align: 'center' }],
            columns: [{ dataField: 'utmName', autoResize:false , align: 'center'}],
            values: [
                { dataField: 'count', text: 'UTM', autoResize:false  , align: 'center'},

            ],
        });
        this.pivotDataSource = pivotDataSource
    }
    ngOnInit(): void {
        this.loadDataTopMonth();
    }
    ngAfterViewInit(): void {
        // this.pivotGrid1.refresh();
      }

    public loadData() {
        var date = JSON.parse(JSON.stringify(this.dateRange));
        date.endDate = date.endDate.replace("23:59:59","00:00:00");
        let startDate = moment(date.startDate).format('YYYYMMDD');
        let endDate = moment(date.endDate).format('YYYYMMDD');
        this.total = 0;
        this.dmService.getOption(null, this.REQUEST_URL, "/thongKeTopUtm?startDate=" + startDate + "&endDate=" + endDate).subscribe(
            (res: HttpResponse<any>) => {
                this.listEntity = res.body.RESULT;
                this.listEntity.forEach(item => {
                    this.total += item.count;
                });
                // this.source.localdata = res.body.RESULT;
                // let dataAdapter = new jqx.dataAdapter(this.source);
                // dataAdapter.dataBind();

                // // create a pivot data source from the dataAdapter
                // let pivotDataSource = new jqx.pivot(dataAdapter, {
                //     pivotValuesOnRows: true,
                //     totals: { rows: { subtotals: true, grandtotals: true }, columns: { subtotals: false, grandtotals: true, cellsalign: 'right', text:'Tổng' } },
                //     rows: [{ dataField: 'date', align: 'center', autoResize: false}],
                //     columns: [{ dataField: 'utmName', autoResize: false, align: 'center'}],
                //     values: [
                //         { dataField: 'count',autoResize:false, text: '-' ,'function': 'sum', align: 'center', cellsalign: 'center'},

                //     ],
                // });
                // this.pivotDataSource = pivotDataSource
            },
            () => {
                console.error();
            }
        );
    }

    public loadDataTopMonth() {
        var date = JSON.parse(JSON.stringify(this.dateRange));
        let startDate = dayjs().startOf('month').format('YYYYMMDD');
        let endDate = dayjs().endOf('month').format('YYYYMMDD');
        this.dmService.getOption(null, this.REQUEST_URL, "/thongKeTopUtm?startDate=" + startDate + "&endDate=" + endDate).subscribe(
            (res: HttpResponse<any>) => {
                this.listEntityTop = res.body.RESULT;
            
            },
            () => {
                console.error();
            }
        );
    }

    createPivotDataSource(data: any): any {
        console.log(data);
        // create a data source and data adapter



    }
}
