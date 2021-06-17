package spa.lyh.cn.lib_https.request;

import android.util.Log;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.MultipartBody;

/**
 * 参数注入
 */
public class ParamsInject {
    private static final String TAG = "HttpParams";

    /**
     * 添加form表单参数
     * @param mFormBodyBuild
     * @param key
     * @param value
     */
    public static void AddFormBodyBuilder(FormBody.Builder mFormBodyBuild,String key,Object value){
        if (value instanceof String){
            String stringValue = (String) value;
            mFormBodyBuild.add(key,stringValue);
        }else if (value instanceof List){
            @SuppressWarnings("unchecked")
            List<String> listValue = (List<String>) value;
            for (String lValue : listValue){
                mFormBodyBuild.add(key,lValue);
            }

        }else if (value instanceof String[]){
            String[] stringsValue = (String[]) value;
            for (String sValue : stringsValue){
                mFormBodyBuild.add(key,sValue);
            }
        }else {
            Log.e(TAG,"不支持的值类型,键："+key);
        }
    }

    public static void AddMultipartBodyBuilder(MultipartBody.Builder multipartBodyBuilder, String key, Object value){
        if (value instanceof String){
            String stringValue = (String) value;
            multipartBodyBuilder.addFormDataPart(key,stringValue);
        }else if (value instanceof List){
            @SuppressWarnings("unchecked")
            List<String> listValue = (List<String>) value;
            for (String lValue : listValue){
                multipartBodyBuilder.addFormDataPart(key,lValue);
            }

        }else if (value instanceof String[]){
            String[] stringsValue = (String[]) value;
            for (String sValue : stringsValue){
                multipartBodyBuilder.addFormDataPart(key,sValue);
            }
        }else {
            Log.e(TAG,"不支持的值类型,键："+key);
        }
    }

    public static void AddRequestParams(RequestParams requestParams, String key, Object value){
        if (value instanceof String){
            String stringValue = (String) value;
            requestParams.put(key,stringValue);
        }else if (value instanceof List<?>){
            @SuppressWarnings("unchecked")
            List<String> listValue = (List<String>) value;
            requestParams.put(key,listValue);
        }else if (value instanceof String[]){
            String[] stringsValue = (String[]) value;
            requestParams.put(key,stringsValue);
        }else {
            Log.e(TAG,"不支持的值类型,键："+key);
        }
    }

    public static void AddUrlParams(StringBuilder urlBuilder, String key, Object value){
        if (value instanceof String){
            String stringValue = (String) value;
            urlBuilder.append(key).append("=").append(stringValue).append("&");
        }else if (value instanceof List){
            @SuppressWarnings("unchecked")
            List<String> listValue = (List<String>) value;
            for (String lValue : listValue){
                urlBuilder.append(key).append("=").append(lValue).append("&");
            }

        }else if (value instanceof String[]){
            String[] stringsValue = (String[]) value;
            for (String sValue : stringsValue){
                urlBuilder.append(key).append("=").append(sValue).append("&");
            }
        }else {
            Log.e(TAG,"不支持的值类型,键："+key);
        }
    }
}
