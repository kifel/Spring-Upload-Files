# UPLOAD FILE SPRING BOOT

Projeto de uma api para uplodad download e visualizar arquivos, construido com Sprig boot 3!

![Badge em Desenvolvimento](https://img.shields.io/static/v1?label=STATUS&message=PROJETO%20EM%20TESTE&color=GREEN&style=for-the-badge)

---

## 📋 Índice

- [Sobre](#upload-file-spring-boot)
- [Tecnologias utilizadas](#-tecnologias-utilizadas)
- [Como executar o projeto](#-como-executar-o-projeto)
- [Construído com](#%EF%B8%8F-construído-com)
- [Autores](#%EF%B8%8F-autores)

---

## 🚀 Tecnologias utilizadas

O projeto está desenvolvido utilizando as seguintes tecnologias:

- Spring boot 3.1.1
- Java 17.0.6
- PostgreSQL

---

## ⌨ Como executar o projeto

Para executar o projeto, é necessário ter a versão 17 do Java instalada em seu sistema e o baco de dados PostgreSQL.

1. Certifique-se de ter a versão 17 do Java instalada em seu sistema. Você pode verificar a versão atual do Java digitando o seguinte comando no terminal:

```shell
java -version
```

Se a versão não corresponder a 17, você precisará atualizar sua instalação do Java.

2. Instale o PostgreSQL na sua maquina e faça as configurações inciais do mesmo na sua maquina, segue o link oficial do banco de dados:
```shell
https://www.postgresql.org/download/
```

3. Faça o download do código-fonte do projeto Spring a partir do GitHub. Você pode clonar o repositório usando o seguinte comando no terminal:

```shell
git clone https://github.com/kifel/Spring-Upload-Files.git
```

4. Crie o banco de dados para utilziar no projeto.

5. Configure o arquivo .env alterando a URL da API, conecção com o banco e senha do PostgreSQL, segue o padrao do .env
```m
DB_URL=jdbc:postgresql://localhost:5432/nome_do_banco_de_dados_criado
DB_USERNAME=usuario_do_banco
DB_PASSWORD=senha_do_banco
API_URL=http://localhost:8080
```

6. Após a conclusão da instalação das dependências, execute o Spring Boot.

7. Segue os endpoints da aplicação:
- GET
 - http://localhost:8080/api/file/view/{NOME_DO_AQUIVO}
 - http://localhost:8080/api/file/download/{NOME_DO_AQUIVO}
- POST
 - http://localhost:8080/api/file/upload

O post é feito com Multipart, sendo enviado em file, exemplo:
![imagem de exemplo](https://cdn.discordapp.com/attachments/971904895043125258/1127653025905905708/image.png)

---

## 🛠️ Construído com

- [Visual Studio Code](https://code.visualstudio.com/) - ferramenta de desenvolvimento

---

## ✒️ Autores

- **João Victor de Mello Pereira** - _Desenvolvimento do código_ - [Kifel](https://github.com/kifel)

---

## 🎁 Expressões de gratidão

- Conte a outras pessoas sobre este projeto 📢
- Obrigado por ver esse projeto ❤️

---
