package spa.lyh.cn.lib_https.response;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;

public class CommonDataCallback extends CommonBase implements Callback {
    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private boolean devMode;
    private long requestTime;

    public CommonDataCallback(DisposeDataHandle handle) {
        requestTime = System.currentTimeMillis();
        this.mListener = handle.dataListener;
        this.devMode = handle.devMode;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(e != null){
            e.printStackTrace();
            /**
             * 此时还在非UI线程，因此要转发
             */
            mDeliveryHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (e.getMessage() != null && mListener != null){
                        if (e.getMessage().equals("Canceled")){
                            mListener.onFailure(new OkHttpException(OkHttpException.CANCEL_REQUEST, CANCEL_MSG,null));
                        }else {
                            mListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, NET_MSG,null));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(response.request().url().toString(),response.headers(),result);
            }
        });
    }

    private void handleResponse(String url, Headers headerData, Object bodyData) {
        if (bodyData == null || bodyData.toString().trim().isEmpty()) {
            if (mListener != null){
                mListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, EMPTY_MSG,null));//网络错误
            }
            return;
        }
        //是否在控制台打印信息
        if (devMode){
            long finishTime = System.currentTimeMillis();
            long time = finishTime - requestTime;
            LyhLog.e(TAG, LogUtils.makeResponseLog(url,bodyData.toString(),time+"ms"));
        }
        if (mListener != null){
            mListener.onSuccess(headerData,bodyData.toString());
        }
    }
}
