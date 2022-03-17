package spa.lyh.cn.lib_https.multirequest;

import com.alibaba.fastjson.TypeReference;

import okhttp3.Call;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeMultiDataListener;

public class MultiCall {
   public Call call;
   public DisposeMultiDataListener listener;
   public TypeReference<?> typeReference;
   public boolean useHttpFilter;

   public MultiCall(Call call,TypeReference<?> typeReference,boolean useHttpFilter, DisposeMultiDataListener listener){
      this.call = call;
      this.listener = listener;
      this.useHttpFilter = useHttpFilter;
      this.typeReference = typeReference;
   }
}
