# 01 | 使用了并发工具类库，线程安全就高枕无忧了吗？

## 没有意识到线程重用导致用户信息错乱的 Bug

ThreadLocalMisuseController.java

- 错误用法
```http request
GET http://localhost:8080/threadlocal/wrong?userId=1
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
GET http://localhost:8080/threadlocal/right?userId=1
```
请求两次
```json
{
"before": "http-nio-8080-exec-1 - null",
"after": "http-nio-8080-exec-1 - 1"
}
```
结果相同