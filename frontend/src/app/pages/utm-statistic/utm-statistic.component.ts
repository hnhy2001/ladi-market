import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DanhMucService } from 'app/danhmuc.service';
import { NotificationService } from 'app/notification.service';
import{ jqxPivotGridComponent } from 'jqwidgets-ng/jqxpivotgrid';   
@Component({
    selector: 'utm-statistic-cmp',
    templateUrl: 'utm-statistic.component.html'
})

export class UtmStatisticComponent implements OnInit {
    @ViewChild('pivotGrid1') pivotGrid1: jqxPivotGridComponent;
    
    REQUEST_URL = "/api/v1/data";

    listEntity = [];
    listCode = [];
    listData = [];
    listDate = [];

    height: any = $(window).height()! - 240;
    pivotDataSource: null;

    
    constructor(
        private dmService: DanhMucService,
        private notificationService: NotificationService,
       
    ) {
        this.pivotDataSource = this.createPivotDataSource();
    }
    ngOnInit(): void {
        this.loadData();
    }

    getWidth(): any {
        if (document.body.offsetWidth < 850) {
          return '90%';
        }
    
        return 850;
      }
    
    public loadData() {
        this.dmService.getOption(null, this.REQUEST_URL, "/statisticByUtmMedium").subscribe(
            (res: HttpResponse<any>) => {
                this.listCode = res.body.RESULT.code;
                this.listData = res.body.RESULT.data;
                this.listDate = this.listData[0];
            },
            () => {
                console.error();
            }
        );
    }

    createPivotDataSource(): any {
        // prepare sample data
        let data = new Array();
       
        data = [
          { name: 'hung', date: 29, count: 1 },
          { name: 'hung', date: 30, count: 2 },
          { name: 'ha', date: 31, count: 2 },
        ];
    
        // create a data source and data adapter
        let source = {
          localdata: data,
          datatype: 'array',
          
          datafields: [
            { name: 'count', type: 'number' },
            { name: 'date', type: 'number' },
            { name: 'total', type: 'number' },
            { name: 'name', type: 'string' },
          ],
        };
    
        let dataAdapter = new jqx.dataAdapter(source);
        dataAdapter.dataBind();
    
        // create a pivot data source from the dataAdapter
        let pivotDataSource = new jqx.pivot(dataAdapter, {
          pivotValuesOnRows: true,
          totals: {rows: {subtotals: true, grandtotals: true}, columns: {subtotals: false, grandtotals: true}},
          rows: [{ dataField: 'date'}],
          columns: [{ dataField: 'name' , width: '50%'}],
          values: [
            { dataField: 'count', text:' '},

          ],
        });
    
        return pivotDataSource;
      }
}
