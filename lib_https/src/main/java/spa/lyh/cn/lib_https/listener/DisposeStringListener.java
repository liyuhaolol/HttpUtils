package spa.lyh.cn.lib_https.listener;


import okhttp3.Headers;
import spa.lyh.cn.lib_https.exception.OkHttpException;

public interface DisposeStringListener {
    /**
     * 请求成功回调事件处理
     */
    void onSuccess(Headers headerData, String stringBody);

    /**
     * 请求失败回调事件处理
     */
    void onFailure(OkHttpException error);
}
