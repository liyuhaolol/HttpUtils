package spa.lyh.cn.lib_https.filter;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import okhttp3.Headers;

public abstract class BaseHttpFilter {

    public abstract boolean dataFilter(@NotNull Context context,@NotNull  String url,@NotNull  Headers headers,@NotNull String stringBody);
}
