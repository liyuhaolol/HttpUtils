package spa.lyh.cn.lib_https.request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeaderParams {

    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public HeaderParams() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new RequestParams instance containing the key/value string
     * params from the specified map.
     *
     * @param source the source key/value string map to add.
     */
    public HeaderParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public HeaderParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

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

    public boolean hasParams() {
        if(urlParams.size() > 0){

            return true;
        }
        return false;
    }
}
