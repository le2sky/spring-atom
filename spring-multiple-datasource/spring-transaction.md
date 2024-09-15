### JDBC Connection에 대한 메모

#### JDBC에 대한 이해

- 각각의 DB마다 커넥션을 연결하는 방법, SQL 연결 방법, 결과 응답 방법이 모두 다르다.
- JDBC : 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API
    - java.sql.Connection : 연결
    - java.sql.Statement : SQL에 담을 내용
    - java.sql.ResultSet : SQL 요청 응답
- DriverManager.getConnection : Driver로부터 커넥션을 가져옴

#### DB 커넥션 흐름

- DB 드라이버를 통해 커넥션 조회
- DB 드라이버는 DB와 3way handshake하여 TCP/IP 연결
- DB 드라이버는 TCP/IP 연결 이후 부가정보(ID,PW)를 DB에 전달
- 인증이 완료되면 DB 내부에 세션이 생성
- DB가 커넥션 생성 응답을 보냄
- DB 드라이버가 커넥션 객체를 생성해 클라이언트에게 반환

### DB 커넥션 풀에 대한 메모

- TCP/IP 커넥션 매번 새로 만드는 비용이 큼(DB, Server 모두 부담)
- 커넥션을 미리 생성하여 풀어내는데, 이를 커넥션 풀이라고 함
- 커넥션 생성 비용 감소시키고, 무한정 늘어나는 문제를 해결

### 데이터소스에 대한 메모

- Hikari CP와 DriverManager로 커넥션 얻을 수 있음
- 다양한 커넥션 획득 방법을 추상화한 것이 데이터소스
- DriverManager는 데이터소스를 구현하지 않아서 DriverManagerDataSource를 사용해야함
- 커넥션 풀에서 커넥선을 생성하는 작업은 애플리케이션 실행 속도에 영향을 주지 않기 위해 별도 스레드에서 작동
- conection 풀을 사용하면 connection.close를 호출하면 반납된다.

### JDBC 트랜잭션에 대한 메모

```

Connection connection = dataSource.getConnection();
connection.setAutoCommit(false);

```

- 수동 커밋 모드로 설정하는 것을 트랜잭션을 시작한다고 표현할 수 있다.
- 애플리케이션에서 DB 트랜잭션을 적용하려면 서비스 계층이 복잡해진다.(connection 재사용 로직, seAutoCommit 호출..)
- 트랜잭션 문제
    - JDBC 구현 기술이 응용 계층에 누수
    - 트랜잭션 동기화 문제(같은 트랜잭션을 유지하기 위해 커넥션을 파라미터로 넘겨야함)
    - 트랜잭션 적용 반복 문제
- 예외 누수 문제 : 데이터 접근 계층의 JDBC 구현 기술 예외가 응용 계층으로 전파된다.
- JDBC 반복 문제 : 중복 코드 발생

### 스프링 트랜잭션에 대한 메모

#### 스프링 트랜잭션 추상화

- JDBC 트랜잭션과 JPA 트랜잭션의 양상이 다름
    - conn.setAutoCommit vs em.getTransaction().begin();
- begin, commit, rollback 추상화한 PlatformTransactionManager가 존재
- PlatformTransactionManager
    - DataSourceTransactionManager : JDBC
    - JpaTransactionManager : JPA
    - HibernateTransactionManager : Hibernate
    - EtcTransactionManager : 기타
- 스프링 5.3부터 DataSourceTransactionManger를 상속받아 기능을 확장한 JdbcTransactionManager 존재

```java
 public interface PlatformTransactionManager extends TransactionManager {
    TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
            throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```

- getTransaction()으로 트랜잭션을 시작한다.
- 기존에 이미 트랜잭션이 있는 경우 해당 트랜잭션에 참여할 수 있다.

#### 스프링 트랜잭션 동기화

- 스프링 트랜잭션 매니저는 트랜잭션 추상화와 리소스 동기화를 수행한다.
- `리소스 동기화` : 트랜잭션을 유지하기 위해 트랜잭션의 시작부터 끝까지 같은 DB 커넥션을 유지해야한다.
- 스프링은 트랜잭션 동기화 매니저를 제공하는데, 스레드 로컬을 사용해 커넥션을 동기화한다. (멀티 스레드 안정성)
- 트랜잭션 매니저 내부에서 동기화 매니저(TransactionSynchronizationManager)를 사용함
- 동작 방식
    - 트랜잭션 매니저는 데이터소스를 통해 커넥션을 확보해 트랜잭션을 시작한다.
    - 트랜잭션 매니저는 트랜잭션이 시작된 커넥션을 동기화 매니저에 보관
    - repository는 트랜잭션 동기화 매니저에 보관된 커넥션을 사용
    - 트랜잭션이 종료되면 트랜잭션 매니저는 트랜잭션 동기화 매니저에 보관된 커넥션을 통해 트랜잭션을 종료하고 커넥션을 닫음
- DataSourceUtils로 트랜잭션 동기화 사용 가능

#### 스프링 트랜잭션 템플릿

- 템플릿 콜백 패턴을 적용한 TransactionTemplate이 존재
- 로직 정상 수행 시 커밋, unchecked exception 발생시 롤백
- 체크 예외 또한 커밋한다.(후술)

#### 스프링 트랜잭션 AOP

- 스프링 AOP와 프록시를 이용해 응용 계층을 더욱 순수하게 만든다.
- @Transactional을 사용하면 스프링의 트랜잭션 AOP가 해당 애노테이션을 인식해 트랜잭션 프록시를 적용한다.
- 어드바이저 : `BeanFactoryTransactionAttributeSourceAdvisor`
- 포인트컷 : `TransactionAttributeSourcePointcut`
- 어드바이스 : `TransactionInterceptor`
- 동작 방식
    - AOP 프록시 호출
    - 스프링 컨테이너를 통해 트랜잭션 매니저 획득
    - trasactionManager.getTransaction()
        - 데이터 소스로부터 커넥션 생성
        - 오토 커밋 비활성화
        - 트랜잭션 동기화 매니저에 커넥션 보관
    - 실제 서비스 호출
    - 데이터 접근 로직에서 트랜잭션 동기화 커넥션 획득
