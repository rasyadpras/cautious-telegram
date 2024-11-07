# CRUD Sederhana dengan Spring Boot

## Deskripsi
Aplikasi ini adalah aplikasi CRUD sederhana yang dibuat dengan menggunakan Spring Boot, 
memiliki fitur CRUD (Create, Read, Update, Delete) yang digunakan untuk mengelola data user. 
Aplikasi ini menggunakan database PostgresSQL sebagai pengelolaan data.

## Fitur
- User dapat membuat akun dan login untuk mengakses aplikasi.
- User dapat menambah, melihat, mengubah, dan menghapus data user dengan menggunakan autentikasi pengguna.
- Terdapat role admin dan customer, dan setiap akun dapat menggunakan multi role.
- Terdapat fitur paging saat mengakses semua data user.
- Terdapat fitur pencarian data user berdasarkan nama.
- Penggunaan cache untuk mempercepat akses data user.

## Instalasi
1. Pastikan anda menginstall Java Development Kit (JDK) minimal versi 17 dan Maven pada komputer anda.
2. Clone repository ini ke lokal dengan menggunakan perintah
    ```bash
    git clone https://github.com/rasyadpras/cautious-telegram.git
    ```
3. Buka terminal atau command prompt dan arahkan ke direktori proyek di mana anda menyimpan file.
4. Pastikan anda telah mengkonfigurasi file `application.properties` dengan benar.
5. Jalankan perintah berikut untuk menjalankan aplikasi:
    ```shell
    mvn spring-boot:run
    ```

###### Catatan
Developer menggunakan Java versi 17 untuk membangun aplikasi ini.
Anda dapat menyesuaikan versi JDK yang anda gunakan dengan mengubah properties pada file `pom.xml`.

## Testing
1. Buka terminal atau command prompt dan arahkan ke direktori proyek di mana anda menyimpan file.
2. Jalankan perintah berikut untuk menjalankan integration test:
    ```shell
    mvn test -Dtest=ControllerTest
    ```

## API Specification

### Authentication

#### Register Account

Request:

- Method: POST
- Endpoint: `/api/auth/register`
- Header:
   - Content-Type: application/json
   - Accept: application/json
- Body:

```json
{
   "email": "string",
   "password": "string"
}
```

Response:

- Roles: `ADMINISTRATOR | CUSTOMER`

```json
{
   "statusCode": 201,
   "message": "Created",
   "data": {
      "id": "string",
      "email": "string",
      "roles": [
         "string"
      ],
      "createdAt": "localDateTime",
      "updatedAt": "localDateTime"
   },
   "paging": null
}
```

#### Login Account

Request:

- Method: POST
- Endpoint: `/api/auth/login`
- Header:
   - Content-Type: application/json
   - Accept: application/json
- Body:

```json
{
   "email": "string",
   "password": "string"
}
```

Response:

- Roles: `ADMINISTRATOR | CUSTOMER`

```json
{
   "statusCode": 200,
   "message": "OK",
   "data": {
      "email": "string",
      "token": "string",
      "roles": [
         "string"
      ]
   },
   "paging": null
}
```

### User

#### Create User

Request:

- Method: POST
- Endpoint: `/api/users`
- Header:
   - Authentication: Bearer `<JWT Token>`
   - Content-Type: application/json
   - Accept: application/json
- Body:

```json
{
   "name": "string",
   "age": "integer",
   "membership": "boolean"
}
```

Response:

```json
{
   "statusCode": 201,
   "message": "Created",
   "data": {
      "id": "string",
      "name": "string",
      "age": "integer",
      "membership": "boolean",
      "createdAt": "localDateTime",
      "updatedAt": "localDateTime"
   },
   "paging": null
}
```

#### Get All Users

Request:

- Method: GET
- Endpoint: `/api/users`
- Header:
   - Authentication: Bearer `<JWT Token>`
   - Accept: application/json
- Query Param:
    - page: number
    - size: number
    - name: string `optional`

Response:

```json
{
   "statusCode": 200,
   "message": "OK",
   "data": [
      {
         "id": "string",
         "name": "string",
         "age": "integer",
         "membership": "boolean",
         "createdAt": "localDateTime",
         "updatedAt": "localDateTime"
      }
   ],
   "paging": {
      "totalPages": "number",
      "page": "number",
      "size": "number",
      "hasNext": "boolean",
      "hasPrev": "boolean"
   }
}
```

#### Get User By Id

Request:

- Method: GET
- Endpoint: `/api/users/{id}`
- Header:
   - Authentication: Bearer `<JWT Token>`
   - Accept: application/json

Response:

```json
{
   "statusCode": 200,
   "message": "OK",
   "data": {
      "id": "string",
      "name": "string",
      "age": "integer",
      "membership": "boolean",
      "createdAt": "localDateTime",
      "updatedAt": "localDateTime"
   },
   "paging": null
}
```

#### Update User

Request:

- Method: PUT
- Endpoint: `/api/users/{id}`
- Header:
   - Authentication: Bearer `<JWT Token>`
   - Content-Type: application/json
   - Accept: application/json
- Body:

```json
{
   "name": "string",
   "age": "integer",
   "membership": "boolean"
}
```

Response:

```json
{
   "statusCode": 200,
   "message": "OK",
   "data": {
      "id": "string",
      "name": "string",
      "age": "integer",
      "membership": "boolean",
      "createdAt": "localDateTime",
      "updatedAt": "localDateTime"
   },
   "paging": null
}
```

#### Delete User

Request:

- Method: DELETE
- Endpoint: `/api/users/{id}`
- Header:
   - Authentication: Bearer `<JWT Token>`
   - Accept: application/json

Response:

```json
{
   "statusCode": 200,
   "message": "OK",
   "data": "User with id {id} has been deleted",
   "paging": null
}
```

## Dokumentasi 
Anda juga bisa melihat dokumentasi API lengkap dengan akses ke endpoint berikut setelah memulai aplikasi: http://localhost:8080/docs
