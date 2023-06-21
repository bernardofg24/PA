Este trabalho teve como objetivo desenvolver uma biblioteca de geração de estruturas JSON a partir de objetos e edição das mesmas.

A biblioteca encontra-se dividida em dois packages:
    model -> "back-end" do projeto - Trata da conversão de objetos em estruturas JSON e parsing das mesmas para formato
                                     String.
                                     Permite ainda a possibilidade de efetuar pesquisas e validações através de Visitors:
                                        SearchVisitor (como usar) -> instanciar o visitor; passar um JSONObj à função "visit"
                                                                     do mesmo;
                                                                     aceder à propriedade "values" do visitor para obter os
                                                                     resultados
                                        ValidationVisitor (como usar) -> igual ao SearchVisitor mas a propriedade com o
                                                                         resultado chama-se "bool"

    gui -> "front-end" do projeto - Correndo a "main" do ficheiro "Controller" é apresentado um editor de JSON onde é
                                    possível efetuar as seguintes operações:
                                        - adicionar e remover propriedades de objetos;
                                        - trocar o valor de propriedades de objetos;
                                        - adicionar e remover valores/entradas de arrays e listas/mapas;
                                        - trocar valores/entradas de arrays e listas/mapas.
                                    É possível efetuar "Undo" em todos os tipos de operações.

Bugs conhecidos:
    model:
        - Nenhum bug documentado mas é possível a sua existência.

    gui:
        - No caso de adições ou remoções, apenas é possível efetuar "Undo" das mesmas se estas tiverem sido a última operação
          efetuada;
        - Se remover todos os valores de um array ou lista, o editor bloqueia.