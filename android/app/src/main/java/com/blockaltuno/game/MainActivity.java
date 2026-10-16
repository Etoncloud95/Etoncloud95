package com.blockaltuno.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    // Fake origin for localStorage only; the page is loaded from the APK and the app has no network access.
    private static final String BASE_URL = "https://blockaltuno.local/";
    private static final String HEAD = "<!doctype html><html lang=\"pt-BR\"><head><meta charset=\"utf-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,viewport-fit=cover\">"
            + "<style>:root{padding-top:env(safe-area-inset-top,0px);padding-bottom:env(safe-area-inset-bottom,0px);box-sizing:border-box}"
            + "body{margin:0;font-size:14px}[hidden]{display:none!important}</style></head><body>";

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setBlockNetworkLoads(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        webView.setBackgroundColor(0xFF241912);
        webView.addJavascriptInterface(new NativeBridge(), "BAUNative");
        setContentView(webView);
        webView.loadDataWithBaseURL(BASE_URL, HEAD + readAsset("index.html") + "</body></html>",
                "text/html", "utf-8", null);
    }

    private String readAsset(String name) {
        try (InputStream in = getAssets().open(name)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return "<p>Erro ao carregar o jogo.</p>";
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        webView.evaluateJavascript("window.blockAltUnoBack ? window.blockAltUnoBack() : false", value -> {
            if (!"true".equals(value)) finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.evaluateJavascript("window.bauPause && window.bauPause()", null);
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.evaluateJavascript("window.bauResume && window.bauResume()", null);
    }

    /** Lets the game trigger short vibrations on line clears. */
    private class NativeBridge {
        private final Vibrator vibrator;

        NativeBridge() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vm = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vm != null ? vm.getDefaultVibrator() : null;
            } else {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
        }

        @JavascriptInterface
        public void vibrate(long ms) {
            if (vibrator == null || !vibrator.hasVibrator()) return;
            vibrator.vibrate(VibrationEffect.createOneShot(Math.max(5, Math.min(ms, 300)), VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}
