# ChessRun

A single-file chess endless-runner web game, wrapped in an Android WebView app for offline-first Play Store distribution.

## Playing in a browser

Open `index.html` directly in any modern browser. The Boogaloo font is bundled locally in `fonts/Boogaloo-Regular.woff2` — no internet connection required.

## Android App Bundle (.aab)

### Prerequisites

No local Android SDK needed. All builds run in GitHub Actions.

### 1 · Generate an upload keystore (one-time)

1. Go to **Actions → Generate Upload Keystore** and click **Run workflow**.
2. When the run completes, download the **`chessrun-keystore-files`** artifact (valid for 1 day).
3. Add the four GitHub repository secrets listed in `keystore-credentials.txt`:

   | Secret name                  | Value                                     |
   | ---------------------------- | ----------------------------------------- |
   | `ANDROID_KEYSTORE_BASE64`    | Full contents of `chessrun-upload-key.b64.txt` |
   | `ANDROID_KEYSTORE_PASSWORD`  | As printed in `keystore-credentials.txt`  |
   | `ANDROID_KEY_ALIAS`          | `chessrun-upload-key`                     |
   | `ANDROID_KEY_PASSWORD`       | As printed in `keystore-credentials.txt`  |

   Path: **Settings → Secrets and variables → Actions → New repository secret**

4. Store `chessrun-upload-key.jks` somewhere safe (password manager). You will need it to reset your upload key in Google Play if you ever lose these secrets.

### 2 · Build the .aab

Push to `main`/`master` or run **Actions → Build Android App Bundle** manually.

- If the four signing secrets are present, the workflow produces a **signed** AAB.
- Otherwise it produces an **unsigned** AAB (useful for testing).

Download the artifact from the completed workflow run.

### 3 · Play App Signing

Enable **Play App Signing** in the Google Play Console before uploading:

> App → Setup → App integrity → App signing

Upload the `.aab` as your **upload key**. Google will re-sign it with their app-signing key before distribution, meaning you can always rotate your upload key through the Play Console if needed.

## Project structure

```
chessrun/
├── index.html                        # Web game (single file)
├── fonts/
│   └── Boogaloo-Regular.woff2        # Bundled font (no CDN dependency)
├── CS1.mp3                           # Background music
├── app/
│   └── src/main/
│       ├── assets/
│       │   ├── index.html            # Copy of web game for Android
│       │   ├── CS1.mp3
│       │   └── fonts/
│       │       └── Boogaloo-Regular.woff2
│       ├── java/com/chessrun/app/
│       │   └── MainActivity.kt       # WebView wrapper
│       └── res/…                     # Icons, layout, strings
└── .github/workflows/
    ├── build.yml                     # AAB build + upload artifact
    └── generate-keystore.yml         # Bootstrap signing keystore
```