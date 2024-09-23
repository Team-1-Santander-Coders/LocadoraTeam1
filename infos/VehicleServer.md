#### Verificar VehicleDTO

Verifica o método toJson no VehicleDTO no meio

### Documentação do Código: `VehicleServer`

Este código define um servidor HTTP simples que manipula operações relacionadas a veículos. Ele foi desenvolvido usando a biblioteca `com.sun.net.httpserver.HttpServer` para criar um backend com várias rotas que servem tanto arquivos estáticos (HTML, CSS, JS) quanto operações CRUD para veículos (Carro, Moto, Caminhão).

---

### Classe: `VehicleServer`
- Responsável por iniciar o servidor e gerenciar as rotas HTTP.
- Depende do `VehicleService` para realizar operações relacionadas a veículos.

---

### Método: `StartServer()`
Inicia o servidor na porta 8000 e configura os seguintes contextos (rotas):
- **`/`**: Serve o arquivo HTML principal.
- **`/style.css`**: Serve o arquivo CSS.
- **`/script.js`**: Serve o arquivo JavaScript.
- **`/vehicles`**: Manipula requisições `GET` para listar veículos.
- **`/vehicle`**: Manipula requisições `POST` para criar um veículo.
- **`/vehicle/edit`**: Manipula requisições `PUT` para editar um veículo.
- **`/vehicle/delete`**: Manipula requisições `DELETE` para deletar um veículo.

---

### Classe: `StaticFileHandler`
Manipulador responsável por servir arquivos estáticos (HTML, CSS, JS).
- **Construtor**: Recebe o nome do arquivo que será servido.
- **Método `handle(HttpExchange exchange)`**:
    - Lê o arquivo solicitado e o envia como resposta HTTP.
    - Define o tipo de conteúdo (HTML, CSS, JS) com base na extensão do arquivo.

---

### Classe: `VehicleListHandler`
Manipulador responsável por listar todos os veículos.
- **Método `handle(HttpExchange exchange)`**:
    - Obtém a lista de veículos do `VehicleService`.
    - Formata cada veículo como uma string contendo informações como tipo, placa, modelo, ano e disponibilidade.
    - Envia a resposta de volta ao cliente com a lista formatada.

---

### Classe: `VehicleCreateHandler`
Manipulador responsável por criar um novo veículo.
- **Método `handle(HttpExchange exchange)`**:
    - Verifica se o método HTTP é `POST`.
    - Lê os dados do veículo a partir do corpo da requisição.
    - Com base no tipo de veículo (Carro, Moto, Caminhão), cria uma entidade correspondente e a transforma em um DTO.
    - Usa o serviço `VehicleService` para adicionar o veículo.
    - Responde com uma mensagem de sucesso ou erro (conflito se a placa já existir).

---

### Classe: `VehicleEditHandler`
Manipulador responsável por editar um veículo existente.
- **Método `handle(HttpExchange exchange)`**:
    - Verifica se o método HTTP é `PUT`.
    - Lê os dados do veículo a ser editado a partir do corpo da requisição.
    - Atualiza o veículo usando o `VehicleService`.
    - Responde com uma mensagem de sucesso ou erro (404 se o veículo não for encontrado).

---

### Classe: `VehicleDeleteHandler`
Manipulador responsável por deletar um veículo.
- **Método `handle(HttpExchange exchange)`**:
    - Verifica se o método HTTP é `DELETE`.
    - Lê a placa do veículo a ser deletado a partir do corpo da requisição.
    - Usa o `VehicleService` para deletar o veículo com a placa correspondente.
    - Responde com uma mensagem de sucesso ou erro (404 se o veículo não for encontrado).

---

### Fluxo de Trabalho

1. O servidor é iniciado com `VehicleServer.StartServer()`.
2. Para cada requisição HTTP, o manipulador correspondente é chamado.
3. O servidor lida com operações de criação, leitura, atualização e exclusão de veículos via DTOs, com tratamento de exceções, como duplicação de placas ou veículos não encontrados.
4. Arquivos estáticos (HTML, CSS, JS) são servidos diretamente para o frontend a partir do diretório `src/resources/static`.

### Observações:
- As rotas implementadas seguem padrões REST, e as operações estão segregadas por métodos HTTP (GET, POST, PUT, DELETE).
- O código implementa tratamento básico de exceções, como veículos duplicados (409) ou inexistentes (404).
