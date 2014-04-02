Laser
===

Simulate laser pointer using Android, Node.js and socket.io

## How to use

Requirements:

* Node.js
* Android SDK

===

### Server

Install server dependencies:

```bash
cd laser-server/
npm install
```

Get server running

```bash
node app.js
```

Server should be running on port 33001 (localhost)

Test on browser:

```bash
http://localhost:33001
```
===

### Android app

Install the Android SDK

Connect your device to the computer by USB

Install the apk using adb:

```bash
cd <android_sdk_folder>/platform-tools/
./adb install <laser_folder>/Laser.apk
```

