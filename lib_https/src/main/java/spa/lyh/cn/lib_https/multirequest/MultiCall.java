package spa.lyh.cn.lib_https.multirequest;


import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeMultiDataListener;

public class MultiCall {
   public Call call;
   public DisposeMultiDataListener dataListener;
   public boolean useHttpFilter;

   public MultiCall(Call call,boolean useHttpFilter, DisposeMultiDataListener listener){
      this.call = call;
      this.useHttpFilter = useHttpFilter;
      this.dataListener = listener;
   }
}
