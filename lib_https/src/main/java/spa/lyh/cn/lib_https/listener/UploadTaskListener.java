package spa.lyh.cn.lib_https.listener;

import org.jetbrains.annotations.NotNull;

import spa.lyh.cn.lib_https.model.Progress;

public interface UploadTaskListener {
    void onSuccess(String url);
    void onFailure(int status,@NotNull String msg);
    void onProgress(@NotNull Progress progress);
}
