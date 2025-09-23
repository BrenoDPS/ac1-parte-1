# TDD Projeto - Gamificação para Engajamento

Este repositório contém a implementação de funcionalidades para uma plataforma de cursos online, utilizando as metodologias **Test-Driven Development (TDD)** e **Behavior-Driven Development (BDD)**, seguindo o processo **Agile Testing Driven Development (ATDD)**. O projeto foi desenvolvido pela equipe de Breno de Pádua Soares, Diogo Francia, Lucas da Silva Marques e Raphael Nobuyuki Haga Okuyama.

---

## Estudo de Caso

A plataforma de cursos online, no modelo de assinaturas, busca implementar um sistema de gamificação para aumentar o engajamento dos alunos. O desafio é desenvolver novas funcionalidades que incentivam a participação e recompensam o desempenho e a interação na comunidade.

---

## User Stories

As funcionalidades foram definidas a partir das seguintes User Stories:

* **US-1: Ranking de Engajamento**
    * **Como** um aluno interessado em desafios,
    * **Eu quero** visualizar meu posicionamento em um ranking de participação da comunidade,
    * **Para que** eu possa acompanhar minha evolução e competir de forma saudável com outros colegas.

* **US-2: Cursos Extras por Mérito**
    * **Como** um aluno assinante do plano básico,
    * **Eu quero** receber 3 cursos extras sempre que eu concluir um curso com média acima de 7,0,
    * **Para que** eu possa aumentar meu aprendizado e ter mais acesso a novos conteúdos sem custo adicional.

* **US-3: Recompensas por Engajamento**
    * **Como** um aluno engajado,
    * **Eu quero** ganhar recompensas (como um curso gratuito no fim do mês),
    * **Para que** eu possa ter acesso a mais conhecimento e ver valor na minha participação.

* **US-4: Migração para Plano Premium**
    * **Como** um aluno que atingiu 12 cursos concluídos,
    * **Eu quero** migrar automaticamente para o plano Premium e receber vouchers para projetos reais, além de acumular moedas de recompensa,
    * **Para que** eu possa aplicar meus conhecimentos em experiências práticas e expandir minha formação.

---

## Cenários BDD (Behavior-Driven Development)

Os BDDs abaixo detalham o comportamento esperado para a User Story de **Ranking de Engajamento**.

### Cenário 1: Visualizar posição no ranking semanal
**Background:**
* Dado que o usuário "joao@example.com" está autenticado.

**Cenário:**
* **Dado que** existe um ranking semanal com a pontuação do usuário "joao@example.com".
* **Quando** eu acesso a tela de "Ranking de Engajamento" para o período "Semana".
* **Então** eu devo ver minha posição, meus pontos totais e a variação em relação à semana anterior.

### Cenário 2: Filtrar ranking por período
**Background:**
* Dado que o usuário "maria@example.com" está autenticado.

**Cenário:**
* **Esquema do Cenário**: Filtrar ranking por período.
* **Dado que** existem registros de participação apropriados para o período `<Período>`.
* **Quando** seleciono o filtro de período `<Período>` na tela de ranking.
* **Então** os pontos e a posição apresentados correspondem aos dados do período `<Período>`.

**Exemplos:**
* | Período |
* | Semana |
* | Mês |
* | Geral |

### Cenário 3: Pontuação é atualizada após ações de engajamento
* **Dado que** sou "paulo@example.com" autenticado e tenho 50 pontos no período "Semana".
* **Quando** eu realizo uma ação de engajamento válida que vale 10 pontos (ex.: completo um desafio).
* **E** eu recarrego a tela de ranking.
* **Então** meus pontos totais devem ser "60".
* **E** minha posição deve refletir a nova ordenação de pontos.