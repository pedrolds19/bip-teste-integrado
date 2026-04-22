import { Component, OnInit } from '@angular/core';
import { Beneficio, BeneficioService } from './services/beneficio';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
  standalone: false
})
export class App implements OnInit {
  beneficios: Beneficio[] = [];
  telaAtiva: 'inicio' | 'lista' | 'transferir' | 'admin' = 'inicio';


  mudarTela(tela: 'inicio' | 'lista' | 'transferir' | 'admin') {
    this.telaAtiva = tela;
    if (tela === 'lista') {
      this.carregarBeneficios();
    }
  }

  idOrigem: number | null = null;
  idDestino: number | null = null;
  valor: number | null = null;

  constructor(private service: BeneficioService) {
  }

  ngOnInit() {
    this.carregarBeneficios();
  }

  carregarBeneficios() {
    this.service.listarTodos().subscribe({
      next: (dados) => {
        this.beneficios = [...dados];
      },
      error: (err) => alert('Erro ao carregar lista: ' + err.message)
    });
  }

  transferir() {
    if (this.idOrigem && this.idDestino && this.valor) {

      if (this.valor <= 0) {
        alert('OPERAÇÃO CANCELADA:\n\nDigite um valor válido, maior que zero.');
        return; 
      }

      if (this.idOrigem === this.idDestino) {
        alert('OPERAÇÃO CANCELADA:\n\nA conta de origem e destino não podem ser a mesma.');
        return;
      }

      this.service.transferir(this.idOrigem, this.idDestino, this.valor).subscribe({
        next: () => {
          alert('Transferência realizada com sucesso!');
          this.carregarBeneficios();
          this.limparFormulario();
        },
        error: (err) => {
          console.error('Log técnico do Erro:', err);

          let mensagem = 'Erro de comunicação com o servidor.';

          if (err.error) {
            if (err.error.erro) {
              mensagem = err.error.erro;
            }
            else if (typeof err.error === 'string') {
              try {
                mensagem = JSON.parse(err.error).erro;
              } catch (e) {
                mensagem = err.error;
              }
            }
          }

          alert('OPERAÇÃO CANCELADA:\n\n' + mensagem);
        }
      });
    }
  }


  limparFormulario() {
    this.idOrigem = null;
    this.idDestino = null;
    this.valor = null;
  }

  novoNome: string = '';
  novaDescricao: string = '';
  valorInicial: number | null = null;

  salvarNovo() {
    if (this.novoNome && this.valorInicial !== null) {
      const novo = {
        nome: this.novoNome,
        descricao: this.novaDescricao,
        valor: this.valorInicial
      };

      this.service.salvar(novo).subscribe({
        next: () => {
          alert('Benefício cadastrado com sucesso!');
          this.limparCamposCadastro();
          this.mudarTela('lista');
        },
        error: (err) => alert('Erro ao cadastrar: ' + err.message)
      });
    }
  }

  remover(id: number) {
    if (confirm('Deseja realmente remover este item?')) {
      this.service.excluir(id).subscribe({
        next: () => {
          this.carregarBeneficios();
        },
        error: (err) => alert('Erro ao remover: ' + err.message)
      });
    }
  }


  limparCamposCadastro() {
    this.novoNome = '';
    this.novaDescricao = '';
    this.valorInicial = null;
  }

  idBusca: number | null = null;
  beneficioBuscado: Beneficio | null = null;
  mensagemBusca: string = '';

  fazerBusca() {
    if (!this.idBusca) {
      this.mensagemBusca = 'DIGITE UM ID PARA PESQUISAR.';
      this.beneficioBuscado = null;
      return;
    }

    this.service.buscarPorId(this.idBusca).subscribe({
      next: (resultado) => {
        if (resultado) {
          this.beneficioBuscado = resultado;
          this.mensagemBusca = '';
          console.log('Ativo localizado com sucesso.');
        } else {
          this.beneficioBuscado = null;
          this.mensagemBusca = `ID #${this.idBusca} NÃO ENCONTRADO.`;
        }
      },
      error: (err) => {
        this.beneficioBuscado = null;
        if (err.status === 404) {
          this.mensagemBusca = `ID #${this.idBusca} NÃO CONSTA NA BASE DE DADOS.`;
        } else {
          this.mensagemBusca = 'ERRO NA COMUNICAÇÃO COM O SERVIDOR.';
        }
        alert("Um erro foi encontrado");
      }
    });
  }



  limparBusca() {
    this.idBusca = null;
    this.beneficioBuscado = null;
    this.mensagemBusca = '';
  }
}
