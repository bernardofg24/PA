Este trabalho teve como objetivo desenvolver uma biblioteca de produção e edição de dados no formato de JSON.

A primeira parte foi dividida em duas fases. A primeira prendeu-se com o modelo. Para isso, foram criadas várias
classes para os diferentes tipos de variáveis que o JSON poderia compreender, isto é, array, boolean, char, collections, 
enum, map, number e string. Todas as classes criadas foram nomeadas de "JSON" seguidas do tipo de variável em questão, 
por exemplo "JSONBoolean". Estas serviram, nomeadamente, para serem moldadas e unidas num JSON object final, com o nome "JSONObj".

De maneira a testar o código desenvolvido, foi criada o file "test.kt". Para isso, foram ainda criadas as classes "Aluno" 
e "UC", de maneira a que o resultado final se assemelhasse ao exemplo dado no ununciado da primeira parte.

Ainda nesta parte, foi pedido que se desenvolvesse uma forma de varrimento, baseado no padrão de desenho Visitor, que permitisse 
efetuar pesquisas, e que obedecesse a determinada estrutura. Ao nível das pesquisas, foi criada a classe "SearchVisitor". Esta 
permite guardar e obter todos os valores com determinado identificador. O seguinte, nomeado de "ValidationVisitor", permite verificar 
se o modelo obdece a determinadas regras, como se as variáveis pertecentes à mesma propriedade têm a mesma estrutura.

Em relação à parte seguinte, era pedido que se adicionasse anotações, como forma de adaptar a instanciação. Para isso, foram criadas 
duas class annotation classes. A primeira, foi denominada de "DoNotInitiate", que tem como objetivo de aplicar a uma determinada propriedade, 
fazendo com que esta não inicialize como JSON Object. A seguinte, a qual chamamos de "ToString", dá substitui o tipo de variável de uma 
dada propriedade, inicializando-a como JSONString.


Passando para a segunda parte do projeto, era pedido que o editor tivesse uma aplicação gráfica. Esta seria dividida em duas colunas, a esquerda 
e a direita. Na primeira é feita a edição dos vários componentes, e na outra, seria feita a projeção textual do modelo JSON, alterada a cada edição. 
Para isso, foi necessário desenvolver observadores.


