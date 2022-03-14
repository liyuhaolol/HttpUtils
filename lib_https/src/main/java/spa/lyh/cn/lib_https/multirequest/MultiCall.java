package spa.lyh.cn.lib_https.multirequest;

import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;

public class MultiCall {
   public Call call;
   public DisposeDataListener listener;

   public MultiCall(){};
   public MultiCall(Call call, DisposeDataListener listener){
      this.call = call;
      this.listener = listener;
   }
}
