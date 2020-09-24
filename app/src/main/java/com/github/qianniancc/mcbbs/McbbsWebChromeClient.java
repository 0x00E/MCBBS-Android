package com.github.qianniancc.mcbbs;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class McbbsWebChromeClient extends WebChromeClient {

    private McbbsBrowser mcbbsBrowser;

    /**
     * Constructor.
     *
     * @param mcbbsBrowser
     *            reference to its enclosing ScriptBrowser
     */
    public McbbsWebChromeClient(McbbsBrowser mcbbsBrowser) {
        this.mcbbsBrowser = mcbbsBrowser;
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        MainActivity.setTitle("加载中...");
        mcbbsBrowser.activity.setProgress(progress * 100);
        if(progress==100){
            MainActivity.setTitle(view.getTitle());
        }

    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
    }

}
