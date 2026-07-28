import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.anonymouschat.app',
  appName: 'Anonymous Chat',
  webDir: 'dist',
  server: {
    cleartext: true
  }
};

export default config;
