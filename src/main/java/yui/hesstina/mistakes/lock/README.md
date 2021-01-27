# 01 | 代码加锁：不要让“锁”事成为烦心事
原子性相关的问题
```http
GET http://localhost:8080/lockscope/wrong
```

只单方面的给 add 方法加锁，compare 没加锁也是没意义的
```text
2021-01-27 12:46:23.091  INFO 16084 --- [      Thread-48] y.h.m.lock.lockscope.AtomicMistakes      : compare start
2021-01-27 12:46:23.091  INFO 16084 --- [      Thread-47] y.h.m.lock.lockscope.AtomicMistakes      : add start
2021-01-27 12:46:23.091  INFO 16084 --- [      Thread-48] y.h.m.lock.lockscope.AtomicMistakes      : a: 8444, b: 8451, a < b: false
2021-01-27 12:46:23.092  INFO 16084 --- [      Thread-48] y.h.m.lock.lockscope.AtomicMistakes      : a: 23580, b: 23579, a < b: false
2021-01-27 12:46:23.092  INFO 16084 --- [      Thread-48] y.h.m.lock.lockscope.AtomicMistakes      : a: 30880, b: 30882, a < b: true
2021-01-27 12:46:23.118  INFO 16084 --- [      Thread-48] y.h.m.lock.lockscope.AtomicMistakes      : compare done
2021-01-27 12:46:23.125  INFO 16084 --- [      Thread-47] y.h.m.lock.lockscope.AtomicMistakes      : add done
```

add 和 compareRight 都加锁
```text
2021-01-27 13:13:02.006  INFO 17064 --- [      Thread-45] y.h.m.lock.lockscope.AtomicMistakes      : add start
2021-01-27 13:13:02.025  INFO 17064 --- [      Thread-45] y.h.m.lock.lockscope.AtomicMistakes      : add done
2021-01-27 13:13:02.025  INFO 17064 --- [      Thread-46] y.h.m.lock.lockscope.AtomicMistakes      : compare start
2021-01-27 13:13:02.032  INFO 17064 --- [      Thread-46] y.h.m.lock.lockscope.AtomicMistakes      : compare done
```

对象锁的问题
- 错误的加锁方式
```http
GET http://localhost:8080/lockscope/wrong1
```

```json
{
  "result": 530093
}
```

- 正确的加锁方式
```http
GET http://localhost:8080/lockscope/right1
```
```json
{
  "result": 999999
}
```