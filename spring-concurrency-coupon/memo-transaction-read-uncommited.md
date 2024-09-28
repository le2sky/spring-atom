### 동시성 문제를 트랜잭션 격리 수준으로 해결하려는 방식(READ_UNCOMMITED편)

#### 가설

- 데이터베이스를 활용하여 풀 수 있는 방법 중에서 트랜잭션 격리 수준이 생각났다.
- 다른 트랜잭션이 커밋하기 이전이라 기존재 여부 검증에 실패한다.
- (가설) 그렇다면, 다른 트랜잭션이 무슨 작업을 하고 있는지 알고 있다면 풀어볼 수 있지 않을까?

#### 적용

- @Transactional 어노테이션에 isolation 속성을 READ_UNCOMMITED로 설정한다.
- 더티 리드와 spring data jpa의 repository을 이용한다.

```java

@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public Long issue(Long memberId, Long couponId) {
    log.info("신규 쿠폰 발급 coupon = {}, member = {}", couponId, memberId);

    Member member = memberRepository.findById(memberId).orElseThrow();
    Coupon coupon = couponRepository.findById(couponId).orElseThrow();
    MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);

    // 1. IDENTITY에 의한 채번 insert 쿼리가 발생한다.
    memberCouponRepository.save(memberCoupon);
    validateAlreadyIssued(memberId, couponId);

    log.info("쿠폰 발급 종료 member = {}", memberId);
    return memberCoupon.getId();
}

// 2. 더티 리드를 이용해서 다른 트랜잭션에서 삽입한 데이터 알 수 있다.
private void validateAlreadyIssued(Long memberId, Long couponId) {
    try {
        // 3. 가장 먼저 insert를 수행하고 조회를 한 스레드는 1개를 반환할 것이고, 나머지는 그 이상의 데이터를 반환하니 예외가 발생
        memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId);
    } catch (IncorrectResultSizeDataAccessException e) {
        // 4. 예외가 발생한 트랜잭션은 롤백된다.
        throw new IllegalStateException("해당 사용자는 이미 쿠폰을 발급했습니다.");
    }
}
```

#### 장점과 한계

장점 :

- WAS가 분산되어 있는 환경에서도 동작한다.(일반적인 방식은 아니다.)
- 동시성 제어 구간을 축소할 수 있다. 가령, memberId = 1, couponId = 1과 memberId = 2, couponId = 2는 동시에 수행할 수 있다.

한계 :

- 정확하게 여러 스레드가 동시에 save를 호출하고, find를 수행하는 경우, 접근한 모든 스레드가 실패한다.
- repository.save 시점에 insert 쿼리가 바로 전송되어야 한다.
- 연산의 순서가 일반적이지 않아 다른 개발자가 이해하기 어려울 수 있다. -> 왜 validate가 아래에 있지? -> 위로 올린다. -> 동시성 문제가 발생한다.
