import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEndereco, getEnderecoIdentifier } from '../endereco.model';

export type EntityResponseType = HttpResponse<IEndereco>;
export type EntityArrayResponseType = HttpResponse<IEndereco[]>;

@Injectable({ providedIn: 'root' })
export class EnderecoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enderecos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(endereco: IEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .post<IEndereco>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(endereco: IEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .put<IEndereco>(`${this.resourceUrl}/${getEnderecoIdentifier(endereco) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(endereco: IEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .patch<IEndereco>(`${this.resourceUrl}/${getEnderecoIdentifier(endereco) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEndereco>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEndereco[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEnderecoToCollectionIfMissing(enderecoCollection: IEndereco[], ...enderecosToCheck: (IEndereco | null | undefined)[]): IEndereco[] {
    const enderecos: IEndereco[] = enderecosToCheck.filter(isPresent);
    if (enderecos.length > 0) {
      const enderecoCollectionIdentifiers = enderecoCollection.map(enderecoItem => getEnderecoIdentifier(enderecoItem)!);
      const enderecosToAdd = enderecos.filter(enderecoItem => {
        const enderecoIdentifier = getEnderecoIdentifier(enderecoItem);
        if (enderecoIdentifier == null || enderecoCollectionIdentifiers.includes(enderecoIdentifier)) {
          return false;
        }
        enderecoCollectionIdentifiers.push(enderecoIdentifier);
        return true;
      });
      return [...enderecosToAdd, ...enderecoCollection];
    }
    return enderecoCollection;
  }

  protected convertDateFromClient(endereco: IEndereco): IEndereco {
    return Object.assign({}, endereco, {
      criacao: endereco.criacao?.isValid() ? endereco.criacao.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criacao = res.body.criacao ? dayjs(res.body.criacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((endereco: IEndereco) => {
        endereco.criacao = endereco.criacao ? dayjs(endereco.criacao) : undefined;
      });
    }
    return res;
  }
}
