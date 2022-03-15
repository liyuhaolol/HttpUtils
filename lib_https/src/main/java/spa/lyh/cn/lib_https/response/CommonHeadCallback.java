package spa.lyh.cn.lib_https.response;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.log.LyhLog;
import spa.lyh.cn.lib_https.response.base.CommonBase;
import spa.lyh.cn.lib_https.utils.LogUtils;

public class CommonHeadCallback extends CommonBase implements Callback {

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeHeadListener mListener;
    private boolean devMode;
    private long requestTime;

    public CommonHeadCallback(DisposeDataHandle handle) {
        requestTime = System.currentTimeMillis();
        this.mListener = handle.headListener;
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
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleHeaders(response.request().url().toString(),response.headers());
            }
        });
    }

/*    private ArrayList<String> handleCookie(Headers headers) {
        ArrayList<String> tempList = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
                tempList.add(headers.value(i));
            }
        }
        return tempList;
    }*/

    private void handleHeaders(String url,Headers headerData) {
        if (headerData == null || headerData.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(OkHttpException.NETWORK_ERROR, EMPTY_MSG));//网络错误
            return;
        }
        //是否在控制台打印信息
        if (devMode){
            long finishTime = System.currentTimeMillis();
            long time = finishTime - requestTime;
            LyhLog.e(TAG, LogUtils.makeHeaderLog(url, headerData.toString(),time+"ms"));
        }

        mListener.onSuccess(headerData);
    }
}
