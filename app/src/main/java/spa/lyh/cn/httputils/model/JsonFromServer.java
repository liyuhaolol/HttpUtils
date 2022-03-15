package spa.lyh.cn.httputils.model;

import java.io.Serializable;

public class JsonFromServer<T> implements Serializable{
   private static final long serialVersionUID = 1L;
   public int code;
   public String msg;
   public T data;
   public T info;
}
