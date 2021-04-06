package spa.lyh.cn.httputils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.RequestParams;

public class MainActivity extends AppCompatActivity {
    TextView text,progress_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        progress_tv = findViewById(R.id.progress);

        RequestCenter.getNewVersion(this, new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object responseObj) {
                Toast.makeText(MainActivity.this,headerData.get("Content-Type"), Toast.LENGTH_SHORT).show();
                String msg = (String) responseObj;
                text.setText(msg);
            }

            @Override
            public void onFailure(Object reasonObj) {
                OkHttpException exception = (OkHttpException) reasonObj;
                text.setText(exception.getEmsg());
            }
        });

        downloadFile(this,
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3208238474,2536510412&fm=26&gp=0.jpg",
                getExternalCacheDir().getPath(),
                HttpClient.OVERWRITE_FIRST,
                new DisposeDownloadListener() {
                    @Override
                    public void onSuccess(String filePath, String fileName) {
                        progress_tv.setText("下载成功");
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        progress_tv.setText("下载成功");
                    }

                    @Override
                    public void onProgress(boolean haveFileSize, int progress, String currentSize, String sumSize) {
                        if (haveFileSize){
                            progress_tv.setText("已下载："+progress+"%");
                        }else {
                            progress_tv.setText("已下载："+currentSize);
                        }
                    }
                });

        RequestCenter.headRequest(this,
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3208238474,2536510412&fm=26&gp=0.jpg",
                null,
                new DisposeHeadListener() {
                    @Override
                    public void onSuccess(Headers headerData) {
                    }

                    @Override
                    public void onFailure(Object error) {
                    }
                });
    }

    public static Call downloadFile(Context context, String url, String path, int mod, DisposeDownloadListener listener) {
        RequestParams params = new RequestParams();
        return HttpClient.getInstance(context).downloadFile(context,
                CommonRequest.createDownloadRequest(url, null, params, true),
                new DisposeDataHandle(listener, path, true),mod);
    }
}
