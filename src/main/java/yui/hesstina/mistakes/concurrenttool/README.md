# 01 | 使用了并发工具类库，线程安全就高枕无忧了吗？

## 没有意识到线程重用导致用户信息错乱的 Bug

在这个包下
```java
package yui.hesstina.mistakes.concurrenttool.threadlocal;
```

- 错误用法
```http request
GET http://localhost:8080/threadlocalmisuse/wrong?userId=1
```
请求两次
```json
{
  "before": "http-nio-8080-exec-1 - null",
  "after": "http-nio-8080-exec-1 - 1"
}
```
```json
{
  "before": "http-nio-8080-exec-1 - 1",
  "after": "http-nio-8080-exec-1 - 1"
}
```
同一个线程会有两个相同的值

- 正确用法
```http request
GET http://localhost:8080/threadlocalmisuse/right?userId=1
```
请求两次
```json
{
"before": "http-nio-8080-exec-1 - null",
"after": "http-nio-8080-exec-1 - 1"
}
```
结果相同

## 使用了线程安全的并发工具，并不代表解决了所有线程安全问题

在这个包下
```java
package yui.hesstina.mistakes.concurrenttool.concurrenthashmapmisuse;
```

- 错误用法
```http request
GET http://localhost:8080/concurrenthashmapmisuse/wrong
```
日志文件
```text
2021-01-26 01:57:30.574  INFO 3388 --- [nio-8080-exec-1] .m.c.c.ConcurrentHashMapMisuseController : init size:900
2021-01-26 01:57:30.579  INFO 3388 --- [Pool-1-worker-9] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.580  INFO 3388 --- [ool-1-worker-13] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.580  INFO 3388 --- [Pool-1-worker-6] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.580  INFO 3388 --- [ool-1-worker-11] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.580  INFO 3388 --- [Pool-1-worker-4] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.580  INFO 3388 --- [Pool-1-worker-2] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.583  INFO 3388 --- [ool-1-worker-15] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 01:57:30.585  INFO 3388 --- [Pool-1-worker-8] .m.c.c.ConcurrentHashMapMisuseController : gap size:31
2021-01-26 01:57:30.585  INFO 3388 --- [ool-1-worker-10] .m.c.c.ConcurrentHashMapMisuseController : gap size:54
2021-01-26 01:57:30.585  INFO 3388 --- [Pool-1-worker-1] .m.c.c.ConcurrentHashMapMisuseController : gap size:68
2021-01-26 01:57:30.590  INFO 3388 --- [nio-8080-exec-1] .m.c.c.ConcurrentHashMapMisuseController : finish size:1753
```

- 正确用法
在并发的部分加锁

```http request
GET http://localhost:8080/concurrenthashmapmisuse/right
```
日志文件
```text
2021-01-26 02:05:03.135  INFO 1320 --- [nio-8080-exec-1] .m.c.c.ConcurrentHashMapMisuseController : init size:900
2021-01-26 02:05:03.140  INFO 1320 --- [Pool-1-worker-9] .m.c.c.ConcurrentHashMapMisuseController : gap size:100
2021-01-26 02:05:03.148  INFO 1320 --- [ool-1-worker-10] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.150  INFO 1320 --- [Pool-1-worker-1] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.150  INFO 1320 --- [ool-1-worker-11] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.151  INFO 1320 --- [Pool-1-worker-8] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.151  INFO 1320 --- [ool-1-worker-13] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.153  INFO 1320 --- [ool-1-worker-15] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.154  INFO 1320 --- [Pool-1-worker-4] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.154  INFO 1320 --- [Pool-1-worker-6] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.154  INFO 1320 --- [Pool-1-worker-2] .m.c.c.ConcurrentHashMapMisuseController : gap size:0
2021-01-26 02:05:03.155  INFO 1320 --- [nio-8080-exec-1] .m.c.c.ConcurrentHashMapMisuseController : finish size:1000
```

## 没有充分了解并发工具的特性，从而无法发挥其威力
在这个包下
```java
package yui.hesstina.mistakes.concurrenttool.concurrenthashmapperformance;
```

```http request
GET http://localhost:8080/concurrenthashmapperformance/compare
```

```text
2021-01-26 02:32:37.202  INFO 1404 --- [nio-8080-exec-1] c.ConcurrentHashMapPerformanceController : StopWatch '': running time = 4490709500 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
3511184200  078%  normal use
979525300  022%  good use
```
