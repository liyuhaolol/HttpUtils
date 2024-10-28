package spa.lyh.cn.lib_https.multirequest;


import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeMultiJsonListener;
import spa.lyh.cn.lib_https.listener.DisposeMultiStringListener;

public class MultiCall {
   public Call call;
   public DisposeMultiJsonListener jsonListener;
   public DisposeMultiStringListener stringListener;
   public boolean useHttpFilter;

   public MultiCall(Call call,boolean useHttpFilter, DisposeMultiJsonListener listener){
      this.call = call;
      this.jsonListener = listener;
      this.useHttpFilter = useHttpFilter;
      this.stringListener = null;
   }

   public MultiCall(Call call,boolean useHttpFilter, DisposeMultiStringListener listener){
      this.call = call;
      this.jsonListener = null;
      this.useHttpFilter = useHttpFilter;
      this.stringListener = listener;
   }
}
