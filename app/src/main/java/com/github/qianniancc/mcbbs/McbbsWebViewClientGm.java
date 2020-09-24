package com.github.qianniancc.mcbbs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.Toast;

import at.pardus.android.webview.gm.run.WebViewClientGm;
import at.pardus.android.webview.gm.store.ScriptStoreSQLite;


public class McbbsWebViewClientGm extends WebViewClientGm {
    private McbbsBrowser mcbbsBrowser;
    private Handler myHandler;
    Context context;
    private SharedPreferences prefs;



    public McbbsWebViewClientGm(ScriptStoreSQLite scriptStore,
                                String jsBridgeName, String secret, McbbsBrowser mcbbsBrowser, Handler myHandler, Context context) {
        super(scriptStore, jsBridgeName, secret);
        this.mcbbsBrowser = mcbbsBrowser;
        this.myHandler=myHandler;

        this.context=context;

        prefs = PreferenceManager.getDefaultSharedPreferences(this.context);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final String url) {
        return mcbbsBrowser.checkDownload(url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (prefs.getBoolean("antiImg", false)) {
            myHandler.sendEmptyMessage(1);
        } else {
            myHandler.sendEmptyMessage(2);
        }
        mcbbsBrowser.addressField.setText(url);
        mcbbsBrowser.checkDownload(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        Toast.makeText(
                mcbbsBrowser.activity,
                mcbbsBrowser.activity
                        .getString(at.pardus.android.webview.gm.store.ui.R.string.error_while_loading)
                        + " "
                        + failingUrl + ": " + errorCode + " " + description,
                Toast.LENGTH_LONG).show();
    }
}
