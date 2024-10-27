package spa.lyh.cn.lib_https.listener;

/**
 * @author liyuhao
 *
 */
public class DisposeDataHandle {

	public DisposeJsonListener jsonListener = null;
	public DisposeDownloadListener downloadListener = null;
	public DisposeHeadListener headListener = null;

	public DisposeStringListener stringListener = null;
	public boolean devMode;

	public String mSource = null;

	/**
	 * 不指定对应的clazz，应该用不到
	 * @param listener
	 */
	public DisposeDataHandle(DisposeStringListener listener,boolean devMode)
	{
		this.stringListener = listener;
		this.devMode = devMode;
	}

	/**
	 * json请求使用的handle
	 * @param listener
	 * @param devMode
	 */
	public DisposeDataHandle(DisposeJsonListener listener, boolean devMode)
	{
		this.jsonListener = listener;
		this.devMode = devMode;
	}


	/**
	 * 头请求使用的handle
	 * @param listener
	 * @param devMode
	 */
	public DisposeDataHandle(DisposeHeadListener listener,boolean devMode)
	{
		this.headListener = listener;
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