# ChessRun

A mobile chess running game — wrapped as an offline-first Android app built automatically via GitHub Actions.

---

## Table of Contents

1. [About](#about)
2. [Android Build Quickstart](#android-build-quickstart)
3. [Step 1 — Generate an Upload Keystore (no local computer needed)](#step-1--generate-an-upload-keystore)
4. [Step 2 — Add GitHub Secrets](#step-2--add-github-secrets)
5. [Step 3 — Build the AAB and download the artifact](#step-3--build-the-aab)
6. [Play App Signing](#play-app-signing)
7. [Local Development](#local-development)

---

## About

ChessRun is a single-file browser chess game (`index.html`) wrapped in an Android WebView app for distribution on Google Play. The game works completely offline — all assets (JavaScript, CSS, audio, and the Boogaloo font) are bundled inside the app.

---

## Android Build Quickstart

### Step 1 — Generate an Upload Keystore

You only need to do this once. No local computer or Android Studio required.

1. Go to the **Actions** tab of this repository.
2. Select the **"Generate Upload Keystore"** workflow on the left.
3. Click **"Run workflow"** → **"Run workflow"** (green button).
4. Wait ~30 seconds for it to finish.
5. Click on the completed run, then scroll to **Artifacts** and download **`chessrun-keystore-files`**.
6. Unzip the downloaded file. You will find:
   - `chessrun-upload-key.jks` — keep this file safe in a password manager
   - `chessrun-upload-key.b64.txt` — the base64-encoded keystore
   - `keystore-credentials.txt` — all passwords and instructions

> ⚠️ The artifact is only kept for **1 day**. Download it immediately after the workflow finishes.

---

### Step 2 — Add GitHub Secrets

1. Open `keystore-credentials.txt` from the downloaded artifact.
2. Go to your repository → **Settings** → **Secrets and variables** → **Actions**.
3. Click **"New repository secret"** and add all four secrets:

| Secret name                 | Value                                          |
|-----------------------------|------------------------------------------------|
| `ANDROID_KEYSTORE_BASE64`   | Full contents of `chessrun-upload-key.b64.txt` |
| `ANDROID_KEYSTORE_PASSWORD` | From `keystore-credentials.txt`                |
| `ANDROID_KEY_ALIAS`         | From `keystore-credentials.txt`                |
| `ANDROID_KEY_PASSWORD`      | From `keystore-credentials.txt`                |

---

### Step 3 — Build the AAB

Builds run automatically on every push to `main`/`master`. To trigger manually:

1. Go to **Actions** → **"Build Android App Bundle"**.
2. Click **"Run workflow"** → **"Run workflow"**.
3. Wait ~5 minutes for the build to complete.
4. Click on the completed run, scroll to **Artifacts**, and download **`chessrun-release-signed`**.

The artifact contains `app-release.aab` — the signed Android App Bundle ready for Google Play upload.

If signing secrets are **not** set, the workflow still produces an unsigned AAB artifact named `chessrun-release-unsigned`.

---

## Play App Signing

When uploading to Google Play for the first time:

1. In the Google Play Console, go to your app → **Setup** → **App integrity** → **App signing**.
2. Enable **Play App Signing**.
3. Upload `chessrun-upload-key.jks` as the **upload key** when prompted.

Google will use their own app signing key to sign the final APK delivered to users. Your upload key is only used to authenticate uploads — meaning if you ever lose it, you can request a new upload key through the Play Console without losing your app.

---

## Local Development

### Web game (browser)
Simply open `index.html` in any modern browser. No server needed.

### Android (requires Android Studio or command-line tools)

```bash
# Build unsigned release AAB
./gradlew bundleRelease

# Build debug APK
./gradlew assembleDebug
```

Output: `app/build/outputs/bundle/release/app-release.aab`
