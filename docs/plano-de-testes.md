# Plano de Testes - Backend Petservice

## Introdução

Este documento apresenta o plano de testes para o backend do sistema Petservice. Ele cobre as principais classes de
serviços e valida a funcionalidade central de cada uma, abrangendo cenários de sucesso, falhas e exceções.

## Estrutura e Escopo dos Testes

Os testes incluem validação de comportamento de APIs, manipulação de dados e segurança (autenticação e autorização).
Abaixo, os detalhes de cada serviço e os critérios de aceitação esperados.

---

## Classes de Teste e Funcionalidades

### 1. `AddressServiceImplTest`

| **Funcionalidade**      | Recuperação de Informações de Endereço por CEP                                                                                                                                  |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Valida a integração com a API de endereço (ViaCEP) e verifica se o serviço retorna o endereço corretamente a partir do CEP.                                                     |
| **Cenários de Teste**   | - CEP válido: Retorna todos os campos esperados. <br> - CEP inválido: Retorna erro ou mensagem informativa. <br> - API indisponível: Captura exceção e exibe mensagem adequada. |
| **Verificações**        | - Recuperação de endereço a partir de CEP válido. <br> - Retorno de erro ao informar CEP inválido. <br> - Tratamento de exceção para falha na API externa.                      |
| **Critérios de Aceite** | - O serviço deve funcionar com diferentes APIs externas. <br> - Mensagens de erro devem ser informativas para o usuário.                                                        |

---

### 2. `AuthenticationServiceImplTest`

| **Funcionalidade**      | Autenticação de Usuário - RF001                                                                                                                                                                                                                                                                       |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa o processo de autenticação de usuários, incluindo geração de tokens JWT.                                                                                                                                                                                                                        |
| **Cenários de Teste**   | - Login com credenciais válidas: Retorna um token JWT válido. <br> - Login com credenciais inválidas: Retorna erro de autenticação. <br> - Token expirado: Verifica se o sistema rejeita tokens expirados corretamente. <br> - Token malformado: Retorna erro apropriado sem comprometer a segurança. |
| **Verificações**        | - Login no sistema com sucesso. <br> - Tentativa de login com usuário inválido. <br> - Falha ao não preencher campos obrigatórios. <br> - Login com senha incorreta. <br> - Teste de bloqueio após três tentativas com senha incorreta.                                                               |
| **Critérios de Aceite** | - Usuários devem ser redirecionados após login bem-sucedido. <br> - Mensagens de erro devem ser exibidas em tempo hábil.                                                                                                                                                                              |

---

### 3. `CategoryTagServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Categorias de Serviços - RF005                                                                                                                                                                                                                                                                                     |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Valida as operações de CRUD (Criar, Ler, Atualizar, Excluir) para categorias de serviços.                                                                                                                                                                                                                                           |
| **Cenários de Teste**   | - Criação de categoria válida: Retorna a nova categoria com ID. <br> - Atualização de categoria: Atualiza os campos e verifica persistência. <br> - Exclusão de categoria: Remove a categoria e verifica que ela não é mais acessível. <br> - Erro ao tentar criar com dados inválidos: Verifica se é exibida uma mensagem de erro. |
| **Verificações**        | - Cadastro de nova categoria com dados válidos. <br> - Atualização de categoria com campos válidos. <br> - Exclusão bem-sucedida de uma categoria existente. <br> - Retorno de erro ao cadastrar com dados inválidos.                                                                                                               |
| **Critérios de Aceite** | - O sistema deve evitar duplicação de categorias. <br> - Erros de validação devem ser claros e específicos.                                                                                                                                                                                                                         |

---

