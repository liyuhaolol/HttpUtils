## 1.4.5版本，修正filter的返回code错误问题
## 1.4.4版本，PUT和DELETE请求增加json格式的请求
## 1.4.3版本，修正请求池的请求拦截器传递对象永恒为String的BUG
## 1.4.2版本，开放RequestParams对象接受，Intager，Float，Double，Long，Boolean对象以应对JsonRequest里的各种基本数据类型。
## 1.4.0版本，将fastjson替换为fastjson2，okhttp升级为4.11.0，conscrypt升级为2.5.2
- 因为升级为fastjson2所以推荐使用本框架的项目，也要将自己的json解析框架升级为fastjson2，不换的话，不保证是否会出现其他问题
`api "com.alibaba.fastjson2:fastjson2:2.0.34.1.android4"`
