import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Beneficio {
  id: number;
  nome: string;
  descricao: string;
  valor: number;
}

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private readonly API = 'http://localhost:8080/backend-module/api/v1/beneficios';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.API);
  }

  // Se a API for /transferir e usar QueryParams no Java:
  transferir(origem: number, destino: number, valor: number): Observable<any> {
    const url = `${this.API}/transferir?origem=${origem}&destino=${destino}&valor=${valor}`;
    return this.http.post(url, {}); // Corpo vazio
  }

  // Método para o CRUD (C do Create)
  salvar(beneficio: any): Observable<any> {
    return this.http.post(this.API, beneficio, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  // Método para o CRUD (D do Delete)
  excluir(id: number): Observable<any> {
    return this.http.delete(`${this.API}/${id}`);
  }

  // Adicione este método na classe BeneficioService
  buscarPorId(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.API}/${id}`);
  }
}
