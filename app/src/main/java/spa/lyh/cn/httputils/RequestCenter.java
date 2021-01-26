package spa.lyh.cn.httputils;

import android.app.Activity;

import com.alibaba.fastjson.TypeReference;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.RequestParams;

public class RequestCenter {

    /**
     * 检查更新
     *
     * @param activity
     * @param listener
     */
    public static void getNewVersion(Activity activity, DisposeDataListener listener) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("versionType", "1");
        bodyParams.put("channelType", "XiaoMi");
        Call call = RequestCenter.postRequest(activity, "http://app.jrlamei.com/jrlmCMS/forApp/getChannelNewVersion.jspx", bodyParams, null, listener, null);
    }


    protected static Call postRequest(final Activity activity, String url, RequestParams params, RequestParams headers, final DisposeDataListener listener, TypeReference<?> typeReference) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, true), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object responseObj) {
                if (!activity.isFinishing()){
                    if (listener != null){
                        try {
                            listener.onSuccess(headerData,responseObj);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (!activity.isFinishing()) {
                    if (listener != null){
                        try {
                            listener.onFailure(reasonObj);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, typeReference, true));

        return call;
    }

}
