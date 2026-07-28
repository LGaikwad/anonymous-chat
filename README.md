# Anonymous Real-Time Chat

A mobile-first anonymous chat application built with:

- React + TypeScript + Vite
- Java 17 + Spring Boot
- Spring WebSocket + STOMP
- Spring Data JPA
- PostgreSQL
- Docker Compose

## Features

- Create a room and get a shareable URL
- Join without authentication
- Automatic temporary USER-XXXX identity
- Real-time messaging using WebSockets
- Messages persisted in PostgreSQL
- Recent message history
- Online user count
- Basic validation and rate limiting
- Mobile-friendly UI

## Run with Docker

Requirements:
- Docker Desktop

From the project root:

```bash
docker compose up --build
```

Open:

http://localhost:5173

## Run without Docker

### Backend

Requirements:
- Java 17+
- Maven 3.9+
- PostgreSQL

Set database environment variables or use the defaults:

```text
DB_HOST=localhost
DB_PORT=5432
DB_NAME=anonymous_chat
DB_USERNAME=chat
DB_PASSWORD=chat
```

Then:

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

http://localhost:8080

### Frontend

Requirements:
- Node.js 20+

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

http://localhost:5173

## Important for real devices

If testing from a phone on the same Wi-Fi as your computer, replace localhost in the frontend API configuration with your computer's LAN IP, for example:

```text
VITE_API_BASE_URL=http://192.168.1.10:8080
VITE_WS_URL=ws://192.168.1.10:8080/ws
```

For production use HTTPS and WSS.

## API

```text
POST /api/rooms
GET  /api/rooms/{roomCode}
GET  /api/rooms/{roomCode}/messages
```

WebSocket endpoint:

```text
/ws
```

Subscribe:

```text
/topic/room/{roomCode}
```

Send:

```text
/app/chat/{roomCode}
```

## Data stored

The application intentionally does not have user accounts.

Messages store only:

- message ID
- room
- temporary user code
- text
- timestamp

No name, email, phone number, password, or profile is collected.

## Production notes

Before public deployment, add:
- HTTPS/WSS
- stronger rate limiting backed by Redis
- CAPTCHA/abuse protection if needed
- moderation/reporting
- database backups
- monitoring/logging
- room/message retention policy


## Android

See `README-ANDROID.md`.

The frontend is Capacitor-ready and can be wrapped as an Android app.
