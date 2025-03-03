package spa.lyh.cn.lib_https.multirequest;

import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeMultiDownloadListener;

public class MultiDownloadCall {

    public Call call;
    public DisposeMultiDownloadListener downloadListener;

    public MultiDownloadCall(Call call, DisposeMultiDownloadListener listener){
        this.call = call;
        this.downloadListener = listener;
    }
}
