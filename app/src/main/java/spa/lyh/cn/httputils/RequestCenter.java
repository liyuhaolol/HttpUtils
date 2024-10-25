package spa.lyh.cn.httputils;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson2.TypeReference;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeJsonListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.HeaderParams;
import spa.lyh.cn.lib_https.request.RequestParams;

public class RequestCenter {

    /**
     * 检查更新
     *
     * @param activity
     * @param listener
     */
    public static void getNewVersion(Activity activity, DisposeJsonListener listener) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("appType", "1");
        bodyParams.put("siteId", "924958456908492800");
        //TypeReference typeReference = new TypeReference<JsonFromServer<UpdateInfo>>() {};
        Call call = RequestCenter.postRequest(activity, "http://ums.offshoremedia.net/app/versionInfo", bodyParams, null, listener);
    }


    protected static Call postRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeJsonListener listener) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, true), new DisposeDataHandle(new DisposeJsonListener() {
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
        }, true));

        return call;
    }

    protected static Call getRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeJsonListener listener, TypeReference<?> typeReference) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, true), new DisposeDataHandle(new DisposeJsonListener() {
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
        }, true));

        return call;
    }

    protected static Call headRequest(final Activity activity, String url, HeaderParams headers, final DisposeHeadListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(activity).headResquest(CommonRequest.
                createHeadRequest(url, headers, true), new DisposeDataHandle(new DisposeHeadListener() {
            @Override
            public void onSuccess(Headers headerData) {
                if (listener != null){
                    listener.onSuccess(headerData);
                }
            }

            @Override
            public void onFailure(Object error) {
                if (listener != null){
                    listener.onFailure(error);
                }
            }
        }, true));

        return call;
    }

    public static Call createVersionRequest(Context context){
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("versionType", "1");
        bodyParams.put("channelType", "XiaoMi");
        return createPostRequest(
                context,
                "http://app.jrlamei.com/jrlmCMS/forApp/getChannelNewVersion.jspx",
                bodyParams,
                null,
                BuildConfig.DEBUG);
    }

    protected static Call createPostRequest(Context context,String url, RequestParams params, HeaderParams headers,boolean isDev){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createPostRequest(url,params,headers,isDev));
    }

}
