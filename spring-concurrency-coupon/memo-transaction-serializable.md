### 동시성 문제를 트랜잭션 격리 수준으로 해결하려는 방식(SERIALIZABLE편)

#### 가설

- 트랜잭션 격리수준 serializable을 사용하면 내부적으로 단순 읽기 작업인 경우에도 락을 획득한다.
- (가설) 이를 잘 이용하면 동시처리능력을 희생시켜 동시성 문제를 해결할 수 있지 않을까?

#### 적용

- `@Transactional`의 격리 레벨 설정을 serializable로 변경한다.

- validate 로직에서 다음과 같은 쿼리가 발생한다.

```mysql
 select mc1_0.id
 from member_coupon mc1_0
 where mc1_0.`member_id` = ?
   and mc1_0.coupon_id = ?
 limit ?
```

- serializable인 경우 발생하는 레코드 잠금을 재연하기 위해 다음과 같은 쿼리를 작성한다.

```mysql
select member_coupon.id
from member_coupon
where member_id = 2
  and coupon_id = 2
limit 1;
```

- 이 경우 S,GAP 잠금을 확인할 수 있다. (넥스트 키락이 아닌 Shrared Gap Lock)
- 만약 member_coupon(memberId, couponId) 조합이 (1, 2), (6, 2) 두 개 존재하는 경우에는
- member_id가 5인 경우까지 갭락을 건다.
- 위 예시의 경우 member_id 인덱스를 사용하는데, 세컨더리 인덱스 member_id에서 1,6 순서로 저장되어 있다.
- 이때 반복 읽기를 보장하려면 member_id 1부터 5까지는 모두 막아야 1,6 순서를 보장한다.
- 즉, member_id가 1부터 5까지 들어가는 member_coupon을 막으면, member_coupon(1, 2), member_coupon(6, 2) 사이에 새로운 값이 insert되는 것을 막을 수
  있다.
- 핵심은 gap락과 s락으로 인해 다른 트랜잭션에서 경합이 발생해 동시성 문제를 해결할 수 있다는 것이다.

#### 장점과 한계

장점 :

- WAS가 분산되어 있는 환경에서도 동작한다.
- 적용이 간단하다.

한계 :

- 불필요한 공간까지 잠금하기 때문에 상대적인 성능 저하와 데드락을 야기할 수 있다.
    - 상대적인 성능 저하라는 의미는 동시성 문제를 해결하는 모든 방식의 아이디어가 처리량을 희생하는 방법이기 때문이다.
