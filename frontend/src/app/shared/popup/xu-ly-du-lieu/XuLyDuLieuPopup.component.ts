import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, Input, AfterViewInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DanhMucService } from 'app/danhmuc.service';
import { NotificationService } from 'app/notification.service';
import moment from 'moment';

@Component({
  selector: 'jhi-XuLyDuLieuPopup',
  templateUrl: './XuLyDuLieuPopup.component.html',
  styleUrls: ['./XuLyDuLieuPopup.component.scss']
})
export class XuLyDuLieuPopupComponent implements OnInit,AfterViewInit {
  @Input() data: any = {};
  REQUEST_DATA_URL ="/api/v1/data";
  constructor(private activeModal: NgbActiveModal,private service: DanhMucService,private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.formatCurrency();
    }, 100);
  }


  onGiveShip():void{
    window.open("#/bill?id=" + this.data.id)
  }

  formatNumber(n: any): any {
    return n.replace(/\D/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  formatCurrency(): any {
    const input: any = $('#currency-field');
    let inputVal = input.val();
    if (inputVal === '') {
      return;
    }
    const originalLen = inputVal.length;
    let caretPos = input.prop('selectionStart');
    inputVal = this.formatNumber(inputVal);

    // send updated string to input
    input.val(inputVal);

    // put caret back in the right position
    const updatedLen = inputVal.length;
    caretPos = updatedLen - originalLen + caretPos;
    input[0].setSelectionRange(caretPos, caretPos);
  }

  save(i:any):void{
    this.data.dateChanged = moment(new Date()).format('YYYYMMDDHHmmss');
    this.data.price = this.data.price?this.data.price.replace(/,/g, '').replace(/\D/g, ''):null;
    this.data.status = i === -1 ? this.data.status : i;
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
