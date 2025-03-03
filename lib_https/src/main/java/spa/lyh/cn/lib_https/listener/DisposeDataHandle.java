package spa.lyh.cn.lib_https.listener;

/**
 * @author liyuhao
 *
 */
public class DisposeDataHandle {

	public DisposeDownloadListener downloadListener = null;
	public DisposeHeadListener headListener = null;

	public DisposeDataListener dataListener = null;
	public boolean devMode;

	public String mSource = null;

	/**
	 * @param listener
	 */
	public DisposeDataHandle(DisposeDataListener listener, boolean devMode)
	{
		this.dataListener = listener;
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