package spa.lyh.cn.lib_https.utils;


public class LogUtils {

    public static String makeResponseLog(String url, String source,String time){
        String suffix;
        if (source.endsWith("\n") || source.endsWith("\r\n")){
            suffix = "";
        }else {
            suffix = "\n";
        }
        String result = "->\n"
                +"url:"+url+"\n"
                +"duration:"+time+"\n"
                +"response:" + source + suffix
                + "<-";
        return result;
    }

    public static String makeHeaderLog(String url, String source,String time){
        String suffix;
        if (source.endsWith("\n") || source.endsWith("\r\n")){
            suffix = "";
        }else {
            suffix = "\n";
        }
        String result = "->\n"
                +"url:"+url+"\n"
                +"duration:"+time+"\n"
                +"header:" + source + suffix
                + "<-";
        return result;
    }
}
