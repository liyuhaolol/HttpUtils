package spa.lyh.cn.httputils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.httputils.model.JsonFromServer;
import spa.lyh.cn.httputils.model.UpdateInfo;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeJsonListener;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeStringListener;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.HeaderParams;

public class MainActivity extends AppCompatActivity {
    TextView text,progress_tv;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        progress_tv = findViewById(R.id.progress);

        RequestCenter.getNewVersion(this, new DisposeJsonListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData, @NonNull JSONObject jsonObject) {
                Toast.makeText(MainActivity.this,headerData.get("Content-Type"), Toast.LENGTH_SHORT).show();
                //TypeReference typeReference = new TypeReference<JsonFromServer<UpdateInfo>>(){};
                JsonFromServer<UpdateInfo> jsonF = jsonObject.to(new TypeReference<JsonFromServer<UpdateInfo>>(){});
                text.setText(jsonF.toString());

            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
                text.setText(error.getEmsg());
            }
        });

        RequestCenter.logNewVersion(this, new DisposeStringListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData, @NonNull String stringBody) {
                Log.e("qwer","打印结果："+stringBody);
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
                Log.e("qwer",error.getEmsg());
            }
        });

        /*downloadFile(this,
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
                });*/
        ///////请求池
        /*MultiCall call1 = new MultiCall(RequestCenter.createVersionRequest(this), null, true, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成1");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败1");
                return true;
            }
        });
        MultiCall call2 = new MultiCall(RequestCenter.createVersionRequest(this), new TypeReference<JsonFromServer<UpdateInfo>>() {
        }, true, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成2");
                JsonFromServer<UpdateInfo> jsonF = (JsonFromServer<UpdateInfo>) bodyData;
                Log.e("qwer","code:"+jsonF.code);
                Log.e("qwer","info:"+jsonF.data.getVersionInfo());
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败2");
                return true;
            }
        });
        MultiCall call3 = new MultiCall(RequestCenter.createVersionRequest(this), null, false, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成3");
                msg = (String) bodyData;
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败3");
                return true;
            }
        });
        MultiCall call4 = new MultiCall(RequestCenter.createVersionRequest(this), null, true, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成4");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败4");
                return true;
            }
        });
        MultiCall call5 = new MultiCall(RequestCenter.createVersionRequest(this), null, false, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成5");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败5");
                return true;
            }
        });
        MultiCall call6 = new MultiCall(RequestCenter.createVersionRequest(this), null, false, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成6");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败6");
                return true;
            }
        });
        MultiCall call7 = new MultiCall(RequestCenter.createVersionRequest(this), null, true, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成7");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败7");
                return true;
            }
        });
        MultiCall call8 = new MultiCall(RequestCenter.createVersionRequest(this), null, true, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成8");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败8");
                return true;
            }
        });
        MultiCall call9 = new MultiCall(RequestCenter.createVersionRequest(this), null, false, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成9");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败9");
                return true;
            }
        });
        MultiCall call10 = new MultiCall(RequestCenter.createVersionRequest(this), null, false, new DisposeMultiDataListener() {
            @Override
            public boolean onSuccess(Headers headerData, Object bodyData) {
                Log.e("qwer","完成10");
                return true;
            }

            @Override
            public boolean onFailure(Object error) {
                Log.e("qwer","失败10");
                return true;
            }
        });

        List<MultiCall> listCall = new ArrayList<>();
        listCall.add(call1);
        listCall.add(call2);
        listCall.add(call3);
        listCall.add(call4);
        listCall.add(call5);
        List<MultiCall> listCall2 = new ArrayList<>();
        listCall2.add(call6);
        listCall2.add(call7);
        listCall2.add(call8);
        listCall2.add(call9);
        listCall2.add(call10);

        MultiRequestCenter
                .get(this)
                .setDevMode(BuildConfig.DEBUG)
                .addRequests(listCall)
                .startTasks(new RequestResultListener() {
                    @Override
                    public void onFinish() {
                        Log.e("qwer","请求池任务结束1");
                        text.setText(msg);
                    }

                    @Override
                    public void onTermination() {
                        Log.e("qwer","请求池任务被某个任务禁止完成1");
                    }

                    @Override
                    public void onCancel() {
                        Log.e("qwer","取消请求池1");
                    }
                });

        MultiRequestCenter
                .get(this)
                .setDevMode(BuildConfig.DEBUG)
                .addRequests(listCall2)
                .startTasks(new RequestResultListener() {
                    @Override
                    public void onFinish() {
                        Log.e("qwer","请求池任务结束2");
                    }

                    @Override
                    public void onTermination() {
                        Log.e("qwer","请求池任务被某个任务禁止完成2");
                    }

                    @Override
                    public void onCancel() {
                        Log.e("qwer","取消请求池2");
                    }
                });*/

/*        RequestCenter.headRequest(this, "https://m.newsduan.com/web/resource/img/logo.png?version=20220310", null, new DisposeHeadListener() {
            @Override
            public void onSuccess(Headers headerData) {
                Log.e("qwer",headerData.get("Content-Type"));
            }

            @Override
            public void onFailure(Object error) {

            }
        });*/
    }
}
