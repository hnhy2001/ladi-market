import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrModule } from "ngx-toastr";

import { SidebarModule } from './sidebar/sidebar.module';
import { FooterModule } from './shared/footer/footer.module';
import { NavbarModule} from './shared/navbar/navbar.module';
import { FixedPluginModule} from './shared/fixedplugin/fixedplugin.module';

import { AppComponent } from './app.component';
import { AppRoutes } from './app.routing';
import { FormsModule } from "@angular/forms";

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { QRCodeModule } from "angularx-qrcode";
import { XuLyDuLieuPopupComponent } from "./shared/popup/xu-ly-du-lieu/XuLyDuLieuPopup.component";
import { TongKetDuLieuPopupComponent } from "./shared/popup/tong-ket-du-lieu/TongKetDuLieuPopup.component";
import { jqxDateTimeInputModule } from 'jqwidgets-ng/jqxdatetimeinput';
import { LogiinComponent } from './pages/logiin/logiin.component';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { HeadersInterceptor } from "./headers.interceptor";
import { ThemSuaXoaAccountComponent } from './shared/popup/them-sua-xoa-account/them-sua-xoa-account.component';

import { GiaoViecPopUpComponent } from './shared/popup/giao-viec-pop-up/giao-viec-pop-up.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { TuDongGiaoViecComponent } from "./shared/popup/tu-dong-giao-viec/tu-dong-giao-viec.component";
import { jqxGridModule } from "jqwidgets-ng/jqxgrid";
import { CheckOutComponent } from "./shared/popup/checkout/checkout.component";
import { ChuyenTrangThaiPopUpComponent } from "./shared/popup/chuyen-trang-thai-pop-up/chuyen-trang-thai-pop-up.component";
import { ThemSuaShopComponent } from "./shared/popup/them-sua-shop/them-sua-shop.component";
import { GanShopComponent } from "./shared/popup/gan-shop/gan-shop.component";
import { ThongTinCauHinhComponent } from "./shared/popup/thong-tin-cau-hinh/thong-tin-cau-hinh.component";
import { ThemSuaProductComponent } from "./shared/popup/them-sua-product/them-sua-product.component";
import { ThemSuaUtmComponent } from "./shared/popup/them-sua-utm/them-sua-utm.component";
import { UtmStatisticComponent } from "./pages/utm-statistic/utm-statistic.component";
import { jqxPivotGridModule } from "jqwidgets-ng/jqxpivotgrid";
import { ThemSuaXoaCostTypeComponent } from "./shared/popup/them-sua-xoa-cost-type/them-sua-xoa-cost-type.component";
import { ThemSuaXoaCostComponent } from "./shared/popup/them-sua-xoa-cost/them-sua-xoa-cost.component";
import { NgxDaterangepickerMd } from "ngx-daterangepicker-material";
import { ThemSuaXoaConfigComponent } from "./shared/popup/them-sua-xoa-config/them-sua-xoa-config.component";
import { ThemSuaXoaCostMarketingComponent } from "./shared/popup/them-sua-xoa-cost-mkt/them-sua-xoa-cost-mkt.component";
import { ThemSuaXoaWarehouseComponent } from "./shared/popup/them-sua-xoa-warehouse/them-sua-xoa-warehouse.component";
import { NgxSpinnerModule } from 'ngx-spinner';


@NgModule({
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    XuLyDuLieuPopupComponent,
    TongKetDuLieuPopupComponent,
    LogiinComponent,
    ThemSuaXoaAccountComponent,
    GiaoViecPopUpComponent,
    ChuyenTrangThaiPopUpComponent,
    TuDongGiaoViecComponent,
    CheckOutComponent,
    ThemSuaShopComponent,
    ThemSuaProductComponent,
    GanShopComponent,
    ThemSuaUtmComponent,
    ThongTinCauHinhComponent,
    ThemSuaXoaCostTypeComponent,
    ThemSuaXoaCostComponent,
    ThemSuaXoaConfigComponent,
    ThemSuaXoaCostMarketingComponent,
    ThemSuaXoaWarehouseComponent
  ],
  imports: [
    BrowserAnimationsModule,
    RouterModule.forRoot(AppRoutes,{
      useHash: true
    }),
    SidebarModule,
    NavbarModule,
    ToastrModule.forRoot(),
    FooterModule,
    FixedPluginModule,
    NgbModule,
    HttpClientModule,
    QRCodeModule,
    jqxDateTimeInputModule,
    FormsModule,
    NgxWebstorageModule.forRoot({ prefix: '', separator: '' }),
    NgSelectModule,
    jqxGridModule,
    jqxPivotGridModule,
    NgxDaterangepickerMd,
    NgxSpinnerModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HeadersInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
  exports: [XuLyDuLieuPopupComponent,TongKetDuLieuPopupComponent]
})
export class AppModule { }
