package spa.lyh.cn.lib_https;

import java.util.ArrayList;
import java.util.List;

import spa.lyh.cn.lib_https.multirequest.MultiCall;

public class MultiRequestCenter {
   public final static String TAG = "MultiRequestCenter";

   private int number = 5;//线程并发数

   private List<MultiCall> requestList;

   private static MultiRequestCenter instance;

   public static MultiRequestCenter getInstance(){
      if (instance == null){
         synchronized (MultiRequestCenter.class){
            if (instance == null){
               instance = new MultiRequestCenter();
            }
         }
      }
      return instance;
   }

   public MultiRequestCenter(){
      requestList = new ArrayList<>();
   }

   public MultiRequestCenter setThreadNumber(int number){
      this.number = number;
      return this;
   }

   public MultiRequestCenter addRequest(MultiCall multiCall){
      if (canAddRequest()){
         requestList.add(multiCall);
      }else {

      }
      return this;
   }

   public boolean canAddRequest(){
      return true;
   }
}
