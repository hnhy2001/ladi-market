import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OPERATIONS } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DanhMucService {
  // public resourceUrl = 'https://dhftech.store';

  public resourceUrl = 'http://103.75.185.204:8080';

  // public resourceUrl = 'http://103.57.220.113:8080';

  // public resourceUrl = 'http://crm.adsxanh-market.com:8081';

  // public resourceUrl = 'http://localhost:8080';

  constructor(protected http: HttpClient, ) {
  }

  create(entity: any, requestUrl: any): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.resourceUrl + requestUrl + OPERATIONS.CREATE, entity, { observe: 'response' });
  }

  update(entity: any, requestUrl: any): Observable<HttpResponse<any>> {
    return this.http.put<any>(this.resourceUrl + requestUrl + OPERATIONS.UPDATE + '?id=' + entity.id, entity, {
      observe: 'response'
    });
  }

  find(id: number, requestUrl: any): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.resourceUrl + requestUrl + OPERATIONS.DETAILS}/${id}`, { observe: 'response' });
  }

  getById(id: number, requestUrl: any): Observable<HttpResponse<any>> {
    return this.http.get<any>(this.resourceUrl + requestUrl + OPERATIONS.DETAILS + '?id=' + id, { observe: 'response' });
  }

  query(req: any, requestUrl: any): Observable<HttpResponse<any>> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceUrl + requestUrl + OPERATIONS.SEARCH, { params: options, observe: 'response' });
  }

  delete(id: number, requestUrl: any): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl + requestUrl +  '?id='}${id}`, { observe: 'response' });
  }

  postOption(entity: any, requestUrl: any, option: any): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.resourceUrl + requestUrl + option, entity, { observe: 'response' });
  }

  post(requestUrl: any, option: any): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.resourceUrl + requestUrl + option,{ observe: 'response' });
  }

  getOption(req: any, requestUrl: any, option: any): Observable<HttpResponse<any>> {

    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceUrl + requestUrl + option, { params: options, observe: 'response' });
  }

  putOption(entity: any, requestUrl: any, option: any): Observable<HttpResponse<any>> {
    return this.http.put<any>(this.resourceUrl + requestUrl + option, entity, { observe: 'response' });
  }

}
