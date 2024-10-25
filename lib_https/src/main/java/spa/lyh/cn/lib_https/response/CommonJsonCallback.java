package spa.lyh.cn.lib_https.response;

import android.os.Handler;
import android.os.Looper;


import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeJsonListener;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;

/**
 * @author liyuhao
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback extends CommonBase implements Callback {




    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeJsonListener mListener;
    private boolean devMode;
    private long requestTime;

    public CommonJsonCallback(DisposeDataHandle handle) {
        requestTime = System.currentTimeMillis();
        this.mListener = handle.mListener;
        this.devMode = handle.devMode;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        if(ioexception != null){
            ioexception.printStackTrace();
            /**
             * 此时还在非UI线程，因此要转发
             */
            mDeliveryHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (ioexception.getMessage() != null){
                        if (ioexception.getMessage().equals("Canceled")){
                            mListener.onFailure(new OkHttpException(OkHttpException.CANCEL_REQUEST, CANCEL_MSG));
                        }else {
                            mListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, NET_MSG));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(response.request().url().toString(),response.headers(),result);
            }
        });
    }

    private void handleResponse(String url,Headers headerData,Object bodyData) {
        if (bodyData == null || bodyData.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, EMPTY_MSG));//网络错误
            return;
        }
        //是否在控制台打印信息
        if (devMode){
            long finishTime = System.currentTimeMillis();
            long time = finishTime - requestTime;
            LyhLog.e(TAG, LogUtils.makeResponseLog(url,bodyData.toString(),time+"ms"));
        }

        try {
            //按照范型解析
            JSONObject realResult = JSONObject.parseObject(bodyData.toString());

            if (realResult != null){
                mListener.onSuccess(headerData,realResult);
            }else {
                mListener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR, EMPTY_MSG));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR,JSON_CONVERT_ERROR));//json解析错误
        }
    }
}