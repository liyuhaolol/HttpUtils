package spa.lyh.cn.lib_https.filter;

import android.content.Context;

import okhttp3.Headers;

public abstract class BaseHttpFilter {

    public abstract boolean dataFilter(Context context,String url, Headers headers, Object bodyData);
}
