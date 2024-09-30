### 동시성 문제를 비관적 락으로 해결하려는 방식

#### 가설

- 트랜잭션 격리수준 serializable을 사용하는 것은 내부적으로 락을 사용하기 때문에 명시적으로 레코드 잠금을 구하는 방식과 원리는 동일하다.
- 다만, serializable은 한 트랜잭션에서 불필요한 부분까지 잠금을 구하기 때문에(모든 조회가 락을 잡고 조회된다.) 비효율적이다.
- (가설) 비관적 락을 사용하면 serializable 보다는 상대적으로 효율적으로 동시성 문제를 해결할 수 있을 것이다.

#### 적용

- repository에서 다음과 같이 lock을 설정한다.

```java

@Lock(LockModeType.PESSIMISTIC_READ)
@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
boolean existsMemberCouponByMemberIdAndCouponId(Long memberId, Long couponId);
```

- validate 로직에서 다음과 같은 쿼리가 발생한다.

```mysql
    select mc1_0.id
    from member_coupon mc1_0
    where mc1_0.`member_id` = 2
      and mc1_0.coupon_id = 2
    limit 1
    for
    share
```

- 위 경우에도 serializable과 마찬가지로 gap락을 이용해 insert를 막는다.

#### 장점과 한계

장점 :

- WAS가 분산되어 있는 환경에서도 동작한다.
- serializable에 비해 불필요한 레코드에 잠금을 설정하지 않는다.

한계 :

- 불필요한 공간까지 잠금하기 때문에 상대적인 성능 저하와 데드락을 야기할 수 있다.
    - 여기서 말하는 불필요한 공간이란 충돌이 나는게 어색한 구간에도 gap락에 의해 insert가 불가능한 부분이다.
    - 가령, `insert into member_coupon(is_used, coupon_id, member_id)
    values (false, 3, 12);` 과 같은 쿼리는 coupon_id와 member_id가 전혀 다르다.
    - 하지만 gap락에 의해서 위 쿼리 또한 막히게 되니 동시 처리 능력이 상당히 희생된다.
