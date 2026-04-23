# Projeto Jogo em Java

Jogo de batalha em turno feito em Java para terminal/console. O projeto possui dois modos de jogo:

- `Batalhas`: arena livre em que o jogador escolhe a ordem dos bosses.
- `Campanha`: progressao guiada com historia, capitulos, bosses em sequencia, ganho de experiencia, desbloqueio de armas e recompensas.

O foco do projeto e exercitar orientacao a objetos, separacao de responsabilidades, heranca, composicao e fluxo de jogo baseado em turnos.

## Sumario

- [Visao geral](#visao-geral)
- [Funcionalidades](#funcionalidades)
- [Como executar](#como-executar)
- [Como rodar os testes](#como-rodar-os-testes)
- [Fluxo geral do jogo](#fluxo-geral-do-jogo)
- [Arquitetura e classes](#arquitetura-e-classes)
- [Mecanicas principais](#mecanicas-principais)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Observacoes](#observacoes)

## Visao geral

Ao iniciar o programa, o jogador escolhe um modo de jogo, cria seu personagem e seleciona armas iniciais. A partir disso:

- no modo `Batalhas`, entra em uma arena onde pode enfrentar os bosses disponiveis na ordem desejada;
- no modo `Campanha`, segue uma jornada em capitulos, com introducao narrativa, batalha contra um boss, recompensa, evolucao e preparacao para o proximo confronto.

Durante as batalhas, o jogador pode:

- atacar;
- defender;
- usar itens de cura;
- usar habilidade especial;
- evoluir atributos e talentos entre batalhas;
- trocar a arma principal para alterar bonus ofensivos e defensivos.

## Funcionalidades

- Menu inicial com escolha entre `Batalhas` e `Campanha`
- Criacao de jogador e selecao de armas iniciais
- Batalhas em turno contra bosses com comportamentos distintos
- Sistema de armas de curta e longa distancia
- Armas com e sem municao
- Sistema de defesa com bloqueio, parry e reducao de dano
- Consumiveis de cura e aprimoramento
- Progressao por experiencia e nivel
- Distribuicao de atributos
- Sistema de talentos por ramo
- Desbloqueio gradual de armas conforme o nivel sobe
- Historico de batalhas ao final da partida
- Suite automatizada de testes

## Como executar

### Requisitos

- Java JDK instalado
- `javac` disponivel no `PATH`
- `java` disponivel no `PATH`
- PowerShell para usar os scripts e comandos abaixo

### Compilar e executar o jogo

No diretorio raiz do projeto:

```powershell
$classesDir = "out\classes"
New-Item -ItemType Directory -Force $classesDir | Out-Null
$sourceFiles = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d $classesDir $sourceFiles
java -cp $classesDir app.Main
```

## Como rodar os testes

```powershell
powershell -ExecutionPolicy Bypass -File .\run-tests.ps1
```

O script compila `src` e `test` e executa a classe `tests.TestRunner`.

## Fluxo geral do jogo

1. `app.Main` inicia a aplicacao.
2. `controle.ControladorBatalha` exibe o menu inicial e le a escolha do modo.
3. O jogador e configurado com nome e armas iniciais.
4. O modo escolhido assume o controle:
   - `batalha.ArenaBatalhas` para arena livre;
   - `campanha.Campanha` para a jornada narrativa.
5. Cada confronto e representado por `batalha.Batalha`.
6. O loop da batalha alterna turno do jogador e turno do inimigo.
7. O jogador usa `combate.CombateJogador`, `inventario.Inventario` e `progressao.ProgressaoJogador`.
8. Ao vencer, recebe experiencia, sobe de nivel, desbloqueia armas e escolhe atributos/talentos.
9. Ao final, o sistema mostra o historico das batalhas realizadas.

## Arquitetura e classes

### Pacote `app`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Main` | Ponto de entrada da aplicacao | Cria o `ControladorBatalha`, pergunta o modo de jogo e delega o fluxo para `ArenaBatalhas` ou `Campanha`. |

### Pacote `controle`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `ControladorBatalha` | Camada de entrada e controle de fluxo | Le entradas do teclado, monta o jogador, exibe menus, executa o turno do jogador, controla a distribuicao de atributos/talentos e a configuracao da arma principal. |

### Pacote `batalha`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Batalha` | Loop principal de um combate | Coordena rodadas, chama os turnos do jogador e do inimigo, aplica recompensas e define o vencedor. |
| `ArenaBatalhas` | Modo arena | Permite escolher bosses em ordem livre, manter progressao na run e encerrar quando o jogador quiser. |
| `HistoricoBatalhas` | Registro de confrontos | Armazena as batalhas concluidas e imprime um resumo no encerramento. |

### Pacote `campanha`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Campanha` | Modo narrativo principal | Organiza capitulos, apresenta historia, instancia bosses em sequencia, concede recompensas e conduz o encerramento da jornada. |

### Pacote `combate`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `CombateJogador` | Regras de combate do jogador | Resolve ataques, postura defensiva, bloqueio, parry, recarga da habilidade especial, bonus da arma principal, cura, escudo temporario e preparo para nova batalha. |
| `LogCombate` | Saida formatada do jogo | Centraliza a impressao de secoes, subtitulos, falas e narracoes no console. |

### Pacote `personagens`

#### Classes base

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Criatura` | Base para jogador e inimigos | Guarda nome, vida, efeitos negativos e metodos comuns como dano, cura, narracao e processamento de status. |
| `Jogador` | Personagem controlado pelo usuario | Compoe `Inventario`, `ProgressaoJogador` e `CombateJogador`, delegando a essas classes os sistemas especializados. |
| `Inimigo` | Base abstrata para bosses | Estende `Criatura`, possui ataque base e uma `Defesa`, e define o contrato para titulo, perfil e experiencia concedida. |

#### Bosses

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Malignus` | Primeiro boss | Berserker focado em sangramento, investidas brutais e aumento de pressao na fase 2. |
| `Demonium` | Segundo boss | Boss infernal que queima, se cura e usa ataques de explosao em ciclos. |
| `Necrolis` | Terceiro boss | Controlador sombrio com debuffs de dano, drenagem e janelas de esquiva. |
| `Vorgrath` | Quarto boss | Tanque pesado com ataques tectonicos, estilhacos e sustain em fase avancada. |
| `Aetherion` | Boss final | Inimigo astral com cargas, debuffs, dano cosmico e explosoes de alto impacto. |

### Pacote `armas.base`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Arma` | Base de todas as armas | Define dano, chance de acerto, chance de critico, multiplicador critico, descricao, golpe, critico garantido e bonus defensivos customizaveis. |
| `ArmaComMunicao` | Especializacao de `Arma` | Acrescenta controle de municao atual/maxima e altera o golpe para falhar quando a municao acaba. |
| `TipoArma` | Enum de categorias | Separa armas em `CURTA_DISTANCIA` e `LONGA_DISTANCIA`. |

### Pacote `armas.curta`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Faca` | Arma curta inicial | Simples, precisa e com perfil equilibrado para inicio. |
| `Espada` | Arma curta equilibrada | Combate direto, dano mais alto que a faca e perfil estavel. |
| `EscudoGuardiao` | Arma defensiva | Concede bonus relevantes para bloqueio, parry e reducao defensiva. |
| `AdagaSombria` | Arma curta critica | Focada em alta chance de critico. |
| `LaminasGemeas` | Arma multi-hit | Executa dois golpes na mesma acao. |
| `MachadoBerserker` | Arma de dano bruto | Prioriza dano alto e bom potencial critico. |

### Pacote `armas.longa`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `ArcoEFlecha` | Arma longa com municao | Possui alcance alto e boa chance de critico. |
| `Pistola` | Arma longa com municao | Tem alto potencial critico e dano forte por disparo. |
| `LancaPerfurante` | Arma longa especial | Pode ignorar a defesa do alvo com chance percentual. |

### Pacote `inventario`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Inventario` | Armazena armas e consumiveis | Mantem a lista de armas, controla a arma principal, filtra itens por tipo, adiciona armas/consumiveis e descreve o arsenal do jogador. |

### Pacote `itens`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Consumivel` | Base abstrata dos itens | Define nome, descricao, tipo e o metodo `usar`. |
| `Fruta` | Item de cura | Recupera vida e gera escudo temporario. |
| `PocaoCura` | Cura mais forte | Recupera mais vida e concede escudo temporario maior. |
| `PergaminhoForca` | Buff ofensivo | Aumenta o dano do jogador. |
| `PergaminhoCritico` | Buff critico | Aumenta a chance critica e o multiplicador critico. |
| `TipoConsumivel` | Enum de tipos | Separa consumiveis em `CURA` e `APRIMORAMENTO`. |

### Pacote `defesas`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `Defesa` | Defesa padrao | Nao altera o dano recebido e serve como base para os outros tipos. |
| `Armadura` | Defesa percentual | Reduz dano por porcentagem fixa. |
| `BarreiraMagica` | Defesa por teto | Limita o dano maximo que cada golpe pode causar. |
| `DefesaHibrida` | Defesa mista | Pode esquivar completamente ou aplicar reducao fixa de dano. |

### Pacote `progressao`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `ProgressaoJogador` | Sistema de nivel e evolucao | Controla experiencia, nivel, bonus, pontos de atributo, pontos de talento, desbloqueio de armas e efeitos permanentes de talentos. |
| `AtributoEvolutivo` | Enum de atributos | Permite investir em `Vigor`, `Forca`, `Precisao` e `Guarda`. |
| `Talento` | Enum de talentos | Organiza talentos por ramos ofensivo, defensivo e sustentacao, cada um em camadas liberadas por nivel. |

### Pacote `test`

| Classe | Papel | Como opera |
| --- | --- | --- |
| `tests.TestRunner` | Runner manual de testes | Executa cenarios de regressao para progressao, campanha, arena, armas especiais, status, defesas e bonus da arma principal. |

## Mecanicas principais

### 1. Turnos

Cada rodada da batalha segue esta ordem:

1. inicio do turno do jogador;
2. exibicao de status;
3. escolha da acao do jogador;
4. fim do turno do jogador;
5. turno do inimigo, se ele ainda estiver vivo;
6. atualizacao de recargas e estado geral.

### 2. Acoes do jogador

O jogador pode:

- atacar com qualquer arma do inventario;
- entrar em postura defensiva;
- usar um item de cura;
- usar a habilidade especial `Golpe Heroico`.

### 3. Arma principal

A arma na posicao principal recebe bonus extras:

- dano adicional;
- mais precisao;
- mais chance critica;
- mais bloqueio;
- mais parry.

Isso faz com que a escolha da arma principal seja uma decisao tatica importante entre batalhas.

### 4. Defesa, bloqueio e parry

Ao defender, o jogador passa a usar uma arma de guarda para:

- tentar bloqueio total;
- tentar parry;
- ou ao menos reduzir o dano recebido.

Se o parry acontecer, o jogador responde com um contra-ataque critico.

### 5. Efeitos de status

As criaturas podem sofrer:

- dano continuo, como `Sangramento` e `Queimadura`;
- reducao temporaria de dano, como `Maldicao`.

Esses efeitos sao processados no inicio ou fim do turno, dependendo da regra implementada em `Criatura`.

### 6. Habilidade especial

`Golpe Heroico` causa dano alto e entra em recarga por 3 turnos. O dano final escala com a progressao do jogador.

### 7. Itens

Os consumiveis se dividem em:

- `CURA`: restauram vida e geram escudo temporario;
- `APRIMORAMENTO`: aumentam dano ou atributos criticos.

### 8. Progressao

Ao derrotar bosses, o jogador recebe experiencia. Quando sobe de nivel:

- ganha vida maxima;
- recupera vida;
- recebe pontos de atributo;
- recebe pontos de talento;
- aumenta bonus passivos;
- pode desbloquear novas armas.

### 9. Talentos

Os talentos sao organizados em tres ramos:

- `Ofensivo`
- `Defensivo`
- `Sustentacao`

Cada ramo evolui de forma independente, sem obrigar todos os ramos a avancarem juntos.

### 10. Modos de jogo

#### Batalhas

- modo livre;
- ordem dos bosses definida pelo jogador;
- historico proprio da run;
- util para testar builds, armas e estrategias.

#### Campanha

- modo guiado em capitulos;
- texto de introducao e desfecho por etapa;
- bosses em ordem definida;
- recompensas de historia;
- progressao acumulada ao longo da jornada.

## Estrutura de pastas

```text
src/
  app/
  armas/
    base/
    curta/
    longa/
  batalha/
  campanha/
  combate/
  controle/
  defesas/
  inventario/
  itens/
  personagens/
  progressao/
test/
  tests/
out/
```

### Sobre cada pasta

- `src/`: codigo-fonte principal do jogo.
- `test/`: testes automatizados.
- `out/`: saidas de compilacao geradas localmente.

## Observacoes

- O projeto foi construido para terminal/console, sem interface grafica.
- O codigo esta dividido por responsabilidade, o que facilita extensao e manutencao.
- A campanha e a arena compartilham a mesma base de combate, mudando apenas a forma como as batalhas sao encadeadas.
- O inventario suporta expansao natural com novas armas e novos consumiveis.
- O sistema de inimigos permite adicionar novos bosses criando subclasses de `Inimigo`.

## Autor

Christian Felix Leite
