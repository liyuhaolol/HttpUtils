package spa.lyh.cn.lib_https.listener;

import okhttp3.Headers;

/**********************************************************
 * 监听文件
 **********************************************************/
public interface DisposeDataListener {

	/**
	 * 请求成功回调事件处理
	 */
	void onSuccess(Headers headerData,Object bodyData);

	/**
	 * 请求失败回调事件处理
	 */
	void onFailure(Object error);

}
