package com.appsbyrick.chessrun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        // Allow the file:// origin to read other file:// sub-resources (fonts, audio).
        // Safe here because the WebView only ever loads local app assets.
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        // Allow audio to play without requiring a prior user gesture (needed on some
        // WebView versions for background music loaded from local assets).
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Forward WebView console messages to Logcat for easier debugging.
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("ChessRun.WebView",
                        consoleMessage.sourceId() + ":" + consoleMessage.lineNumber()
                        + " " + consoleMessage.message());
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                // Keep local file:// navigation inside the WebView
                if (url.startsWith("file://")) {
                    return false;
                }
                // Open all external links (e.g. privacy policy) in the system browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/index.html");

        // Handle back navigation: go back in WebView history, or finish the activity
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }
}

