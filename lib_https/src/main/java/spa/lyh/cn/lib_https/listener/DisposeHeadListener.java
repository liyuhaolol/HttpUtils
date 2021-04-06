package spa.lyh.cn.lib_https.listener;

import okhttp3.Headers;

public interface DisposeHeadListener {
    /**
     * 请求成功回调事件处理
     */
    void onSuccess(Headers headerData);

    /**
     * 请求失败回调事件处理
     */
    void onFailure(Object error);
}
