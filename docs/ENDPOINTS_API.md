# Documentação da API REST

## Visão Geral

API RESTful para gerenciamento de usuários, conteúdos, engajamentos e rankings.

**Base URL**: `http://localhost:8080/api/v1`

**Versão**: 1.0.0

---

## Endpoints de Usuários

### 1. Criar Usuário

**POST** `/usuarios`

**Request Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@teste.com"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@teste.com",
  "pontuacaoTotal": 0,
  "dataCadastro": "2024-11-17T10:00:00",
  "ativo": true
}
```

---

### 2. Buscar Usuário por ID

**GET** `/usuarios/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@teste.com",
  "pontuacaoTotal": 100,
  "ativo": true
}
```

**Errors:**
- `404 Not Found` - Usuário não encontrado

---

### 3. Listar Usuários Ativos

**GET** `/usuarios`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "pontuacaoTotal": 150
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "pontuacaoTotal": 200
  }
]
```

---

### 4. Atualizar Usuário

**PUT** `/usuarios/{id}`

**Request Body:**
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@teste.com"
}
```

**Response:** `200 OK`

**Errors:**
- `404 Not Found` - Usuário não encontrado
- `400 Bad Request` - Email já cadastrado

---

### 5. Desativar Usuário

**DELETE** `/usuarios/{id}`

**Response:** `204 No Content`

---

### 6. Adicionar Pontos

**POST** `/usuarios/{id}/pontos?pontos=50`

**Response:** `200 OK`
```json
{
  "id": 1,
  "pontuacaoTotal": 150
}
```

---

### 7. Ranking de Usuários

**GET** `/usuarios/ranking`

**Response:** `200 OK`
```json
[
  {
    "id": 2,
    "nome": "Maria Santos",
    "pontuacaoTotal": 200
  },
  {
    "id": 1,
    "nome": "João Silva",
    "pontuacaoTotal": 150
  }
]
```

---

## Códigos de Status HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado |
| 204 | No Content - Operação sem retorno |
| 400 | Bad Request - Dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro interno |

---

## Formato de Erro

```json
{
  "timestamp": "2024-11-17T10:00:00",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Usuario com ID 999 não encontrado",
  "path": "/api/v1/usuarios/999"
}
```

---

## Testando com cURL

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@teste.com"}'

# Buscar usuário
curl http://localhost:8080/api/v1/usuarios/1

# Listar usuários
curl http://localhost:8080/api/v1/usuarios

# Adicionar pontos
curl -X POST "http://localhost:8080/api/v1/usuarios/1/pontos?pontos=50"
```
