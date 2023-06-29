package spa.lyh.cn.lib_https.listener;


import com.alibaba.fastjson2.TypeReference;

/**
 * @author liyuhao
 *
 */
public class DisposeDataHandle {

	public DisposeDataListener mListener = null;
	public DisposeDownloadListener downloadListener = null;
	public DisposeHeadListener headListener = null;
	//public Class<?> mClass = null;
	public TypeReference<?> typeReference = null;
	public boolean devMode;

	public String mSource = null;

	/**
	 * 不指定对应的clazz，应该用不到
	 * @param listener
	 */
	public DisposeDataHandle(DisposeDataListener listener)
	{
		this.mListener = listener;
	}




	public DisposeDataHandle(DisposeHeadListener listener,boolean devMode)
	{
		this.headListener = listener;
		this.devMode = devMode;
	}


	/**
	 * json请求使用的handle
	 * @param listener
	 * @param typeReference
	 * @param devMode
	 */
	public DisposeDataHandle(DisposeDataListener listener, TypeReference<?> typeReference,boolean devMode)
	{
		this.mListener = listener;
		this.typeReference = typeReference;
		this.devMode = devMode;
	}

	/**
	 * 下文请求使用的handle
	 * @param listener
	 * @param source
	 * @param devMode
	 */
	public DisposeDataHandle(DisposeDownloadListener listener, String source,boolean devMode)
	{
		this.downloadListener = listener;
		this.mSource = source;
		this.devMode = devMode;
	}
}