package spa.lyh.cn.lib_https.listener;

import org.jetbrains.annotations.NotNull;

import spa.lyh.cn.lib_https.exception.OkHttpException;

public interface DisposeMultiDownloadListener {
    boolean onSuccess(@NotNull String filePath, @NotNull  String fileName);

    boolean onFailure(@NotNull OkHttpException error);

    void onProgress(boolean haveFileSize,int progress, @NotNull String currentSize,@NotNull  String sumSize);
}
