package spa.lyh.cn.httputils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiDownloadRequestCenter;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeMultiDownloadListener;
import spa.lyh.cn.lib_https.listener.RequestResultListener;
import spa.lyh.cn.lib_https.multirequest.MultiCall;
import spa.lyh.cn.lib_https.multirequest.MultiDownloadCall;
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
    public static void getNewVersion(Activity activity, DisposeDataListener listener) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("appType", "1");
        bodyParams.put("siteId", "924958456908492800");
        Call call = RequestCenter.postRequest(activity, "http://ums.offshoremedia.net/app/versionInfo", bodyParams, null, listener);
    }

    public static void logNewVersion(Activity activity, DisposeDataListener listener) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("appType", "1");
        bodyParams.put("siteId", "924958456908492800");
        Call call = RequestCenter.postRequest(activity, "http://ums.offshoremedia.net/app/versionInfo", bodyParams, null, listener);
    }


    protected static Call postRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeDataListener listener) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, true), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData, @NonNull String stringBody) {
                if (!activity.isFinishing()){
                    if (listener != null){
                        try {
                            listener.onSuccess(headerData,stringBody);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
                if (!activity.isFinishing()) {
                    if (listener != null){
                        try {
                            listener.onFailure(error);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, true));

        return call;
    }


    protected static Call getRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeDataListener listener) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, true), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData, @NonNull String stringBody) {
                if (!activity.isFinishing()){
                    if (listener != null){
                        try {
                            listener.onSuccess(headerData,stringBody);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
                if (!activity.isFinishing()) {
                    if (listener != null){
                        try {
                            listener.onFailure(error);
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
            public void onSuccess(@NonNull Headers headerData) {
                if (listener != null){
                    listener.onSuccess(headerData);
                }
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
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

    public static Call downloadFile(Context context, String url, String path, DisposeDownloadListener listener) {
        HeaderParams params = new HeaderParams();
        return HttpClient.getInstance(context).downloadFile(context,
                CommonRequest.createDownloadRequest(url, null, params, true),
                new DisposeDataHandle(listener, path, true),HttpClient.OVERWRITE_FIRST);
    }

    public static MultiDownloadCall createDownloadFile(Context context, String url, DisposeMultiDownloadListener listener){
        return new MultiDownloadCall(
                HttpClient.getInstance(context)
                        .createRequest(CommonRequest.createGetRequest(url,null,null,true)),
                false,
                listener);
    }

    public static void startDownloadRequestPool(Context context, List<MultiDownloadCall> calls,String filePath, RequestResultListener listener){
        MultiDownloadRequestCenter
                .get(context)
                .setDevMode(true)
                .setOverwriteMod(HttpClient.OVERWRITE_FIRST)
                .setFilePath(filePath)
                .addRequests(calls)
                .startTasks(listener);
    }

}
