package spa.lyh.cn.lib_https.listener;

import org.jetbrains.annotations.NotNull;

import okhttp3.Headers;
import spa.lyh.cn.lib_https.exception.OkHttpException;

public interface DisposeHeadListener {
    /**
     * 请求成功回调事件处理
     */
    void onSuccess(@NotNull Headers headerData);

    /**
     * 请求失败回调事件处理
     */
    void onFailure(@NotNull OkHttpException error);
}
