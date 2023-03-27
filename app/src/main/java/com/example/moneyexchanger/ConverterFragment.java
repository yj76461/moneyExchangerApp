package com.example.moneyexchanger;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutionException;

public class ConverterFragment extends Fragment{
    private final String[] currencyList = {"AUD", "BGN", "BRL", "CAD", "CHF",
            "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HUF",
            "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN",
            "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB",
            "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};

    public TextView et_from;
    public TextView tv_to;
    private Button btn_convert;
    private ImageButton btn_change;
    private int swap1, swap2;
    private TextView tv_test;
    private CurrencyInfo currencyInfo;
    double currencyRate = 0.0;

    private SendEventListener sendEventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            sendEventListener = (SendEventListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SendEventListener!!!!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_converter, container, false);
        Context ct = container.getContext();
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
        Spinner spinner2 = view.findViewById(R.id.spinner2);

        et_from = view.findViewById(R.id.et_from);
        tv_to = view.findViewById(R.id.tv_to);

        btn_convert = view.findViewById(R.id.btn_convert);
        btn_change = view.findViewById(R.id.btn_change);

        currencyInfo = new CurrencyInfo();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ct,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                currencyList);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyInfo.setFrom(currencyList[i]);
                swap1 = i;

                sendEventListener.sendMessage(currencyList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyInfo.setTo(currencyList[i]);
                swap2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_from.getText().toString().equals("")){
                    Toast.makeText(ct, "Input your amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        // doInBackground 의 인자로 들어갈 스트링 값 두개를 execute 에 넣어준다. 인자 개수의 제한은 없는 것 같다.
                        // get() 은 태스크가 끝날 떄까지 기다리는 것. 즉 비동기를 동기식으로 처리하게 만든다.
                        currencyRate = new Task().execute(currencyInfo.getFrom(), currencyInfo.getTo()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    currencyInfo.setAmount(et_from.getText().toString());
                    double input = Double.parseDouble(currencyInfo.getAmount());
                    double result = Math.round(input * currencyRate * 100.0) / 100.0; // 소수점 두자리에서 자르기 위함
                    float fresult = (float)result;
                    Log.e("received input ", " "+input);
                    Log.e("received result ", " "+result);

                    ValueAnimator animator = ValueAnimator.ofFloat(0, fresult);
                    // 값이 빠르게 증가하는 보간법
                    // (float)숫자 올라가는 애니메이션
                    animator.setInterpolator(new AccelerateInterpolator());

                    animator.addUpdateListener(valueAnimator -> {
                        //getAnimatedValue를 통해 애니메이션의 현재 value를 가져올 수 있다
                        float currentValue = (float)valueAnimator.getAnimatedValue();
                        tv_to.setText(String.valueOf(currentValue));
                    });

                    animator.setDuration(300);
                    animator.start();

                    //tv_to.setText(Double.toString(result));

                }

            }
        });

        // 개발하다 말은 부분 --> swap function 개발 완료
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setSelection(swap2);
                spinner2.setSelection(swap1); // 인덱스로 아이템 선택
                et_from.setText("");
                tv_to.setText("0");
            }
        });

        return view;
    }
}
