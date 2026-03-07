# chessrun

A web-based chess running game wrapped in a native Android App Bundle (.aab) for Google Play distribution.

---

## Android Wrapper

### How It Works

The Android app is a **WebView wrapper** that loads the game entirely from local app assets — no internet connection is required at runtime.

- The game (`index.html`) and all assets (`CS1.mp3`, `fonts/Boogaloo.woff2`) are bundled inside the APK/AAB at build time under `android/app/src/main/assets/`.
- On launch, `MainActivity` opens a `WebView` and loads `file:///android_asset/index.html`.
- JavaScript and DOM storage are enabled so the game works as expected.
- External links (e.g. privacy policy) open in the system browser via `ACTION_VIEW`; they do not navigate inside the WebView.
- The app is **portrait-only** (`screenOrientation="portrait"` in `AndroidManifest.xml`).
- App ID: `com.appsbyrick.chessrun` | App name: **Chess Run** | Min SDK: 26 (Android 8.0+).

### Offline Font

The Google Fonts CDN reference has been removed from the in-app copy of `index.html`. The Boogaloo font is loaded from `fonts/Boogaloo.woff2` via a local `@font-face` declaration, so the game renders correctly with no network access.

---

## Building the Android App Bundle

### Prerequisites

- **Java 17** (or later)
- **Gradle 8.4** (or later) – install from [gradle.org](https://gradle.org/install/) or use the GitHub Actions workflow below
- **Android SDK** with platform `android-34` and build-tools `34.0.0`

### Local Build (unsigned)

```bash
cd android
gradle :app:bundleRelease
# Output: android/app/build/outputs/bundle/release/app-release.aab
```

---

## GitHub Actions Workflows

### Workflow A — Generate Upload Keystore (`generate-keystore.yml`)

This workflow creates a new JKS upload keystore with randomly generated passwords.

**Trigger:** Manual (`workflow_dispatch`)

**Steps:**
1. Go to **Actions → Generate Upload Keystore → Run workflow**.
2. Download the three artifacts:
   - `upload-keystore` – the `.jks` keystore file (store securely, e.g. a password manager)
   - `upload-keystore-base64` – Base64-encoded keystore for the GitHub secret
   - `upload-keystore-credentials` – alias, storePassword, keyPassword

### Workflow B — Build AAB (`build-aab.yml`)

Builds a release Android App Bundle.

**Trigger:** Manual (`workflow_dispatch`) **or** on push to `main`

**Behaviour:**
| Signing secrets present? | Output |
|---|---|
| ✅ Yes | Signed release `.aab` |
| ❌ No  | Unsigned release `.aab` (with a warning in the log) |

---

## Setting Up Signing Secrets

After running **Generate Upload Keystore**, add the following four secrets to your repository:

> **GitHub UI:** `Settings → Secrets and variables → Actions → New repository secret`

| Secret name | Value |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | Entire contents of `upload-keystore.base64.txt` |
| `ANDROID_KEYSTORE_PASSWORD` | Store password from `upload-keystore-credentials.txt` |
| `ANDROID_KEY_ALIAS` | `upload` |
| `ANDROID_KEY_PASSWORD` | Key password from `upload-keystore-credentials.txt` |

---

## Downloading the AAB Artifact

1. Go to **Actions → Build AAB**.
2. Click the latest successful run.
3. Under **Artifacts**, download `chess-run-release-aab`.
4. The `.aab` file is ready for upload to Google Play.

---

## Google Play App Signing (Recommended)

Google Play uses **Play App Signing**: you upload your bundle signed with the *upload key* (generated above) and Google re-signs it with a *separate app signing key* that they manage. This means:

- Your upload key (`upload-keystore.jks`) is only used for CI uploads.
- Even if the upload key is compromised, Google can replace it.
- The app signing key (managed by Google) is what end users actually receive.

Enable Play App Signing when creating your app in the Google Play Console.
