# Deployment outline

## Backend

Deploy the `backend` Docker image to a service that supports Docker.

Required environment variables:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
```

Expose port 8080.

## PostgreSQL

Use a managed PostgreSQL database or a persistent PostgreSQL container.

Do not use the development credentials in production.

## Android

After the backend has a public HTTPS domain:

```text
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_WS_URL=wss://api.yourdomain.com/ws
```

Then:

```bash
cd frontend
npm install
npm run android:sync
npm run android:open
```

Build the signed APK/AAB from Android Studio.
