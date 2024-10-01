### 동시성 문제를 분산락으로 해결해보자 (Redis 활용편)

#### 가설

- (가설) Redis를 이용하면 DB 커넥션을 점유하지 않고 동시성 문제를 해결할 수 있을 것이다.

#### 적용

```java

public Long issue(Long memberId, Long couponId) {
    // 락을 획득하지 못했는데, 락을 릴리즈하는 것을 경계해야 한다.
    memberCouponIssueLock.lock(memberId, couponId);
    try {
        return memberCouponIssuer.issue(memberId, couponId);
    } finally {
        memberCouponIssueLock.unlock(memberId, couponId);
    }
}
```

- Redis Client인 Lettuce와 Redisson을 사용하는 방식이 대표적이다.
- Lettuce의 경우에는 따로 지원해주는 것이 없기 때문에 SETNX 명령어를 이용해 직접 구현해야 한다.
- Redisson의 경우에는 RLock이라는 클래스를 통해서 분산락을 사용할 수 있도록 지원한다.

#### Lettuce 분산락

Lettuce 적용 방식은 다음과 같다. (MemberCouponIssueLock의 구현체인 LettuceMemberCouponIssueLock)

```java

@Override
public void lock(Long memberId, Long couponId) {
    int tryCount = 10;

    tryLockWithSpin(memberId, couponId, tryCount);
}

private void tryLockWithSpin(Long memberId, Long couponId, int tryCount) {
    while (!requestLock(memberId, couponId)) {
        if (tryCount-- == 0) {
            // lock 획득 실패 처리
            throw new RuntimeException();
        }

        try {
            // redis에 너무 많은 부하를 주지 않기 위해 sleep을 설정
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

private boolean requestLock(Long memberId, Long couponId) {
    Boolean result = redisTemplate
            .opsForValue() // opsForX는 커맨드를 호출할 수 있는 기능을 모은 인터페이스를 반환
            .setIfAbsent(generateKey(memberId, couponId), "empty", Duration.ofSeconds(3));

    return Boolean.TRUE.equals(result);
}
```

#### Lettuce 주의 사항

- 락을 획득하는데 필요한 타임아웃을 직접 구현해야한다.
- 스핀락 방식으로 Redis에 부하를 준다.

#### Redisson 분산락

```java

@Override
public void lock(Long memberId, Long couponId) {
    RLock lock = redissonClient.getLock(generateKey(memberId, couponId));
    try {
        boolean acquired = lock.tryLock(5, TimeUnit.SECONDS);
        if (!acquired) {
            // 락 획득 실패
            throw new RuntimeException();
        }
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}
```

- Redisson은 RLock을 제공한다. 이는 락에 대해 타임아웃과 같은 설정을 지원한다.
- Pub/Sub 방식으로 락이 해제되면 락을 구독하는 클라이언트에게 신호를 전달한다.

#### 장점과 한계

장점 :

- WAS가 분산되어 있는 환경에서도 동작한다.
- 응용 계층에서 추상화된 Lock 인터페이스를 사용할 수 있어, 다른 방식으로 전환할 수 있다.
- 기존에 Redis를 운영하고 있는 경우 추가 비용 없이 구축이 가능하다.
- DB 커넥션을 점유하고 대기하지 않아도 된다.

한계 :

- 단순히 동시성 문제를 해결하기 위해서 도입하는 것은 애매한 지점이 있다. Redis에 대한 학습 비용과 인프라 비용, 유지보수 비용이 추가로 발생한다.
