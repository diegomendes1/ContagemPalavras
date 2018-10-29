# Contagem de Palavras

API REST criada com Spring responsável por ler arquivos de diferentes formatos e gerar relatórios sobre suas palavras e quantidades.

Esse programa é capaz de:

 - Através do método POST, no endereço "/novo-processo" e enviando um arquivo, sua aplicação de captura e sua extensão, o programa realiza a contagem das palavras e suas repetições, salva no banco de dados(MSSQL) e retorna um arquivo .xls(Excel) com o resultado do processamento.
 
 - Com o método POST no endereço "/ajustes", ao receber uma lista de tipos desejados e aplicações de capturas, é possível atualizar as configurações do programa.
 
 - Pelo método GET, no endereço "/busca", enviando o tipo de arquivo desejado, aplicação de caputra e uma data, o programa retorna uma lista com os resultados já realizados anteriormente que atendem as especificações. Não é necessário enviar esses dados pois se a busca for realizada sem nenhum dos filtros acima, todos os resultados já feitos anteriormente serão retornados.
 
 - Usando o método GET no endereço "/arquivo-processado", enviando o nome de um resultado já realizado antes, o programa retorna esses resultados numa planilha .xls(Excel).
 
  O programa aceita vários tipos diferentes: txt, doc, docx, pdf, jpg e tif. Além disso, É simples adicionar novos tipos no futuro.
  
  O MSSQL é usado para o uso do banco de dados. Pela natureza desse sistema, em cada computador um banco diferente é criado, portanto é preciso atualizar a url da conexão no computador, que está localizada na classe "Ajustes".
