메일을 분산환경에서 스케줄링하여 보내야하는데, 어떻게 풀어볼 수 있을까?
스프링을 사용하는 분산환경에서 안전하게 스케줄링 로직을 수행하는 방법은 무엇이 있을까?

- mysql named lock이나 was 환경 변수에 was 번호와 전체 was 수를 입력하는 방법을 생각해봤다.
- 찾아보니, shedLock, 분산 스케줄링이라는 개념이 있는데.. 알아보자.

```
java -jar ./build/libs/subscribe-mail-send-0.0.1-SNAPSHOT.jar
```