### 4. `PetEstablishmentServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Estabelecimentos - RF004                                                                                                                                                                                                                                                                                   |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Valida a criação e manutenção de estabelecimentos, incluindo detalhes como horário de funcionamento e serviços oferecidos.                                                                                                                                                                                                  |
| **Cenários de Teste**   | - Criação de estabelecimento com dados válidos: Salva e retorna o estabelecimento corretamente. <br> - Atualização de estabelecimento: Altera informações e confirma persistência. <br> - Exclusão de estabelecimento: Remove o registro do sistema. <br> - Erro ao criar com dados incompletos: Retorna erro de validação. |
| **Verificações**        | - Criação de estabelecimento com todos os dados preenchidos corretamente. <br> - Teste de edição de informações como horários e categorias. <br> - Exclusão de estabelecimentos registrados. <br> - Validação de campos obrigatórios no momento do cadastro.                                                                |
| **Critérios de Aceite** | - Os horários de funcionamento devem ser armazenados e exibidos corretamente. <br> - Dados incompletos ou inválidos não devem ser salvos.                                                                                                                                                                                   |

---

### 5. `PetGuardianServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Tutores - RF002                                                                                                                                                                                                                                                                                                                           |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa a funcionalidade de CRUD para usuários do tipo guardião de pets, além da validação de permissões de acesso.                                                                                                                                                                                                                                          |
| **Cenários de Teste**   | - Cadastro de guardião com dados válidos: Salva e retorna o guardião com sucesso. <br> - Atualização de perfil do guardião: Persiste as mudanças corretamente. <br> - Exclusão de perfil do guardião: Remove o perfil e verifica que não pode ser acessado. <br> - Acesso não autorizado: Testa se o sistema impede alterações por usuários sem permissão. |
| **Verificações**        | - Criação de guardiões com dados válidos. <br> - Alteração de informações pessoais de guardiões existentes. <br> - Exclusão segura de perfis de guardiões. <br> - Garantia de segurança contra alterações não autorizadas.                                                                                                                                 |
| **Critérios de Aceite** | - Apenas usuários autenticados devem criar ou editar perfis. <br> - Erros de permissão devem ser informados com mensagens claras.                                                                                                                                                                                                                          |

---

### 6. `PetServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Pets - RF006                                                                                                                                                                                                                                                                       |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa operações de CRUD para pets, vinculando-os aos usuários e estabelecimentos apropriados.                                                                                                                                                                                                       |
| **Cenários de Teste**   | - Cadastro de pet com dados válidos: Salva e retorna o pet com sucesso. <br> - Atualização de pet: Atualiza os dados e confirma persistência. <br> - Exclusão de pet: Remove o registro do sistema. <br> - Erro ao tentar cadastrar pet com dados inválidos: Retorna mensagem de erro de validação. |
| **Verificações**        | - Cadastro de novos pets associados a tutores válidos. <br> - Alteração de dados, como raça ou idade. <br> - Remoção segura de pets associados a guardiões. <br> - Validação de dados obrigatórios no momento do cadastro.                                                                          |
| **Critérios de Aceite** | - Pets devem ser únicos por guardião. <br> - Erros de validação devem evitar cadastros duplicados.                                                                                                                                                                                                  |

---

### 7. `AppointmentServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Agendamentos - RF008                                                                                                           |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa operações de CRUD para agendamentos de serviços, incluindo o agendamento de pets com tutores e estabelecimentos.                          |
| **Cenários de Teste**   | - Consulta de agendamento por ID: Retorna o agendamento correto. <br> - Exclusão de agendamento: Remove o agendamento com sucesso.              |
| **Verificações**        | - A busca e exclusão de agendamentos por ID.                                                                                                    |
| **Critérios de Aceite** | - Agendamentos devem ser deletados corretamente do sistema. <br> - Verificar se o método de busca retorna o agendamento correto com base no ID. |

---

### 8. `ChatServiceTest`

| **Funcionalidade**      | Envio de Mensagens - RF007                                                                           |
|-------------------------|------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa a operação de envio de mensagens utilizando RabbitMQ.                                          |
| **Cenários de Teste**   | - Envio de mensagem: Envia a mensagem corretamente para o RabbitMQ.                                  |
| **Verificações**        | - Verificar se o método `convertAndSend` do `rabbitTemplate` foi chamado com os parâmetros corretos. |
| **Critérios de Aceite** | - Mensagens devem ser enviadas corretamente para o RabbitMQ.                                         |

---

