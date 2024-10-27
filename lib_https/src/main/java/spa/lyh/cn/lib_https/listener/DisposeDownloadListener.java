package spa.lyh.cn.lib_https.listener;

import spa.lyh.cn.lib_https.exception.OkHttpException;

/**
 * @author liyuhao
 * @function 监听下载进度
 */
public interface DisposeDownloadListener{

	void onSuccess(String filePath, String fileName);

	void onFailure(OkHttpException error);

	void onProgress(boolean haveFileSize,int progress, String currentSize, String sumSize);
}
