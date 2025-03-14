package spa.lyh.cn.lib_https.response.base;

/**
 * Created by liyuhao on 2017/8/29.
 */

public class CommonBase {
    public static final String TAG = "HttpUtils";
    /**
     * the logic layer exception, may alter in different app
     *
     */
    public static final String NET_MSG = "网络或服务器连接失败";
    public static final String NET_MSG_CODE = "网络连接失败:";
    public static final String EMPTY_MSG = "未知错误";
    public static final String EMPTY_RESPONSE = "响应为空";
    public static final String JSON_CONVERT_ERROR = "JSON对象转换失败，JsonString格式有误";
    public static final String CANCEL_MSG = "请求取消";
    public static final String IO_LENGTH_MSG = "无法得到文件长度";
    public static final String IO_NET_MSG = "下载中断";
    public static final String COOKIE_STORE = "Set-Cookie"; // decide the server it
    // can has the value of
    // set-cookie2
}
