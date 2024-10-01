### 동시성 문제를 분산락으로 해결해보자 (MySQL 네임드락편)

#### 가설

- MySQL에 네임드락을 이용하면 임의의 문자열에 잠금을 걸 수 있다.
- (가설) 네임드락을 이용한 분산락을 구현하면, 레코드 잠금에 비해 적은 공간을 잠금하고 동시성 문제를 해결할 수 있을 것이다.

#### 적용

```mysql
select get_lock('mylock', 2);
select is_free_lock('mylock');
select is_used_lock('mylock');
select release_lock('mylock');
select release_all_locks();
```

- 네임드락은 위와 같은 쿼리로 얻거나, 릴리즈할 수 있다.
- 응용 계층에 MembercouponIsusueLock을 정의한다.(java-lock 참고)
- 인프라 계층에서 이를 구현하는 MySqlMemberCouponIssueLock을 만든다.
- MySqlMemberCouponIssueLock은 네임드락을 사용하는 MySqlLockRepository를 사용한다.

```java
public interface MySqlLockRepository extends JpaRepository<MemberCoupon, Long> {

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
```

#### 적용 시 주의사항

```java
public Long issue(Long memberId, Long couponId) {
    memberCouponIssueLock.lock(memberId, couponId);
    try {
        return memberCouponIssuer.issue(memberId, couponId);
    } finally {
        memberCouponIssueLock.unlock(memberId, couponId);
    }
}
```

- 데이터베이스 트랜잭션의 시점을 주의해야 한다.
- 데이터베이스 트랜잭션이 커밋하기 이전에 락을 릴리즈하면 동시성 문제가 다시 발생한다.
    - 이를 위해서 상위 계층 코드(파사드 같은)에서 락을 릴리즈하거나, synchronized를 설정해야 한다.
- 만약 부모 트랜잭션에 합류하고 있는 경우에는 부모 트랜잭션이 종료되는 경우 커밋이 된다. 즉, 커밋을 수행하기 이전에 락을 릴리즈하여 동시성 문제가 발생한다.
    - 이 경우에는 트랜잭션 전파 옵션(ex. REQUIRES_NEW)으로 해결할 수 있다. 하지만, 스레드가 두 개 이상의 커넥션을 동시에 점유하려는 경우, 부하 환경에서 히카리 커넥션 풀 데드락이 발생할 수
      있다.
- 네임드 락의 경우에는 락을 얻은 세션에서 릴리즈해야한다. 만약, OSIV가 꺼져있는 환경이라면 lock을 점유한 커넥션과 릴리즈한 커넥션이 다를 수 있다. 이 경우 예기치 못한 동작이 발생할 수 있다.

#### 장점과 한계

장점 :

- WAS가 분산되어 있는 환경에서도 동작한다.
- 응용 계층에서 추상화된 Lock 인터페이스를 사용할 수 있어, 다른 방식으로 전환할 수 있다.
- serializable이나 비관적락보다 상대적으로 적은 수의 잠금을 사용한다.
- 기존에 MySQL을 운영하고 있는 경우 추가 비용 없이 구축이 가능하다.

한계 :

- MySQL 기능에 의존적인 방식이며, 다른 DB로 변경되는 경우 한계가 있다.
- 커넥션을 점유하고 스레드가 대기하는 비효율이 생긴다. 이 경우, 커넥션 풀을 분리하거나 락을 점유하지 못하는 경우 빠른 실패를 유도할 수 있다. 빠른 실패를 유도하는 경우, 최초에 락을 점유한 스레드가 실패한
  경우 모든 동시 요청이 실패한다. 락 타임 아웃을 짧게 가져가는 경우에도 빠른 실패와 비슷하다.
- 데이터베이스를 한 번 더 찍어야 하므로 Redis를 이용한 분산락이나 레코드 잠금보다 지연이 발생한다.
- (고민해볼 지점) 레코드 잠금을 사용해 불필요한 동시성 제어까지 수행 vs 제어 구간은 핏하지만, 데이터베이스 왕복 시간과 커넥션 점유 대기 시간에서 비효율이 발생하는 네임드락
    - 전자의 경우 데드락 발생 위험이 있고, 후자는 없다.
    - 전자의 경우도 커넥션 점유 및 대기는 존재한다.
    - db 응답 시간은 db를 상대적으로 적게 요청하는 레코드 잠금 쪽이 우세하다.
    - 다만 서버의 동시 처리 능력은 후자를 선택하는 경우가 우세하다. 왜냐하면, 특정 api 요청 내부에서 member_id, coupon_id 조합으로 동시성을 제어하는 것과 대기하지 않아도 괜찮은 부분에서도
      대기하는 레코드 잠금 중에서 전자가 더 많은 요청을 처리할 수 있기 때문이다.
    - 전자의 경우에는 member-coupon issue api가 아닌 다른 api와도 충돌이 발생할 수 있다.
