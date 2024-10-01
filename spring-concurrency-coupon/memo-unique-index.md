### 동시성 문제를 유니크 인덱스로 해결하려는 방식

#### 가설

- 유니크 제약조건을 사용하면 중복 저장이 불가능하다.
- (가설) 유니크 제약조건을 도입하면 간단하게 동시성 문제를 해결할 수 있지 않을까?

#### 적용

- JPA에서는 다음과 같이 인덱스 제약조건을 추가할 수 있다.
- 중복 저장이 불가하므로 한 사용자는 쿠폰을 한 번만 지급받을 수 있게 된다.

```java

@Entity
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_member_coupon",
                columnNames = {"member_id", "coupon_id"}
        )
})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon {
}
```

#### 장점과 한계

장점 :

- 적용이 간단하다.
- 추가적인 작업(잠금, 지연, 잠금 해제)이 필요없다.

한계 :

- 기획적인 한계가 있을 수 있다.
    - 가령 member_coupon에 used 컬럼이 있다고 가정하자.
    - member_id(2), coupon_id(2), used
    - member_id(2), coupon_id(3), used
    - member_id(2), coupon_id(4), not_used
    - 위 경우에서 used는 여러개를 가질 수 있고, not_used는 1개만 가질 수 있다면 어떨까?
    - 데벨업의 경우는 mission_id, solution_id, status 조합으로 solution 레코드를 삽입한다.
    - 이때, 동시 호출시 문제가되는 경우는 in_progress status가 동시에 2개가 생기는 경우이다.
    - 그리고, solution status가 completed인 경우는 과거 내역 관리를 위해서 중복을 허용한다.
- 비즈니스 제약 조건이 DB에 의존적인 것은 단점일 수 있다.
    - (고민해볼 지점) DB를 다른 저장소로 변경해야하는 경우는 어떻게 되는가?
