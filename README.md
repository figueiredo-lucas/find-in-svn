# find-in-svn

Pequeno utilitário criado para buscar conteúdo dentro de arquivos em um SVN remoto através do seu link, usuário e senha.

#Uso

--help ou -h : Help do sistema.

--user ou -u : Usuário de conexão com o svn.

--pass ou -p : Senha de conexão com o svn.

--svn ou -s : URL do svn.

# Exemplo
java -jar FindInSvn.jar -sup http://url/do/svn usuario senha

OBS: Os parâmetros de URL, Usuário e Senha são obrigatórios. Caso não informados no comando, serão solicitados na execução.
