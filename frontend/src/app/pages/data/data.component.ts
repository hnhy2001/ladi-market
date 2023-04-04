import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild, ElementRef , AfterViewInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DanhMucService } from 'app/danhmuc.service';
import { ConfirmationDialogService } from 'app/layouts/confirm-dialog/confirm-dialog.service';
import { NotificationService } from 'app/notification.service';
import { ChuyenTrangThaiPopUpComponent } from 'app/shared/popup/chuyen-trang-thai-pop-up/chuyen-trang-thai-pop-up.component';
import { GiaoViecPopUpComponent } from 'app/shared/popup/giao-viec-pop-up/giao-viec-pop-up.component';
import { TongKetDuLieuPopupComponent } from 'app/shared/popup/tong-ket-du-lieu/TongKetDuLieuPopup.component';
import { TuDongGiaoViecComponent } from 'app/shared/popup/tu-dong-giao-viec/tu-dong-giao-viec.component';
import { XuLyDuLieuPopupComponent } from 'app/shared/popup/xu-ly-du-lieu/XuLyDuLieuPopup.component';
import DateUtil from 'app/shared/util/date.util';
import dayjs from 'dayjs/esm';

import { jqxGridComponent } from 'jqwidgets-ng/jqxgrid';
import * as moment from 'moment';
import { DateRanges, TimePeriod } from 'ngx-daterangepicker-material/daterangepicker.component';
import { LocalStorageService } from 'ngx-webstorage';
import * as XLSX from 'xlsx';  

@Component({
    selector: 'data-cmp',
    templateUrl: 'data.component.html'
})

export class DataComponent implements OnInit, AfterViewInit{
    @ViewChild('gridReference') myGrid: jqxGridComponent;
    @ViewChild('TABLE', { static: false }) TABLE: ElementRef;  
    // grid
    source: any
    dataAdapter: any;
    page = 0;
    size = 25;

