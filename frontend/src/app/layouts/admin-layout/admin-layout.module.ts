import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdminLayoutRoutes } from './admin-layout.routing';

import { DashboardComponent }       from '../../pages/dashboard/dashboard.component';
import { TableComponent }           from '../../pages/table/table.component';
import { DataComponent }           from '../../pages/data/data.component';
import { TypographyComponent }      from '../../pages/typography/typography.component';
import { AccountComponent }           from '../../pages/account/account.component';
import { WorkComponent }            from '../../pages/work/work.component';
import { NotificationsComponent }   from '../../pages/notifications/notifications.component';
import { UpgradeComponent }         from '../../pages/upgrade/upgrade.component';
import { jqxGridModule } from 'jqwidgets-ng/jqxgrid';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { jqxDateTimeInputModule } from 'jqwidgets-ng/jqxdatetimeinput';
import { NgxDaterangepickerMd } from "ngx-daterangepicker-material";
import { ShopComponent } from 'app/pages/shop/shop.component';
import { UtmMediumComponent } from 'app/pages/utm-medium/utm-medium.component';
import { ProductComponent } from 'app/pages/product/product.component';
import { UtmStatisticComponent } from 'app/pages/utm-statistic/utm-statistic.component';
import { jqxPivotGridModule } from 'jqwidgets-ng/jqxpivotgrid';
import { CostTypeComponent } from 'app/pages/cost-type/cost-type.component';
import { CostComponent } from 'app/pages/cost/cost.component';
import { StatiscalCostComponent } from 'app/pages/statiscal-cost/statiscal-cost.component';
import { StatiscalRevenueComponent } from 'app/pages/statiscal-revenue/statiscal-revenue.component';
import { ConfigComponent } from 'app/pages/config/config.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    NgbModule,
    jqxGridModule,
    jqxPivotGridModule,
    jqxDateTimeInputModule,
    NgxDaterangepickerMd.forRoot(),
  ],
  declarations: [
    DashboardComponent,
    TableComponent,
    UpgradeComponent,
    TypographyComponent,
    AccountComponent,
    WorkComponent,
    NotificationsComponent,
    DataComponent,
    ShopComponent,
    UtmMediumComponent,
    ProductComponent,
    UtmStatisticComponent,
    CostTypeComponent,
    CostComponent,
    StatiscalCostComponent,
    StatiscalRevenueComponent,
    ConfigComponent
  ]
})

export class AdminLayoutModule {}
