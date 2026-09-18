# Projeto Motiva — Sprint 3 (Persistência com Oracle/JDBC)
Camada de persistência do projeto Motiva (monitoramento e manutenção de
vegetação em rodovias), ligando as classes de domínio (`TrechoRodovia`,
`MotorPrioridade`, `IntervencaoOperacional` e subclasses, `EquipeManutencao`)
a um banco Oracle via JDBC, seguindo o padrão DAO.

## Estrutura do projeto

```
SPRINT-3-Persistencia-de-Dados/
├── sql/
│   ├── script-criacao.sql     # criação de 4 tabelas
│   └── script-dados.sql       # dados de teste
├── src/
| └── br/
|        └── com/
|            └── motiva/
│                  ├── model/                 # entidades de domínio (regras de negócio)
│                  ├── db/                    # ConexaoBD (JDBC)
│                  ├── dao/                   # DAOs (CRUD + records de persistência)
│                  ├── service/               # GeradorRelatorio 
│                  └── main/                  # Main.java 
└── README.md
```

## Como as classes se conectam

- `TrechoRodovia` implementa `MonitoravelViaIoT` e guarda `nivelVegetacao`.
- `MotorPrioridade` lê o `nivelVegetacao` e classifica em `Prioridade`
  (CRITICO ≥ 40, ATENCAO ≥ 20, ALERTA ≥ 10, NORMAL < 10) e cria a
  `IntervencaoOperacional` adequada (`RocadaMecanizada`, `RocadaManual` ou
  `Pulverizacao`).
- `EquipeManutencao.executarIntervencao()` chama
  `IntervencaoOperacional.iniciarIntervencao()`, que por polimorfismo
  executa o `executarServico()` da subclasse correta.
- `GeradorRelatorio` usa o `MotorPrioridade` para contar quantos trechos
  estão em cada faixa, imprime no console e grava o resumo via
  `RelatorioPrioridadeDAO`.
- Cada DAO usa um `record` próprio para
  representar a linha do banco, separado da classe de domínio, assim o
  DAO fica isolado das regras de negócio da classe `model`.

