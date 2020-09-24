package com.github.qianniancc.mcbbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import at.pardus.android.webview.gm.model.Script;
import at.pardus.android.webview.gm.run.WebViewGm;
import at.pardus.android.webview.gm.store.ScriptStoreSQLite;
import at.pardus.android.webview.gm.store.ui.ScriptBrowser;
import at.pardus.android.webview.gm.util.DownloadHelper;

public class McbbsBrowser {

    private static final String TAG = ScriptBrowser.class.getName();

    protected MainActivity activity;

    protected ScriptStoreSQLite scriptStore;

    private String startUrl;

    protected View browser;

    protected WebViewGm webView;

    protected EditText addressField;

    @SuppressLint("InflateParams")
    private void init() {
        browser = activity.getLayoutInflater().inflate(
                at.pardus.android.webview.gm.store.ui.R.layout.script_browser, null);
        webView = (WebViewGm) browser.findViewById(at.pardus.android.webview.gm.store.ui.R.id.webView);
        webView.setScriptStore(scriptStore);
        addressField = (EditText) browser.findViewById(at.pardus.android.webview.gm.store.ui.R.id.addressField);
        addressField
                .setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO
                                || actionId == EditorInfo.IME_NULL) {
                            webView.loadUrl(v.getText().toString());
                            webView.requestFocus();
                            ((InputMethodManager) activity
                                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(
                                            v.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                });
    }


    protected boolean checkDownload(final String url) {
        if (url.endsWith(".user.js")) {
            new Thread() {
                public void run() {
                    installScript(url);
                }
            }.start();
            return true;
        }
        return false;
    }



    public McbbsBrowser(MainActivity activity,
                        ScriptStoreSQLite scriptStore, String startUrl) {
        this.activity = activity;
        this.scriptStore = scriptStore;
        this.startUrl = startUrl;
        init();
    }

    protected void installScript(String url) {
        makeToastOnUiThread(activity.getString(at.pardus.android.webview.gm.store.ui.R.string.starting_download_of)
                + " " + url, Toast.LENGTH_SHORT);
        String scriptStr = DownloadHelper.downloadScript(url);


        if (scriptStr == null) {
            makeToastOnUiThread(
                    activity.getString(at.pardus.android.webview.gm.store.ui.R.string.error_downloading_from) + " "
                            + url, Toast.LENGTH_LONG);
            return;
        }

        Script script = Script.parse(scriptStr, url);

        if (script == null) {
            Log.d(TAG, "Error parsing script:\n" + scriptStr);
            makeToastOnUiThread(activity.getString(at.pardus.android.webview.gm.store.ui.R.string.error_parsing_at)
                    + " " + url, Toast.LENGTH_LONG);
            return;
        }


        scriptStore.add(script);
        makeToastOnUiThread(activity.getString(at.pardus.android.webview.gm.store.ui.R.string.added_new_script) + " " + script, Toast.LENGTH_LONG);
    }

    protected void installScriptBG(String url) {
        String scriptStr = DownloadHelper.downloadScript(url);
        if (scriptStr == null) {
            return;
        }
        Script script = Script.parse(scriptStr, url);
        if (script == null) {
            Log.d(TAG, "Error parsing script:\n" + scriptStr);
            return;
        }

        scriptStore.add(script);
    }

    private void makeToastOnUiThread(final String message, final int length) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, length).show();
            }
        });
    }
}
