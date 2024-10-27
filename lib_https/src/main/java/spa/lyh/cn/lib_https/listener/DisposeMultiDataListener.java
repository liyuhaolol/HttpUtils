package spa.lyh.cn.lib_https.listener;

import okhttp3.Headers;
import spa.lyh.cn.lib_https.exception.OkHttpException;

public interface DisposeMultiDataListener {

    /**
     * 请求成功回调事件处理,返回true请求池才会正常响应任务结束方法
     */
    boolean onSuccess(Headers headerData, Object bodyData);

    /**
     * 请求失败回调事件处理,返回true请求池才会正常响应任务结束方法
     */
    boolean onFailure(OkHttpException error);
}
