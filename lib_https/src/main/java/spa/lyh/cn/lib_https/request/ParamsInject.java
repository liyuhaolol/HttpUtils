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
        }else if (value instanceof Integer){
            String mValue = value.toString();
            mFormBodyBuild.add(key,mValue);
        }else if (value instanceof Float){
            String mValue = value.toString();
            mFormBodyBuild.add(key,mValue);
        }else if (value instanceof Double){
            String mValue = value.toString();
            mFormBodyBuild.add(key,mValue);
        }else if (value instanceof Long){
            String mValue = value.toString();
            mFormBodyBuild.add(key,mValue);
        }else if (value instanceof Boolean){
            String mValue = value.toString();
            mFormBodyBuild.add(key,mValue);
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
        }else if (value instanceof Integer){
            String mValue = value.toString();
            multipartBodyBuilder.addFormDataPart(key,mValue);
        }else if (value instanceof Float){
            String mValue = value.toString();
            multipartBodyBuilder.addFormDataPart(key,mValue);
        }else if (value instanceof Double){
            String mValue = value.toString();
            multipartBodyBuilder.addFormDataPart(key,mValue);
        }else if (value instanceof Long){
            String mValue = value.toString();
            multipartBodyBuilder.addFormDataPart(key,mValue);
        }else if (value instanceof Boolean){
            String mValue = value.toString();
            multipartBodyBuilder.addFormDataPart(key,mValue);
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
        }else if (value instanceof Integer){
            Integer integer = (Integer) value;
            requestParams.put(key,integer);
        }else if (value instanceof Float){
            Float afloat = (Float) value;
            requestParams.put(key,afloat);
        }else if (value instanceof Double){
            Double aDouble = (Double) value;
            requestParams.put(key,aDouble);
        }else if (value instanceof Long){
            Long aLong = (Long) value;
            requestParams.put(key,aLong);
        }else if (value instanceof Boolean){
            Boolean aBoolean = (Boolean) value;
            requestParams.put(key,aBoolean);
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
        }else if (value instanceof Integer){
            Integer integer = (Integer) value;
            urlBuilder.append(key).append("=").append(integer).append("&");
        }else if (value instanceof Float){
            Float mValue = (Float) value;
            urlBuilder.append(key).append("=").append(mValue).append("&");
        }else if (value instanceof Double){
            Double mValue = (Double) value;
            urlBuilder.append(key).append("=").append(mValue).append("&");
        }else if (value instanceof Long){
            Long mValue = (Long) value;
            urlBuilder.append(key).append("=").append(mValue).append("&");
        }else if (value instanceof Boolean){
            Boolean mValue = (Boolean) value;
            urlBuilder.append(key).append("=").append(mValue).append("&");
        }else {
            Log.e(TAG,"不支持的值类型,键："+key);
        }
    }
}
