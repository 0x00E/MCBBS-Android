package com.github.qianniancc.mcbbs;

import android.webkit.DownloadListener;

public class McbbsDownloadListener implements DownloadListener {
    /**
     * Constructor.
     *
     * @param mcbbsBrowser reference to its enclosing ScriptBrowser
     */
    private McbbsBrowser mcbbsBrowser;

    public McbbsDownloadListener(McbbsBrowser mcbbsBrowser) {
        this.mcbbsBrowser = mcbbsBrowser;
    }

    @Override
    public void onDownloadStart(final String url, String userAgent,
                                String contentDisposition, String mimetype, long contentLength) {
        mcbbsBrowser.checkDownload(url);
    }
}
