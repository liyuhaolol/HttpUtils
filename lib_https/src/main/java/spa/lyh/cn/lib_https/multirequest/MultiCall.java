package spa.lyh.cn.lib_https.multirequest;

import com.alibaba.fastjson.TypeReference;

import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;

public class MultiCall {
   public Call call;
   public DisposeDataListener listener;
   public TypeReference<?> typeReference;

   public MultiCall(Call call,TypeReference<?> typeReference, DisposeDataListener listener){
      this.call = call;
      this.listener = listener;
      this.typeReference = typeReference;
   }
}
