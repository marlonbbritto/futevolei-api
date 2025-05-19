# 🏐 Futevôlei Championship API

API REST para gerenciamento de campeonatos de futevôlei com estrutura de **eliminação dupla**, desenvolvida com **Spring Boot 3.4.5** e **Java 21**. O sistema permite o cadastro de duplas, sorteio de confrontos, registro de resultados e montagem automática das chaves.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA
- H2 Database (modo desenvolvimento)
- Lombok
- Spring Boot DevTools

---

## 📦 Funcionalidades

- ✅ Cadastro de duplas participantes
- ✅ Definição dinâmica da quantidade de duplas
- ✅ Sorteio de confrontos iniciais
- ✅ Geração e atualização de chaves (winners e losers brackets)
- ✅ Registro de resultados
- ✅ Avanço automático das duplas conforme vitórias ou eliminações
- ✅ Final com as duas melhores duplas da chave dos ganhadores vs chave dos perdedores

---

## 🧠 Modelo de Domínio

As principais entidades do sistema são:

- `Dupla`
- `Jogador`
- `Campeonato`
- `Partida`
- `Chave` (Winners / Losers)
- `Resultado`