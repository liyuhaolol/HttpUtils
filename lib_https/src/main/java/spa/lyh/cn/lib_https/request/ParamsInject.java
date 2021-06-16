package spa.lyh.cn.lib_https.request;

import android.util.Log;

import java.util.List;

import okhttp3.FormBody;

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
            Log.e("qwer","String");
            String stringValue = (String) value;
            mFormBodyBuild.add(key,stringValue);
        }else if (value instanceof List){
            Log.e("qwer","List");
            List<?> listValue = (List<?>) value;
            for (int i = 0;i < listValue.size();i++){
                Object oValue = listValue.get(i);
                if (oValue instanceof String){
                    Log.e("qwer","String2");
                    String lsValue = (String) oValue;
                    mFormBodyBuild.add(key,lsValue);
                }else {
                    Log.e(TAG,"不支持的参数类型");
                }
            }

        }else {
            Log.e(TAG,"不支持的参数类型");
        }
    }
}