### 9. `MessageServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Mensagens - RF007                                              |
|-------------------------|---------------------------------------------------------------------------------|
| **Descrição**           | Testa operações de consulta de mensagens, incluindo busca pela última mensagem. |
| **Cenários de Teste**   | - Consulta de últimas mensagens: Retorna a mensagem correta do banco.           |
| **Verificações**        | - Consultar as mensagens mais recentes.                                         |
| **Critérios de Aceite** | - A consulta deve retornar as últimas mensagens corretamente.                   |

---

### 10. `ReviewServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Avaliações - RF009, RF010, RF011, RF012                                                                                                                                                                                                  |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa operações de CRUD para avaliações, incluindo criação, atualização, exclusão e busca de avaliações por autor.                                                                                                                                        |
| **Cenários de Teste**   | - Consulta de avaliação por ID: Retorna a avaliação correta. <br> - Exceção ao não encontrar avaliação: Lança exceção apropriada. <br> - Criação de avaliação: Cria avaliação corretamente. <br> - Exclusão de avaliação: Remove a avaliação com sucesso. |
| **Verificações**        | - Consultar e excluir avaliações. <br> - Lançar exceções adequadas quando não encontrar avaliações.                                                                                                                                                       |
| **Critérios de Aceite** | - Avaliações devem ser associadas a autores corretamente. <br> - Exceções devem ser lançadas corretamente quando a avaliação não for encontrada.                                                                                                          |

---

### 11. `ServiceProvidedServiceImplTest`

| **Funcionalidade**      | Gerenciamento de Serviços Prestados - RF005                                                                                                                                                                                                                          |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descrição**           | Testa operações de CRUD para serviços prestados por estabelecimentos de pets, incluindo criação, consulta e exclusão de serviços.                                                                                                                                    |
| **Cenários de Teste**   | - Consulta de serviço prestado por ID: Retorna o serviço corretamente. <br> - Criação de serviço prestado: Cria o serviço corretamente. <br> - Exceção ao não encontrar serviço prestado: Lança exceção apropriada. <br> - Consulta de serviços por estabelecimento. |
| **Verificações**        | - Consultar serviços prestados. <br> - Lançar exceção quando o serviço não for encontrado. <br> - Criar e associar corretamente os serviços aos estabelecimentos.                                                                                                    |
| **Critérios de Aceite** | - Serviços prestados devem ser únicos e associados ao estabelecimento correto. <br> - Exceções de "não encontrado" devem ser tratadas adequadamente.                                                                                                                 |

## Estratégia de Teste

### Tipos de Testes

1. **Testes Unitários**: Validar a lógica interna dos métodos em cada classe de serviço.
2. **Testes de Integração (Via CEP)**: Testar a integração entre as classes de serviço e componentes externos, como APIs
   e banco de dados.
3. **Testes de Exceção**: Avaliar o comportamento do sistema quando ocorre uma falha, como problemas de rede ou dados
   inválidos.

### Ferramentas de Teste

- **JUnit**: Para execução dos testes unitários e de integração.
- **Mockito**: Para criação de mocks das dependências dos serviços.
- **Spring Test**: Para configuração de ambiente de teste em aplicações Spring Boot.

### Ambiente de Teste

- Ambiente de desenvolvimento configurado com banco de dados em memória para testes e simulação de APIs externas.

---

## Classificação de Bugs

| ID | Nível de Severidade | Descrição                                                                                |
|----|---------------------|------------------------------------------------------------------------------------------|
| 1  | Blocker             | Erros críticos que impedem o funcionamento do sistema.                                   |
| 2  | Grave               | Funcionalidades principais falhando, como falha na autenticação ou criação de registros. |
| 3  | Moderada            | Erros em funcionalidades secundárias, como avaliações.                                   |
| 4  | Pequena             | Erros menores, como mensagens incorretas ou validações insuficientes.                    |

---

## Definição de Pronto

Considera-se que a funcionalidade está pronta quando:

- Todos os testes unitários e de integração passam com sucesso.
- Não há bugs com severidade acima de "Moderada".