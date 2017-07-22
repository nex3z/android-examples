package com.nex3z.examples.webviewintercept;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etUrl = (EditText) findViewById(R.id.et_url);
        mWebView = (WebView) findViewById(R.id.webview);

        Button btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Map<String, String> header = new HashMap<>();
                // header.put("custom_header", "custom_header_on_load");
                // mWebView.loadUrl(etUrl.getText().toString(), header);
                mWebView.loadUrl(etUrl.getText().toString());
            }
        });

        initWebView();
    }

    private void initWebView() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {

            // Before API level 21
            @SuppressWarnings("deprecation") @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.v(LOG_TAG, "shouldInterceptRequest(): url = " + url);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && shouldInterceptUrl(url)) {
                    return newResponse(url);
                } else {
                    return super.shouldInterceptRequest(view, url);
                }
            }

            // Since API Level 21
            @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.v(LOG_TAG, "shouldInterceptRequest(): request.getUrl() = " + request.getUrl()
                        + ", request.getMethod() = " + request.getMethod());

                String url = request.getUrl().toString();
                if (shouldInterceptUrl(url) && request.getMethod().equals("GET")) {
                    return newResponse(request);
                } else {
                    return super.shouldInterceptRequest(view, request);
                }
            }

            private WebResourceResponse newResponse(String url) {
                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url.trim())
                        .addHeader("custom_header", "custom_header_on_intercept")
                        .build();

                try {
                    Response response = httpClient.newCall(request).execute();
                    return new WebResourceResponse(null,
                            response.header("content-encoding", "utf-8"),
                            response.body().byteStream()
                    );
                } catch (Exception e) {
                    return null;
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            private WebResourceResponse newResponse(WebResourceRequest originalRequest) {
                OkHttpClient httpClient = new OkHttpClient();

                Request.Builder builder = new Request.Builder()
                        .url(originalRequest.getUrl().toString());

                Map<String, String> headers = originalRequest.getRequestHeaders();
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                builder.addHeader("custom_header", "custom_header_on_intercept");
                Request request = builder.build();

                try {
                    Response response = httpClient.newCall(request).execute();
                    return new WebResourceResponse(null,
                            response.header("content-encoding", "utf-8"),
                            response.body().byteStream());
                } catch (Exception e) {
                    return null;
                }
            }

            private boolean shouldInterceptUrl(String url) {
                return url.contains("bing.com");
            }
        });
    }
}
