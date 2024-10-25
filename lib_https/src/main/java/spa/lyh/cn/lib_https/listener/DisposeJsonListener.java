package spa.lyh.cn.lib_https.listener;

import com.alibaba.fastjson2.JSONObject;

import okhttp3.Headers;

/**********************************************************
 * 监听文件
 **********************************************************/
public interface DisposeJsonListener {

	/**
	 * 请求成功回调事件处理
	 */
	void onSuccess(Headers headerData, JSONObject jsonObject);

	/**
	 * 请求失败回调事件处理
	 */
	void onFailure(Object error);

}
