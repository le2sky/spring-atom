### 비관적 락

- 데이터베이스의 락을 사용하여 동시성을 제어하는 방법
- select ... for update, for shared 구문을 사용한다.

#### LockModeType

- PESSIMISTIC_WRITE : 배타 락
- PESSIMISTIC_READ : 공유 락
- PESSIMISTIC_FORCE_INCREMENT
    - 배타 락 + version 필드 사용
    - mysql 8.0 이상인 경우 nowait 추가
    - read만 하는 경우에도 version을 업데이트

#### JPA 동시성 제어 메커니즘과 트랜잭션 격리 수준의 차이점

- JPA 동시성 제어 메커니즘 : 특정 엔티티에 대한 동시 접근을 막기 위해 사용
- 트랜잭션 격리 수준 : 트랜잭션 동안의 일관된 데이터 읽기를 고려하기 위해 적용

#### 타임 아웃 설정

- 교착 상태를 해결하기 위해 대기 시간을 지정해야 한다.
- JPA에서 쿼리 힌트를 추가하면 된다. (javax.persistence.lock.timeout)
- DBMS에 따라서 힌트가 적용되지 않을 수 있다.
