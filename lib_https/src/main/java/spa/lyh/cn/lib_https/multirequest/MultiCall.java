package spa.lyh.cn.lib_https.multirequest;

import com.alibaba.fastjson.TypeReference;

import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;

public class MultiCall {
   public Call call;
   public DisposeDataListener listener;
   public TypeReference<?> typeReference;
   public boolean useHttpFilter;

   public MultiCall(Call call,TypeReference<?> typeReference,boolean useHttpFilter, DisposeDataListener listener){
      this.call = call;
      this.listener = listener;
      this.useHttpFilter = useHttpFilter;
      this.typeReference = typeReference;
   }
}
