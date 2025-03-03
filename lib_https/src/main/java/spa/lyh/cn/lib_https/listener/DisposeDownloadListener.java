package spa.lyh.cn.lib_https.listener;

import org.jetbrains.annotations.NotNull;

import spa.lyh.cn.lib_https.exception.OkHttpException;

/**
 * @author liyuhao
 * @function 监听下载进度
 */
public interface DisposeDownloadListener{

	void onSuccess(@NotNull String filePath,@NotNull  String fileName);

	void onFailure(@NotNull OkHttpException error);

	void onProgress(boolean haveFileSize,int progress, @NotNull String currentSize,@NotNull  String sumSize);
}
