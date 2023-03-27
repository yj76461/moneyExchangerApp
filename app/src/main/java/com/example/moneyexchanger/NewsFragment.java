package com.example.moneyexchanger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsFragment extends Fragment {
    private WebView wv_news = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        String curPick = this.getArguments().getString("curPick");
        Toast.makeText(getActivity(), "curPick is " + curPick, Toast.LENGTH_SHORT).show();
        wv_news = view.findViewById(R.id.wv_news);
        wv_news.getSettings().setJavaScriptEnabled(true);
        wv_news.setWebViewClient(new WebViewClient());

        wv_news.setWebChromeClient(new WebChromeClient());
        wv_news.loadUrl("https://www.cnbc.com/search/?query=" + curPick + "&qsearchterm=");


        return view;
    }
}
