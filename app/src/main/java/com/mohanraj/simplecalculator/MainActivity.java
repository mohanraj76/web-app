package com.mohanraj.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText inputurl;
    ImageView clearurl;
    WebView webView;
    ProgressBar progressBar;
    ImageView webback , webforward , webrefresh , webshare;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputurl = findViewById(R.id.url_input);
        clearurl = findViewById(R.id.cancel_icon);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);
        webback = findViewById(R.id.web_back);
        webforward = findViewById(R.id.web_forward);
        webrefresh = findViewById(R.id.web_refresh);
        webshare = findViewById(R.id.web_share);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        clearurl.setOnClickListener(view -> {
            inputurl.setText("");
        });

        webback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()){
                    webView.goBack();
                }
            }
        });

        webforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()){
                    webView.goForward();
                }
            }
        });

        webrefresh.setOnClickListener(view -> webView.reload());

        webshare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,webView.getUrl());
            intent.setType("text/plain");
            startActivity(intent);
        });





        loadmyurl("google.com");

        inputurl.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputurl.getWindowToken(),0);
                loadmyurl(inputurl.getText().toString());
                return true;
            }
            return false;
        });


    }


    void loadmyurl(String url){
        boolean matchurl = Patterns.WEB_URL.matcher(url).matches();
        if(matchurl){
            webView.loadUrl(url);
        }else {
            webView.loadUrl("google.com/search?q="+url);
        }
    }


    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }

    }

    class MywebviewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            inputurl.setText(webView.getUrl());
            progressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }



}