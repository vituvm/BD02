import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface ICurso {
  id?: number;
  titulo?: string;
  duracaoCH?: number | null;
  descricao?: string | null;
  valor?: number | null;
  criacao?: dayjs.Dayjs | null;
  professor?: IUsuario | null;
  aluno?: IUsuario | null;
}

export class Curso implements ICurso {
  constructor(
    public id?: number,
    public titulo?: string,
    public duracaoCH?: number | null,
    public descricao?: string | null,
    public valor?: number | null,
    public criacao?: dayjs.Dayjs | null,
    public professor?: IUsuario | null,
    public aluno?: IUsuario | null
  ) {}
}

export function getCursoIdentifier(curso: ICurso): number | undefined {
  return curso.id;
}
