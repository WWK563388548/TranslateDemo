package com.example.wwk.translatedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = "Translate";
    public static final String KEY_FROM = "aaa123ddd";
    public static final String KEY_OF_YOUDAO = "336378893";

    private TextView mDisplayResult;
    private EditText mInputWords;
    private Button mQueryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
    }

    private void initializeView() {

        mDisplayResult = (TextView) findViewById(R.id.Query_result_text_view);
        mInputWords = (EditText) findViewById(R.id.input_word_edit);
        mQueryButton = (Button) findViewById(R.id.query_button);
        mQueryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.query_button:

                String inputString = mInputWords.getText().toString();
                if (inputString == null) {
                    Toast.makeText(MainActivity.this, "输入框不能为空", Toast.LENGTH_LONG).show();
                }
                String url = "http://fanyi.youdao.com/openapi.do?keyfrom="
                        + KEY_FROM + "&key=" + KEY_OF_YOUDAO
                        + "&type=data&doctype=json&version=1.1&q="
                        + inputString;

                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.d(LOG_TAG, "请求到的数据: " + t);
                        parseJson(t);
                    }
                });
                break;
        }
    }

    // Parse and display data
    private void parseJson(String t) {

        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject object = jsonObject.getJSONObject("basic");

            String result = "美式发音" + object.getString("us-phonetic") + "\n"
                    + "英式发音" + object.getString("uk-phonetic") + "\n"
                    + "\n"
                    + "释义：" + "\n"
                    + "\n"
                    + object.getString("explains") + "\n"
                    + "\n"
                    + "网络释义:" + "\n";

            JSONArray jsonArray = jsonObject.getJSONArray("web");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                result = result + jsonObject3.getString("value") + "\n";
                result = result + jsonObject3.getString("key") + "\n";
            }

            mDisplayResult.setText(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
