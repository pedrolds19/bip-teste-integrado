### Desafio Fullstack Integrado - BIP
Este repositório contém a minha solução completa para o Desafio Fullstack Integrado. A aplicação é um sistema de gestão de benefícios desenvolvido em camadas, com foco na correção de falhas transacionais e na experiência do usuário.

### Objetivo
Corrigir falhas críticas de negócio no módulo EJB, implementar um backend robusto em Java e um frontend moderno em Angular, garantindo a integridade dos dados em operações de transferência.

### Tecnologias Utilizadas
## Backend:

Java 8+ (Java EE / Jakarta EE): Lógica de negócio e serviços.

EJB (Enterprise JavaBeans): Gestão de transações e concorrência.

JAX-RS (REST): Exposição dos endpoints da API.

Hibernate / JPA: Persistência e mapeamento de dados.

WildFly: Servidor de aplicação utilizado.

JUnit 5: Testes unitários para validação de regras de negócio.

## Frontend:

Angular 17+: Framework para a interface SPA.

TypeScript: Desenvolvimento seguro e tipado.

CSS Moderno: Design minimalista e responsivo.

### Correção do Bug no EJB

## O desafio apresentava um BeneficioEjbService com falhas graves de consistência. As seguintes melhorias foram implementadas:

Pessimistic Locking: Adicionado LockModeType.PESSIMISTIC_WRITE nas consultas de transferência para evitar "Race Conditions" (condições de corrida), garantindo que duas
transferências simultâneas não corrompam o saldo.

Validação de Saldo: Implementada lógica que impede que o saldo de origem fique negativo, lançando exceções adequadas (IllegalStateException).

Barreira contra Valores Inválidos: O sistema agora bloqueia transferências de valores nulos, zerados ou negativos.

Tratamento de Exceções: Implementado um "desempacotador" de erros no Backend para que o frontend receba mensagens claras (ex: "Saldo insuficiente") em vez de logs técnicos do servidor.

## Como Executar o Projeto

## 1. Backend Java 8+ e Maven.

Execute o comando para gerar o artefato:
   ```
   mvn clean package
   ```
Realize o deploy do .war gerado no seu servidor WildFly.

## 2. Frontend (Angular)
Navegue até a pasta frontend.
Instale as dependências:
```
mvn clean package
```
Inicie a aplicação:
   ```
ng serve
```

### Testes Automatizados
O projeto conta com testes unitários focados no serviço de transferência, garantindo que as validações de saldo e valores negativos nunca deixem de funcionar. Para rodar:
```
mvn test
```
