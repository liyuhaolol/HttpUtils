package spa.lyh.cn.lib_https.listener;

import spa.lyh.cn.lib_https.model.Progress;

public interface UploadTaskListener {
    void onSuccess(String url);
    void onFailure(int status, String msg);
    void onProgress(Progress progress);
}
