package spa.lyh.cn.lib_https;


import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import spa.lyh.cn.lib_https.cookie.SimpleCookieJar;
import spa.lyh.cn.lib_https.filter.BaseHttpFilter;
import spa.lyh.cn.lib_https.https.HttpsUtils;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.response.CommonFileCallback;
import spa.lyh.cn.lib_https.response.CommonHeadCallback;
import spa.lyh.cn.lib_https.response.CommonJsonCallback;

/**
 * @author qndroid
 * @function 用来发送get, post请求的工具类，包括设置一些请求的共用参数
 */
public class HttpClient {
    private static final int TIME_OUT = 60;//超时参数
    public final static int OVERWRITE_FIRST = 1;
    public final static int ADD_ONLY = 2;
    private OkHttpClient mOkHttpClient;
    private BaseHttpFilter httpFilter;//拦截器

    private static HttpClient instance;

    public static HttpClient getInstance(Context context){
        if (instance == null){
            synchronized (HttpClient.class){
                if (instance == null){
                    instance = new HttpClient(context);
                }
            }
        }
        return instance;
    }
    private HttpClient(Context context){
        //为Client设置参数
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //返回true允许所有https证书
                return true;
            }
        });
        //设置cookie
        okHttpClientBuilder.cookieJar(new SimpleCookieJar(context));
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //允许重定向
        okHttpClientBuilder.followRedirects(true);
        /**
         * trust all the https point
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());

        mOkHttpClient = okHttpClientBuilder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public BaseHttpFilter getHttpFilter() {
        return httpFilter;
    }

    public void setHttpFilter(BaseHttpFilter httpFilter) {
        this.httpFilter = httpFilter;
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     * @param request
     * @param handle
     * @return
     */
    public Call sendResquest(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public Call headResquest(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonHeadCallback(handle));
        return call;
    }

    public Call downloadFile(Context context,Request request, DisposeDataHandle handle,int mod) {
        if (request != null){
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new CommonFileCallback(context,handle,mod));
            return call;
        }else {
            return null;
        }

    }
}