import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IEndereco {
  id?: number;
  cep?: string | null;
  logradouro?: string | null;
  complemento?: string | null;
  numero?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  estado?: string | null;
  criacao?: dayjs.Dayjs | null;
  usuario?: IUsuario | null;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public cep?: string | null,
    public logradouro?: string | null,
    public complemento?: string | null,
    public numero?: string | null,
    public bairro?: string | null,
    public cidade?: string | null,
    public estado?: string | null,
    public criacao?: dayjs.Dayjs | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getEnderecoIdentifier(endereco: IEndereco): number | undefined {
  return endereco.id;
}