    columns: any[] =
    [
        {
            text: '#', sortable: false, filterable: false, editable: false, width: '5%',
            groupable: false, draggable: false, resizable: false,
            datafield: '', columntype: 'number', 
            cellsrenderer: (row: number, column: any, value: number): string => {
                return '<div style="position: relative;top: 50%;left: 4px;transform: translateY(-50%);">' + (this.size*this.page + value + 1) + '</div>';
            }
        },
        { text: 'Ngày', editable: false, datafield: 'ngay', width: '10%'},
        { text: 'Tên KH', editable: false, datafield: 'name', width: '15%'},
        { text: 'SĐT', editable: false, datafield: 'phone' , width: '10%'},
        { text: 'Sản phẩm',editable:false ,datafield: 'product' , width: '20%'},
        { text: 'Trạng thái', editable: false, datafield: 'status' ,  width: '5%',cellsrenderer: (row: number, column: any, value: number): string => {
            switch (value){
                case 0: 
                {
                    return '<div class="div-center bg-light">' + 'Mới' + '</div>';
                }
                case 1: 
                {
                    return '<div class = "bg-info div-center text-white">' + 'Đã tiếp nhận' + '</div>';
                }
                case 2: 
                {
                    return '<div class = "bg-primary div-center text-white">' + 'Đang xử lý' + '</div>';
                }
                case 3: 
                {
                    return '<div class = "bg-warning div-center">' + 'KNM L1' + '</div>';
                }
                case 4: 
                {
                    return '<div class = "bg-warning div-center">' + 'KNM L2' + '</div>';
                }
                case 5: 
                {
                    return '<div class = "bg-warning div-center">' + 'KNM L3' + '</div>';
                }
                case 6: 
                {
                    return '<div class = "bg-danger div-center text-white">' + 'Thất bại' + '</div>';
                }
                case 7: 
                {
                    return '<div class = "bg-success div-center text-white">' + 'Thành công' + '</div>';
                }
                case 8: 
                {
                    return '<div class = "bg-success div-center text-white">' + 'Đã in đơn' + '</div>';
                }
                default:
                {
                    return '<div></div>';
                }
            }
        }},
        { text: 'Nhân viên', editable: false, datafield: 'nhanvien' ,  width: '12%'},
        { text: 'Doanh số',editable:false ,datafield: 'price' , width: '10%', cellsrenderer: (row: number, column: any, value: number): string => 
        {
            return '<div>' + value.toLocaleString('vi', {style : 'currency', currency : 'VND'}) + '</div>'
        }
        },
        { text: 'Chi phí', editable: false, datafield: 'cost' ,  width: '10%'},
        // { text: 'Nguồn', editable: false, datafield: 'link' , width: '30%'},
        // { text: 'Địa chỉ', editable: false, datafield: 'street' , width: '10%'},
        // { text: 'Xã', editable: false, datafield: 'ward' , width: '8%'},
        // { text: 'Huyện', editable: false, datafield: 'district' ,  width: '8%'},
        // { text: 'Tỉnh', editable: false, datafield: 'state' ,  width: '8%'},
        // { text: 'Ghi chú', editable: false, datafield: 'message' , width: '12%'},

    ];
    height: any = $(window).height()! - 240;
    localization: any = {
      pagergotopagestring: 'Trang',
      pagershowrowsstring: 'Hiển thị',
      pagerrangestring: ' của ',
      emptydatastring: 'Không có dữ liệu hiển thị',
      filterstring: 'Nâng cao',
      filterapplystring: 'Áp dụng',
      filtercancelstring: 'Huỷ bỏ'
    };
    pageSizeOptions = ['10', '25', '50', '100'];
    // chung
    REQUEST_URL ="/api/v1/data";
    listEntity = [];
    selectedEntity:any;
    public searchKey = '';
    statusDto: any = '';
    data:any = [];
    // date
    dateRange: TimePeriod = {
        startDate: dayjs(),
        endDate: dayjs()
      };;
    date: object;
    ranges: DateRanges = {
        ['Hôm nay']: [dayjs(), dayjs()],
        ['Hôm qua']: [dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')],
        ['7 Ngày qua']: [dayjs().subtract(6, 'days'), dayjs()],
        ['30 Ngày qua']: [dayjs().subtract(29, 'days'), dayjs()],
        ['Tháng này']: [dayjs().startOf('month'), dayjs().endOf('month')],
        ['Tháng trước']: [dayjs().subtract(1, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')],
        ['3 Tháng trước']: [dayjs().subtract(3, 'month').startOf('month'), dayjs().subtract(1, 'month').endOf('month')]
    };

    info:any;
    countList = [0,0,0,0,0,0,0,0];
    shopCode = '';
    tongDoanhSo = 0;
    tongDon = 0;

    constructor(
        private dmService: DanhMucService,
        private notificationService: NotificationService,
        private confirmDialogService: ConfirmationDialogService,
        private modalService: NgbModal,
        private localStorage: LocalStorageService,
        private route: ActivatedRoute
    ){
        this.source =
        {
            localdata: [],
            datafields:
            [
                { name: 'id', type: 'number' },
                { name: 'name', type: 'string' },
                { name: 'phone', type: 'string' },
                { name: 'street', type: 'string' },
                { name: 'country', type: 'string' },
                { name: 'ward', type: 'string' },
                { name: 'state', type: 'string' },
                { name: 'district', type: 'string' },
                { name: 'status', type: 'number' },
                { name: 'date', type: 'string'},
                { name: 'product', type: 'string' },
                { name: 'link', type: 'string' },
                { name: 'nhanvien', type: 'string' },
                { name: 'ngay', type: 'string' },
                { name: 'message', type: 'string' },
                { name: 'ipAddress', type: 'string' },
                { name: 'dateChanged', type: 'string' },
                { name: 'staffName', type: 'string' },
                { name: 'price', type: 'number' },
                { name: 'nhanVienId', type: 'number' },
                { name: 'shopCode', type:'string'},
                { name: 'cost', type:'double'}
            ],
            id:'id',
            datatype: 'array'
        };
        this.dataAdapter = new jqx.dataAdapter(this.source);

        this.info = this.localStorage.retrieve("authenticationToken");

        if(this.info && this.info.role === 'user') {
            this.dateRange = {
                startDate: dayjs().subtract(6, 'days'),
                endDate: dayjs().add(1, 'days')
              };;
        }
    }

    ngOnInit(){
        const interval = setInterval(() => {
            this.loadData()
        }, 300000);

        this.route.queryParams
        .subscribe(params => {
            console.log(params); // { orderby: "price" }
            this.shopCode = params.shopCode;
            this.loadData();
        }
        );
    }

    ngAfterViewInit(): void {
        this.myGrid.pagesizeoptions(this.pageSizeOptions);
      }

      getByStatus(e):void{
        this.statusDto = e;
        this.loadData();
      }

    public loadData(){
        if(!this.shopCode)
        return;
        var date = JSON.parse(JSON.stringify(this.dateRange));
        date.endDate = date.endDate.replace("23:59:59","00:00:00");
        let startDate = moment(date.startDate).format('YYYYMMDD') + '000000';
        let endDate = moment(date.endDate).format('YYYYMMDD') + '235959';
        const status = (this.statusDto !== '') ? this.statusDto : '0,1,2,3,4,5,6,7,8';
        this.dmService.getOption(null, this.REQUEST_URL,"?status=" + status + '&startDate=' + startDate + '&endDate=' + endDate +'&shopCode='+this.shopCode+'&page='+this.page+'&size='+this.size ).subscribe(
            (res: HttpResponse<any>) => {
              setTimeout(() => {
                this.listEntity = res.body.RESULT;
                this.data = this.customDate(res.body.RESULT,this.statusDto)
                this.source.localdata = this.data;
                this.dataAdapter = new jqx.dataAdapter(this.source);
              }, 100);
            },
            () => {
              console.error();
            }
          );
        this.loadDataCount();
    }

    loadDataCount(){
        var date = JSON.parse(JSON.stringify(this.dateRange));
        date.endDate = date.endDate.replace("23:59:59", "00:00:00");
        const startDate = moment(date.startDate).format('YYYYMMDD');
        const endDate = moment(date.endDate).format('YYYYMMDD');
        this.countList = [0,0,0,0,0,0,0,0,0];
        this.tongDoanhSo = 0;
        this.tongDon = 0;
        this.dmService.getOption(null, this.REQUEST_URL, "/statisticdatabydateandstatus?startDate=" + startDate + "&endDate=" + endDate + "&shopCode=" + this.shopCode).subscribe(
            (res: HttpResponse<any>) => {
                const result = res.body.RESULT;
                for (let index = 0; index < result.length; index++) {
                    this.countList[Number(result[index].status)] = Number(result[index].count);
                    this.tongDoanhSo += Number(result[index].sum);
                    this.tongDon += Number(result[index].count);
                }
            },
            () => {
                console.error();
            }
        );
    }

    customDate(list: any[], status:any): any[] {
        list.forEach(unitItem => {
            unitItem.ngay = unitItem.date? DateUtil.formatDate(unitItem.date):null;
            unitItem.nhanvien = unitItem.account? unitItem.account.userName:'';
            unitItem.nhanVienId = unitItem.account? unitItem.account.id:'';
            
        });
        return list;
      }

    public assignWork(){
        let indexs = this.myGrid.getselectedrowindexes();
        if(indexs.length === 0){
            this.notificationService.showWarning('Vui lòng chọn công việc',"Cảnh báo!");
            return;
        }
        const listWork = [];
            for(let i = 0; i <this.listEntity.length; i++) {
                if(indexs.includes(i)){
                    listWork.push(this.listEntity[i]);
                }
            }

        const modalRef = this.modalService.open(GiaoViecPopUpComponent, { windowClass: 'modal-view',keyboard: true });
        modalRef.componentInstance.data = listWork;
        modalRef.componentInstance.shopCode = this.shopCode;
        modalRef.result.then(
            () => {
              this.loadData();
             
            },
            () => {}
          );
    }

    public openChangeStatus(){
        let indexs = this.myGrid.getselectedrowindexes();
        if(indexs.length === 0){
            this.notificationService.showWarning('Vui lòng chọn công việc',"Cảnh báo!");
            return;
        }
        const listWork = [];
            for(let i = 0; i <this.listEntity.length; i++) {
                if(indexs.includes(i)){
                    listWork.push(this.listEntity[i]);
                }
            }

        const modalRef = this.modalService.open(ChuyenTrangThaiPopUpComponent, { windowClass: 'modal-view',keyboard: true });
        modalRef.componentInstance.data = listWork;
        modalRef.result.then(
            () => {
              this.loadData();
             
            },
            () => {}
          );
    }

    openAutoAssignWork():void{
        const modalRef = this.modalService.open(TuDongGiaoViecComponent, { windowClass: 'modal-view',keyboard: true });
        modalRef.componentInstance.shopCode = this.shopCode;
        modalRef.result.then(
            () => {
              this.loadData();
            },
            () => {}
          );
    }

    

    public onProcessData():void{
        if(!this.selectedEntity) {
            this.notificationService.showWarning('Vui lòng chọn dữ liệu',"Cảnh báo!");
            return;
        }
        const modalRef = this.modalService.open(XuLyDuLieuPopupComponent, { size: 'xl' });
        modalRef.componentInstance.data = this.selectedEntity;
        modalRef.result.then(
            () => {
              this.loadData();
             
            },
            () => {}
          );
    }
    public onExportData():void{
        const modalRef = this.modalService.open(TongKetDuLieuPopupComponent, { size: 'xl' });
        modalRef.componentInstance.data = this.listEntity;
        modalRef.result.then(
            () => {
              this.loadData();
            },
            () => {}
          );
    }
    public onRowSelect(event:any):void{
        this.selectedEntity = this.listEntity[Number(event.args.rowindex)];
    }


    public exportTOExcel() {  
        const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(this.TABLE.nativeElement);  
        const wb: XLSX.WorkBook = XLSX.utils.book_new();  
        XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');  
        XLSX.writeFile(wb, `data_${this.getCurrentDate()}.xlsx`); 
    } 

    private getCurrentDate() {
        let date = new Date();
        return moment(date).format('DD/MM/YYYY');
    }

    public onRowdblclick(event:any){
        console.log(event);
        const modalRef = this.modalService.open(XuLyDuLieuPopupComponent, { size: 'xl' });
        modalRef.componentInstance.data = this.listEntity[Number(event.args.rowindex)];
        modalRef.result.then(
            () => {
              this.loadData();
             
            },
            () => {}
        );
    }

    refresh():void{
        this.statusDto = '';
        this.loadData();
    }

    formatDate(date:any){
        return DateUtil.formatDate(date);
    }

    nextPage(){
        this.page++;
        this.loadData();
    }
    previousPage(){
        if(this.page > 0) this.page--;
        this.loadData();
    }

    onChangeDate(){
        this.size = 10;
    }
}
