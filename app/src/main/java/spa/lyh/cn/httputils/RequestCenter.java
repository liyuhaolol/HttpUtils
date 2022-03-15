package spa.lyh.cn.httputils;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
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
    public static void getNewVersion(Activity activity, DisposeDataListener listener,String... b) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("versionType", "1");
        bodyParams.put("channelType", "XiaoMi");
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        bodyParams.put("arrayList",arrayList);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        bodyParams.put("list",list);
        String[] strings = new String[]{"1","2","3"};
        bodyParams.put("strings",strings);
        bodyParams.put("string...",b);*/
        Call call = RequestCenter.postRequest(activity, "http://app.jrlamei.com/jrlmCMS/forApp/getChannelNewVersion.jspx", bodyParams, null, listener, null);
    }


    protected static Call postRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeDataListener listener, TypeReference<?> typeReference) {
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

    protected static Call getRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, final DisposeDataListener listener, TypeReference<?> typeReference) {
        //初始化等待loadDialog并显示
        //final LoadingDialog loadDialog = new LoadingDialog(activity);
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, true), new DisposeDataHandle(new DisposeDataListener() {
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

    protected static Call createVersionRequest(Context context){
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("versionType", "1");
        bodyParams.put("channelType", "XiaoMi");
        Call call = HttpClient.getInstance(context).createRequest(
                CommonRequest.createPostRequest(
                        "http://app.jrlamei.com/jrlmCMS/forApp/getChannelNewVersion.jspx?big=",
                        bodyParams,
                        null,
                        BuildConfig.DEBUG));
        return call;
    }

}
