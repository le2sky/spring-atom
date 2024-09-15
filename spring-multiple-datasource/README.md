#### AbstractRoutingDataSource

- 여러 데이터소스를 등록하여 특정 상황에 원하는 데이터 소스를 사용할 수 있도록 추상화함
- lookupKey에 해당하는 데이터소스에 연결을 시켜줌

#### LazyConnectionDataSourceProxy

- 스프링은 트랜잭션 시작시 커넥션의 실제 사용여부와 무관하게 커넥션을 확보한다.
- 따라서, 불필요하게 커넥션을 사용하는 경우로 커넥션 고갈로 연결될 수 있다.
    - (하늘) JPA의 경우 실제 커넥션 사용 시점, 트랜잭션 aop 프록시 호출 시점에 커넥션 점유하는 걸로 보임
    - (하늘) 테스트 방법 : controller에서 em 호출, trasactional이 붙은 서비스 empty 메서드 호출
    - (하늘) 그러면, OSIV 킨다고 해도 실제 사용 시점 + aop 프록시 호출까지는 커넥션 획득을 미루는 건가?
- LazyConnectionDataSourceProxy를 사용하는 경우, 트랜잭션 시작 시 실제 커넥션이 필요한 경우에만 커넥션을 반환한다.
    - JPA 프록시 객체랑 비슷한 원리인가

#### 테스트 하는 법

```
show global variables like 'log_output';
show global variables like 'general_log';

set global log_output = 'table';
set global general_log = 1;
select convert(argument using utf8) from mysql.general_log\G;

set global log_output = 'file';
set global general_log = 0;
```
