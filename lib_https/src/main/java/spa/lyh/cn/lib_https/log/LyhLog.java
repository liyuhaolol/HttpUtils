package spa.lyh.cn.lib_https.log;

import android.util.Log;

//这玩意是为了解决Android自带的Log.e无法在logcat上打印极长的String的问题
public class LyhLog {
    private static final int STRING_MAXLENGTH = 1000; //Log单行的最大长度

    private static String TAG = "log_util";

    private static final int Level_Verbose = 1;
    private static final int Level_Info = 2;
    private static final int Level_Debug = 3;
    private static final int Level_Warn = 4;
    private static final int Level_Error = 5;


    public static void i(String tag, String msg) {

        handleMessage(Level_Info, tag, msg);
    }

    public static void d(String tag, String msg) {

        handleMessage(Level_Debug, tag, msg);
    }

    public static void e(String tag, String msg) {

        handleMessage(Level_Error, tag, msg);
    }

    public static void v(String tag, String msg) {

        handleMessage(Level_Verbose, tag, msg);
    }

    public static void w(String tag, String msg) {

        handleMessage(Level_Warn, tag, msg);
    }

    public static void handleMessage(int level, String tag, String message) {
        try{
            TAG = tag;
            int STRING_START = 0;
            int msg_difference = message.length() - STRING_MAXLENGTH;
            if (msg_difference > 0) {
                int STRING_END = STRING_MAXLENGTH;
                for (;;) {
                    showLog(level, tag, message.substring(STRING_START, STRING_END));
                    STRING_START = STRING_END;
                    STRING_END += STRING_MAXLENGTH;
                    if (STRING_END >= message.length()) {
                        showLog(level, TAG, message.substring(STRING_START));
                        return;
                    }
                }
            } else {
                showLog(level, tag, message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void showLog(int level, String tag, String message) {
        switch (level) {
            case Level_Verbose:
                Log.v(tag, message);
                break;
            case Level_Debug:
                Log.d(tag, message);
                break;
            case Level_Info:
                Log.i(tag, message);
                break;
            case Level_Warn:
                Log.w(tag, message);
                break;
            case Level_Error:
                Log.e(tag, message);
                break;
            default:
                break;

        }

    }
}
