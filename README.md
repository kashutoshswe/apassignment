
# AP Assignment

## Overview
This project is a Spring Boot application designed to handle user authentication, authorization, and JWT token management. It also provides basic functionalities like token refresh and revocation. This README will guide you through setting up and running the project using Docker, with a single command.

## Prerequisites

- **Docker Desktop** installed on your machine (Windows, macOS, or Linux).
- **Java 23+** installed for development (optional if you're using pre-built Docker images).
- **Maven** installed (if building the application locally).

## Running the Application

### Clone the Repository

Clone the repository to your local machine using:
To run the application use the last command mentioned for docker

```bash
git clone https://github.com/kashutoshswe/apassignment.git
cd apassignment
docker-compose up --build
```


## Testing the APIs

Below are all the API endpoints with their corresponding cURL commands for testing.

### 1. Sign Up
Register a new user:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
-H "Content-Type: application/json" \
-d '{"username":"user3", "password":"user3"}'
```

### 2. Sign In
Authenticate the user and retrieve an access token:
```bash
curl -X POST http://localhost:8080/api/auth/signin \
-H "Content-Type: application/json" \
-d '{"username":"user3", "password":"user3"}'
```

#### Expected Response:
The response contains `access_token` and `refresh_token`. Copy the `access_token` for further testing.

---

### 3. Refresh Token
Generate a new access token using the refresh token:
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
-H "Content-Type: application/json" \
-d '{"refreshToken":"<refresh_token_received_from_signin>"}'
```

---

### 4. Revoke Token
Revoke the given access token:
```bash
curl -X POST http://localhost:8080/api/auth/revoke \
-H "Content-Type: application/json" \
-d '{"accessToken":"<access_token_to_revoke>"}'
```

---

## API Summary
| Endpoint                  | Method | Description                      |
|---------------------------|--------|----------------------------------|
| `/api/auth/signup`        | POST   | Register a new user              |
| `/api/auth/signin`        | POST   | Authenticate user credentials    |
| `/api/auth/refresh`       | POST   | Refresh access token             |
| `/api/auth/revoke`        | POST   | Revoke a specific access token   |

---

## Troubleshooting
- Make sure Docker and Docker Compose are installed and running.
