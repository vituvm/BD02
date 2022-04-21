import dayjs from 'dayjs/esm';
import { TipoUsuario } from 'app/entities/enumerations/tipo-usuario.model';

export interface IUsuario {
  id?: number;
  nome?: string;
  cpf?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  tipo?: TipoUsuario | null;
  criacao?: dayjs.Dayjs | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public nome?: string,
    public cpf?: string | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public tipo?: TipoUsuario | null,
    public criacao?: dayjs.Dayjs | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
