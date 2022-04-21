import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Pagamento } from 'app/entities/enumerations/pagamento.model';
import { EstadoTransacao } from 'app/entities/enumerations/estado-transacao.model';
import { ICompra, Compra } from '../compra.model';

import { CompraService } from './compra.service';

describe('Compra Service', () => {
  let service: CompraService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompra;
  let expectedResult: ICompra | ICompra[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompraService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      percentualDesconto: 0,
      valorFinal: 0,
      dataCriacao: currentDate,
      formaPagamento: Pagamento.BOLETO,
      estado: EstadoTransacao.CRIADO,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Compra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.create(new Compra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Compra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          percentualDesconto: 1,
          valorFinal: 1,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
          formaPagamento: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Compra', () => {
      const patchObject = Object.assign(
        {
          percentualDesconto: 1,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
          formaPagamento: 'BBBBBB',
        },
        new Compra()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Compra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          percentualDesconto: 1,
          valorFinal: 1,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
          formaPagamento: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Compra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompraToCollectionIfMissing', () => {
      it('should add a Compra to an empty array', () => {
        const compra: ICompra = { id: 123 };
        expectedResult = service.addCompraToCollectionIfMissing([], compra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compra);
      });

      it('should not add a Compra to an array that contains it', () => {
        const compra: ICompra = { id: 123 };
        const compraCollection: ICompra[] = [
          {
            ...compra,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, compra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Compra to an array that doesn't contain it", () => {
        const compra: ICompra = { id: 123 };
        const compraCollection: ICompra[] = [{ id: 456 }];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, compra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compra);
      });

      it('should add only unique Compra to an array', () => {
        const compraArray: ICompra[] = [{ id: 123 }, { id: 456 }, { id: 23126 }];
        const compraCollection: ICompra[] = [{ id: 123 }];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, ...compraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compra: ICompra = { id: 123 };
        const compra2: ICompra = { id: 456 };
        expectedResult = service.addCompraToCollectionIfMissing([], compra, compra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compra);
        expect(expectedResult).toContain(compra2);
      });

      it('should accept null and undefined values', () => {
        const compra: ICompra = { id: 123 };
        expectedResult = service.addCompraToCollectionIfMissing([], null, compra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compra);
      });

      it('should return initial array if no Compra is added', () => {
        const compraCollection: ICompra[] = [{ id: 123 }];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, undefined, null);
        expect(expectedResult).toEqual(compraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
