package spa.lyh.cn.httputils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;

public class MainActivity extends AppCompatActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

        RequestCenter.getNewVersion(this, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                String msg = (String) responseObj;
                text.setText(msg);
            }

            @Override
            public void onFailure(Object reasonObj) {
                OkHttpException exception = (OkHttpException) reasonObj;
                text.setText(exception.getEmsg());
            }
        });
    }
}
