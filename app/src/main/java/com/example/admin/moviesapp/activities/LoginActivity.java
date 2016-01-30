package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Trailer;

import java.util.List;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements UpdateListener {

    private WebView myWebView_;
    private RequestManager manager_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myWebView_ = (WebView) findViewById(R.id.webview);

        // Get instance of RequestManger
        manager_ = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager_.init(this);

        Intent intent = getIntent();
        String loginLink = intent.getStringExtra("link");
        openBrowser(loginLink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(this.getString(R.string.login_actionbar_title));
    }


    private void openBrowser(String link) {
        myWebView_.setVisibility(View.VISIBLE);

        myWebView_.loadUrl(link);
        myWebView_.getSettings().setJavaScriptEnabled(true);
        myWebView_.getSettings().setDomStorageEnabled(true);
        myWebView_.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Timber.v("onPageFinished");
                String fullUrl = view.getUrl();
//mskiyatwork - The Movie Database (TMDb)
                //https://www.themoviedb.org/account/mskiyatwork
                if (fullUrl.toLowerCase().contains("https://www.themoviedb.org/account/")){
                    if (!view.getTitle().equals("Sign Up - The Movie Database (TMDb)") && !view.getTitle().equals("Зарегистрироваться - The Movie Database (TMDb)")) {
                        if (view.getTitle().equals("Login - The Movie Database (TMDb)")){

                        } else {
                            manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
                            finish();
                        }
                    }
                }
//                if (!view.getTitle().equals("Login - The Movie Database (TMDb)") && (!view.getTitle().equals("Authenticate MovieApp? — The Movie Database (TMDb)"))){
//
//                    String title = view.getTitle();
//                    String a ="Authentication Granted — The Movie Database (TMDb)";
//                    manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
//                    finish();
//                }
                if (view.getTitle().equals("Authentication Granted — The Movie Database (TMDb)")) {
                    myWebView_.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "You have logged in", Toast.LENGTH_SHORT).show();
                    manager_.sendMessage(manager_.obtainMessage(States.SESSION_ID_REQUEST));
                    finish();
                }
            }

        });


    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {

    }

    @Override
    public void UpdateTrailers(List<Trailer> trailersList) {

    }

    @Override
    public void UpdateCasts(List<? extends CommonMovie> casts) {

    }

    @Override
    public void onErrorRaised(String errorMsg) {

    }

}
