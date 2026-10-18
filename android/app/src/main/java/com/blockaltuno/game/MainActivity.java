package com.blockaltuno.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.json.JSONObject;

public class MainActivity extends Activity {
    // Fake origin for localStorage only; the page is loaded from the APK and the app has no network access.
    private static final String BASE_URL = "https://blockaltuno.local/";
    private static final String HEAD = "<!doctype html><html lang=\"pt-BR\"><head><meta charset=\"utf-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,viewport-fit=cover\">"
            + "<style>:root{padding-top:env(safe-area-inset-top,0px);padding-bottom:env(safe-area-inset-bottom,0px);box-sizing:border-box}"
            + "body{margin:0;font-size:14px}[hidden]{display:none!important}</style></head><body>";

    private static final int REQ_EXPORT = 41, REQ_IMPORT = 42;
    private WebView webView;
    private String pendingExport;

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

    /** Save/restore of the progress file goes through the system file picker (no storage permission). */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = resultCode == RESULT_OK && data != null ? data.getData() : null;
        if (requestCode == REQ_EXPORT) {
            boolean ok = false;
            if (uri != null && pendingExport != null) {
                try (OutputStream out = getContentResolver().openOutputStream(uri, "wt")) {
                    if (out != null) { out.write(pendingExport.getBytes(StandardCharsets.UTF_8)); ok = true; }
                } catch (IOException ignored) {
                    // Reported to the page below.
                }
            }
            pendingExport = null;
            webView.evaluateJavascript("window.bauExported && window.bauExported(" + ok + ")", null);
        } else if (requestCode == REQ_IMPORT) {
            String text = null;
            if (uri != null) {
                try (InputStream in = getContentResolver().openInputStream(uri)) {
                    if (in != null) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[8192];
                        int n;
                        while ((n = in.read(buf)) > 0 && out.size() < 4_000_000) out.write(buf, 0, n);
                        text = out.toString(StandardCharsets.UTF_8.name());
                    }
                } catch (IOException ignored) {
                    // Reported to the page below.
                }
            }
            webView.evaluateJavascript("window.bauImport && window.bauImport(" + (text == null ? "null" : JSONObject.quote(text)) + ")", null);
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

    /** Lets the game vibrate and keep its progress in the app's own storage. */
    private class NativeBridge {
        private final Vibrator vibrator;
        private final SharedPreferences prefs = getSharedPreferences("bau", MODE_PRIVATE);

        @JavascriptInterface
        public void setItem(String key, String value) {
            prefs.edit().putString(key, value).apply();
        }

        @JavascriptInterface
        public void removeItem(String key) {
            prefs.edit().remove(key).apply();
        }

        @JavascriptInterface
        public String getAll() {
            JSONObject all = new JSONObject();
            try {
                for (Map.Entry<String, ?> e : prefs.getAll().entrySet()) {
                    if (e.getValue() instanceof String) all.put(e.getKey(), e.getValue());
                }
            } catch (Exception ignored) {
                // Return whatever was collected.
            }
            return all.toString();
        }

        NativeBridge() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vm = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vm != null ? vm.getDefaultVibrator() : null;
            } else {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
        }

        /** Writes every saved key at once (restoring a progress file), replacing what was there. */
        @JavascriptInterface
        public void replaceAll(String json) {
            try {
                JSONObject o = new JSONObject(json);
                SharedPreferences.Editor ed = prefs.edit().clear();
                java.util.Iterator<String> it = o.keys();
                while (it.hasNext()) { String k = it.next(); ed.putString(k, o.getString(k)); }
                ed.commit();
            } catch (Exception ignored) {
                // Leave the old progress untouched.
            }
        }

        /** Reloads the game after a restore so every screen reads the new progress. */
        @JavascriptInterface
        public void restart() {
            runOnUiThread(MainActivity.this::recreate);
        }

        @JavascriptInterface
        public void exportSave(String json, String fileName) {
            runOnUiThread(() -> {
                pendingExport = json;
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("application/json");
                i.putExtra(Intent.EXTRA_TITLE, fileName);
                try { startActivityForResult(i, REQ_EXPORT); } catch (Exception e) { pendingExport = null; webView.evaluateJavascript("window.bauExported && window.bauExported(false)", null); }
            });
        }

        @JavascriptInterface
        public void importSave() {
            runOnUiThread(() -> {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                try { startActivityForResult(i, REQ_IMPORT); } catch (Exception e) { webView.evaluateJavascript("window.bauImport && window.bauImport(null)", null); }
            });
        }

        /** Comma-separated off/on durations in ms, e.g. "0,90,40,180"; played at full strength. */
        @JavascriptInterface
        public void vibratePattern(String csv) {
            if (vibrator == null || !vibrator.hasVibrator() || csv == null) return;
            try {
                String[] parts = csv.split(",");
                long[] t = new long[parts.length + 1];
                int[] amp = new int[parts.length + 1];
                t[0] = 0; amp[0] = 0;
                for (int k = 0; k < parts.length; k++) {
                    t[k + 1] = Math.max(1, Math.min(Long.parseLong(parts[k].trim()), 600));
                    amp[k + 1] = k % 2 == 0 ? 255 : 0;
                }
                if (vibrator.hasAmplitudeControl()) vibrator.vibrate(VibrationEffect.createWaveform(t, amp, -1));
                else vibrator.vibrate(VibrationEffect.createWaveform(t, -1));
            } catch (Exception ignored) {
                // Bad pattern: skip.
            }
        }

        @JavascriptInterface
        public void vibrate(long ms) {
            if (vibrator == null || !vibrator.hasVibrator()) return;
            vibrator.vibrate(VibrationEffect.createOneShot(Math.max(5, Math.min(ms, 300)), VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}
