package spa.lyh.cn.lib_https.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyuhao
 */
public class RequestParams {
    public ConcurrentHashMap<String, Object> urlParams = new ConcurrentHashMap<>();

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public RequestParams(){}


    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, List<String> listValue){

        if (key != null && listValue != null) {
            urlParams.put(key, listValue);
        }
    }

    public void put(String key, String[] listValue){

        if (key != null && listValue != null) {
            urlParams.put(key, listValue);
        }
    }

    public void put(String key, Integer value){
        urlParams.put(key, value);
    }
    public void put(String key, Float value){
        urlParams.put(key, value);
    }

    public void put(String key, Double value){
        urlParams.put(key, value);
    }

    public void put(String key, Long value){
        urlParams.put(key, value);
    }

    public void put(String key, Boolean value){
        urlParams.put(key, value);
    }

    public boolean hasParams() {
        if(urlParams.size() > 0){

            return true;
        }
        return false;
    }
}