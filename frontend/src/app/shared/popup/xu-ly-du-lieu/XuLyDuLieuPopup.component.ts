import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, Input, AfterViewInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DanhMucService } from 'app/danhmuc.service';
import { NotificationService } from 'app/notification.service';
import moment from 'moment';
import { LocalStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-XuLyDuLieuPopup',
  templateUrl: './XuLyDuLieuPopup.component.html',
  styleUrls: ['./XuLyDuLieuPopup.component.scss']
})
export class XuLyDuLieuPopupComponent implements OnInit,AfterViewInit {
  @Input() data: any = {};
  REQUEST_DATA_URL ="/api/v1/data";
  REQUEST_PRODUCT_URL ="/api/v1/product";
  listProduct = [];
  selectedProductEntity: any;
  info:any;
  link!: SafeResourceUrl;
  constructor(private activeModal: NgbActiveModal,
    private service: DanhMucService,
    private notificationService: NotificationService,
    private dmService: DanhMucService,
    private localStorage: LocalStorageService,
    public sanitizer: DomSanitizer,
  ) 
  {
    this.info = this.localStorage.retrieve("authenticationToken");
  }

  ngOnInit(): void {
    this.link = this.sanitizer.bypassSecurityTrustResourceUrl(
      "http://103.75.187.109:3000?phone=" + this.data.phone
    );
    this.loadDataProduct();

  }

  ngAfterViewInit(): void {

  }

  public loadDataProduct() {
    this.dmService.getOption(null, this.REQUEST_PRODUCT_URL, "?status=1&shopCode="+this.data.shopCode).subscribe(
        (res: HttpResponse<any>) => {
            this.listProduct = res.body.RESULT;
            this.selectedProductEntity = this.data.productDto;
        },
        () => {
            console.error();
        }
    );
  }


  onGiveShip():void{
    window.open("#/bill?id=" + this.data.id)
  }

  // formatNumber(n: any): any {
  //   return n.replace(/\D/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  // }

  // formatCurrency(): any {
  //   const input: any = $('#currency-field');
  //   let inputVal = input.val();
  //   if (inputVal === '') {
  //     return;
  //   }
  //   const originalLen = inputVal.length;
  //   let caretPos = input.prop('selectionStart');
  //   inputVal = this.formatNumber(inputVal);

  //   // send updated string to input
  //   input.val(inputVal);

  //   // put caret back in the right position
  //   const updatedLen = inputVal.length;
  //   caretPos = updatedLen - originalLen + caretPos;
  //   input[0].setSelectionRange(caretPos, caretPos);
  // }

  save(i:any):void{
    if(i===6) this.data.price = 0;
    // hnhy2001
    if(i===7){
      this.data.saleId = this.localStorage.retrieve("authenticationtoken").id;
    }
    // hnhy2001
    this.data.dateChanged = moment(new Date()).format('YYYYMMDDHHmmss');
    // this.data.dateChangedOnly = moment(new Date()).format('YYYYMMDD');
    this.data.status = i === -1 ? this.data.status : i;
    this.data.productDto = this.selectedProductEntity;
    const entity = {
      dataList: [this.data]
    }
    this.service.postOption(entity, this.REQUEST_DATA_URL, "/assignWork").subscribe(
      (res: HttpResponse<any>) => {
        this.activeModal.close();
        this.notificationService.showSuccess(`${res.body.MESSAGE}`,"Thông báo!");
      },
      (error: any) => {
        this.notificationService.showError('Đã có lỗi xảy ra',"Thông báo lỗi!");
      }
    );
  }

  onChangeProduct(event:any){
    if(event){
      this.data.price = event.giaBan;
    }
    else {
      this.data.price = "";
    }
    
  }



  public decline(): void {
    this.activeModal.close(false);
  }

  public accept(): void {
    this.activeModal.close(true);
  }

  public dismiss(): void {
    this.activeModal.dismiss();
  }
}
