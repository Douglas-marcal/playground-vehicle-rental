# Requisitos da v1 — Locadora de Veículos (fictícia)

## Stack e decisões

- Kotlin, Spring Boot, Gradle, Java 25
- Clean Architecture, organizada por módulo (`vehicle`, `customer`, ...), cada um com as camadas
  `domain` (Kotlin puro), `application` (casos de uso) e `infrastructure` (web e persistence)
- MySQL 8.4, JPA/Hibernate para mapeamento, **Flyway** para o schema (`ddl-auto=validate`)
- Entidades JPA vivem só na `infrastructure`; o domínio não depende de Spring nem de JPA
- API REST apenas, sem front-end
- Testes: JUnit 5; Testcontainers (MySQL) para integração
- Docker: `docker compose up --build` sobe o MySQL e a aplicação
- **Autenticação fica fora da v1.** Os perfis (admin, atendente) existem apenas como dado no usuário

## Módulos

| # | Módulo | Escopo | Status |
|---|---|---|---|
| 1 | Veículos | Cadastro com categoria, placa (antiga e Mercosul), status (disponível, alugado, manutenção) e quilometragem | Feito (cadastro, consulta, listagem, troca de status) |
| 2 | Clientes | Cadastro com CNH (número, categoria, validade) e documentos (CPF) | Feito (cadastro, consulta, listagem, renovação da CNH) |
| 3 | Filiais | Cadastro de filiais, usadas na retirada e na devolução | A fazer |
| 4 | Reserva e locação | Reserva, retirada, devolução, período e filial | A fazer |
| 5 | Disponibilidade e frota | Conflito de datas, manutenção, bloqueio de veículo indisponível | A fazer |
| 6 | Cálculo de valor | Diária por categoria, multas (atraso, danos), taxas e caução | A fazer |
| 7 | Pagamentos | Registro de pagamentos, caução e estorno | A fazer |
| 8 | Usuários e perfis | Usuário com papel (admin, atendente), sem login | A fazer |
| 9 | Relatórios | Frota, locações e faturamento por período | A fazer |

## Regras de clientes (adotadas como padrão, ajustáveis)

- CPF válido (dígitos verificadores) e único
- CNH com 11 dígitos, única; vale até o fim do dia de validade
- Idade mínima de 21 anos na data do cadastro
- CNH vencida não permite o cadastro nem a renovação com outra CNH vencida
- Categoria A (só moto) não habilita a alugar carros
- A CNH vencida depois do cadastro bloqueia a locação: `Customer.canDriveOn(data)` é a regra que o módulo de locação vai usar

## Regras de negócio a confirmar

Estas regras ainda não foram decididas e afetam o desenho dos módulos 4 a 7:

- Política de condutor adicional
- Como se calcula a diária: por dia corrido ou por períodos de 24 h
- Valor da multa por atraso (fixo, percentual da diária ou por hora)
- Caução: valor fixo por categoria ou percentual; quando é devolvida
- Pagamento: formas aceitas (cartão, Pix, dinheiro) e se é na retirada ou na devolução
- Cancelamento de reserva: prazo e taxa
- Devolução em filial diferente da retirada: há taxa?

## Convenções

- Migrações Flyway em `src/main/resources/db/migration`, no padrão `V<n>__descricao.sql`
- Uma migração já aplicada nunca é editada; mudanças viram uma nova versão
- Valores monetários em `BigDecimal` (`DECIMAL` no banco)
- Erros da API no formato `ProblemDetail` (RFC 9457)
