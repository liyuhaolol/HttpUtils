package spa.lyh.cn.lib_https.listener;

import com.alibaba.fastjson2.JSONObject;

import org.jetbrains.annotations.NotNull;

import okhttp3.Headers;
import spa.lyh.cn.lib_https.exception.OkHttpException;

public interface DisposeMultiJsonListener {

    /**
     * 请求成功回调事件处理,返回true请求池才会正常响应任务结束方法
     */
    boolean onSuccess(@NotNull Headers headerData,@NotNull JSONObject jsonObject);

    /**
     * 请求失败回调事件处理,返回true请求池才会正常响应任务结束方法
     */
    boolean onFailure(@NotNull OkHttpException error);
}
