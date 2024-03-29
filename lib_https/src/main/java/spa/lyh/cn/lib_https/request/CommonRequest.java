package spa.lyh.cn.lib_https.request;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import spa.lyh.cn.lib_https.listener.UploadProgressListener;
import spa.lyh.cn.lib_https.model.FilePart;

/**
 * @author 李宇昊
 * @function 接受请求参数，为我们生成request对象
 */
public class CommonRequest {
    /**
     * create the key-value Request
     *
     * @param url 链接
     * @param params 参数
     * @return request
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null,false);
    }

    /**可以带请求头的Post请求
     * @param url 链接
     * @param params 参数
     * @param headers 头文件
     * @return request
     */
    public static Request createPostRequest(String url, RequestParams params, HeaderParams headers, boolean isDev) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                ParamsInject.AddFormBodyBuilder(mFormBodyBuild,entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构建类的build方法获取真正的请求对象
        FormBody mFormBody = mFormBodyBuild.build();
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url).
                post(mFormBody).
                headers(mHeader)
                .build();
        return request;
    }

    /**
     * create the key-value Request
     *
     * @param url 链接
     * @param json 参数
     * @return request
     */
     public static Request createPostJsonRequest(String url, String json){
        return createPostJsonRequest(url,json,null,false);
     }

    /**可以带请求头的Post请求
     * @param url 链接
     * @param json 参数
     * @param headers 头文件
     * @return request
     */
    public static Request createPostJsonRequest(String url, String json, HeaderParams headers, boolean isDev){
        // 设置请求体的Content-Type为application/json
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody jsonBody = RequestBody.create(json, mediaType);

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url).
                post(jsonBody).
                headers(mHeader)
                .build();
        return request;
    }

    /**
     * ressemble the params to the url
     *
     * @param url 链接
     * @param params 参数
     * @return request
     */
    public static Request createGetRequest(String url, RequestParams params) {

        return createGetRequest(url, params, null,false);
    }

    /**
     * 可以带请求头的Get请求
     * @param url 链接
     * @param params 参数
     * @param headers 头文件
     * @return request
     */
    public static Request createGetRequest(String url, RequestParams params, HeaderParams headers, boolean isDev) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                ParamsInject.AddUrlParams(urlBuilder,entry.getKey(),entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        String webUrl = urlBuilder.substring(0, urlBuilder.length() - 1);//去掉末尾的&
        if (isDev){
            Log.e("WebUrl",webUrl);
        }
        /**
         * 修复自己截取的链接有时候不是个url的问题
         * 李宇昊
         * 2019.3.25
         */
        try{
            return new Request.Builder().
                    url(webUrl)
                    .get()
                    .headers(mHeader)
                    .build();
        }catch (Exception e){
            if (isDev){
                Log.e("HttpURLBuilder","错误的url");
            }
            return null;
        }
    }

    /**
     * 上传文件生成Request
     * @param url 链接
     * @param params 额外的键值对参数
     * @param file 上传的文件
     * @param headers 额外的头部键值对从参数
     * @param isDev 是否为开发者模式
     * @return Request
     */
    public static Request createUploadRequest(String url, RequestParams params, FilePart file, HeaderParams headers, boolean isDev, UploadProgressListener listener){

        List<FilePart> lst = new ArrayList<>();
        lst.add(file);
        return createUploadRequest(url,params,lst,headers,isDev,listener);
    }

    /**
     * 上传文件生成Request
     * @param url 链接
     * @param params 额外的键值对参数
     * @param fileList 上传的文件列表
     * @param headers 额外的头部键值对从参数
     * @param isDev 是否为开发者模式
     * @return Request
     */
    public static Request createUploadRequest(String url, RequestParams params, List<FilePart> fileList, HeaderParams headers, boolean isDev, UploadProgressListener listener){

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                ParamsInject.AddMultipartBodyBuilder(multipartBodyBuilder,entry.getKey(), entry.getValue());
            }
        }

        if (fileList != null && fileList.size() > 0){
            for (FilePart filePart:fileList){
                multipartBodyBuilder.addFormDataPart(filePart.name,filePart.filename,filePart.body);
            }
        }

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //生成header
        Headers mHeader = mHeaderBuild.build();
        //构建请求体
        UploadProgressRequestBody uploadBody = new UploadProgressRequestBody(multipartBodyBuilder.build(),isDev,listener);

        if (isDev){
            Log.e("WebUrl",url);
        }

        Request request = new Request.Builder()
                .url(url).
                post(uploadBody).
                headers(mHeader)
                .build();
        return request;
    }


    public static Request createDeleteRequest(String url, RequestParams params) {
        return createDeleteRequest(url, params, null,false);
    }

    public static Request createDeleteRequest(String url, RequestParams params, HeaderParams headers, boolean isDev) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                ParamsInject.AddFormBodyBuilder(mFormBodyBuild,entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构建类的build方法获取真正的请求对象
        FormBody mFormBody = mFormBodyBuild.build();
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url).
                delete(mFormBody).
                headers(mHeader)
                .build();
        return request;
    }

    public static Request createDeleteJsonRequest(String url, String json){
        return createDeleteJsonRequest(url,json,null,false);
    }

    public static Request createDeleteJsonRequest(String url, String json, HeaderParams headers, boolean isDev){
        // 设置请求体的Content-Type为application/json
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody jsonBody = RequestBody.create(json, mediaType);

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url).
                delete(jsonBody).
                headers(mHeader)
                .build();
        return request;
    }

    public static Request createPutRequest(String url, RequestParams params) {
        return createPutRequest(url, params, null,false);
    }

    public static Request createPutRequest(String url, RequestParams params, HeaderParams headers, boolean isDev) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                ParamsInject.AddFormBodyBuilder(mFormBodyBuild,entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构建类的build方法获取真正的请求对象
        FormBody mFormBody = mFormBodyBuild.build();
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url)
                .put(mFormBody)
                .headers(mHeader)
                .build();
        return request;
    }

    public static Request createPutJsonRequest(String url, String json) {
        return createPutJsonRequest(url, json, null,false);
    }

    public static Request createPutJsonRequest(String url, String json, HeaderParams headers, boolean isDev) {
        // 设置请求体的Content-Type为application/json
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody jsonBody = RequestBody.create(json, mediaType);

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url)
                .put(jsonBody)
                .headers(mHeader)
                .build();
        return request;
    }

    public static Request createHeadRequest(String url, HeaderParams headers) {
        return createHeadRequest(url, headers,false);
    }

    public static Request createHeadRequest(String url, HeaderParams headers, boolean isDev) {
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //生成header
        Headers mHeader = mHeaderBuild.build();
        if (isDev){
            Log.e("WebUrl",url);
        }
        Request request = new Request.Builder().url(url)
                .head()
                .headers(mHeader)
                .build();
        return request;
    }

    /**
     * ressemble the params to the url
     *
     * @param url 链接
     * @param params 参数
     * @return request
     */
    public static Request createDownloadRequest(String url, RequestParams params, HeaderParams headers, boolean isDev) {
        headers.put("Accept-Encoding","identity");
        return createGetRequest(url,params,headers,isDev);
    }
}