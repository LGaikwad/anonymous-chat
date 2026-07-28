# Anonymous Chat — Android-ready build

This project now supports both:

1. Mobile/browser web app
2. Android app using Capacitor

## Prerequisites

Install:

- Node.js 20+
- Android Studio
- Android SDK
- JDK 17+
- Docker Desktop (for backend/database)

## 1. Start backend and database

From the project root:

```bash
docker compose up --build
```

The backend is available at:

```text
http://localhost:8080
```

## 2. Configure Android app for a real online backend

For an Android phone, do NOT use `localhost` for the backend. `localhost` means the phone itself.

Create:

```text
frontend/.env
```

For example:

```text
VITE_API_BASE_URL=https://YOUR-BACKEND-DOMAIN
VITE_WS_URL=wss://YOUR-BACKEND-DOMAIN/ws
```

Use HTTPS/WSS when deployed publicly.

## 3. Install frontend dependencies

```bash
cd frontend
npm install
```

## 4. Add Android platform

```bash
npm run android:add
```

This creates:

```text
frontend/android/
```

## 5. Build and sync

```bash
npm run android:sync
```

## 6. Open Android Studio

```bash
npm run android:open
```

Then select an emulator or connected Android phone and press Run.

## Build APK

In Android Studio:

```text
Build
→ Generate App Bundle(s) or APK(s)
→ Generate APK(s)
```

For a release APK/AAB, configure a signing key in Android Studio.

## Important

The Android wrapper does not magically make the backend public.

For other people to chat from their phones, deploy the Spring Boot backend and PostgreSQL database online, then set:

```text
VITE_API_BASE_URL=https://your-api-domain
VITE_WS_URL=wss://your-api-domain/ws
```

After changing `.env`, rebuild and sync the Android app.

## Current architecture

```text
Android App / Browser
        |
        | HTTPS + WSS
        v
Spring Boot API + WebSocket
        |
        v
PostgreSQL
```

No user accounts are used.

The app stores only:

- room
- temporary user code
- message
- timestamp

## Production checklist

Before public launch:

- Use HTTPS/WSS
- Restrict WebSocket allowed origins
- Add Redis-backed rate limiting
- Add abuse/spam protection
- Add database backups
- Configure CORS correctly
- Add logging/monitoring
- Decide message retention policy
- Configure Android release signing
