### 트랜잭션 격리수준

여러 트랜잭션이 동시에 처리되는 경우, 특정 트랜잭션이 다른 트랜잭션에서 변경하거나 조회하는 데이터를 볼 수 있도록 허용한다.
하위로 갈 수록 격리 수준이 낮아지며, 동시 처리 능력이 향상된다. 데이터 정합과 성능이 반비례한다.

```mysql
select @@session.transaction_isolation;
set session transaction isolation level READ COMMITTED;
select @@session.transaction_isolation;
select @@global.transaction_isolation;
```

#### READ UNCOMMITTED

- 커밋하지 않은 데이터도 접근할 수 있는 격리 수준
- 다른 트랜잭션의 작업이 커밋되거나 롤백되지 않아도 즉시 보인다.
- 다른 트랜잭션의 작업이 완료되지 않았는데, 다른 트랜잭션에서 조회 가능한 문제를 더티 리드라고 한다.
- 데이터 부정합 확률이 높지만, 가장 높은 성능을 보인다.

```mysql
start transaction;

insert into coupon (money, issued_at, benefit_strategy, name)
values (1000, now(), 'unknown', '1000원');

rollback;
```

```mysql
set session transaction isolation level read uncommitted;
start transaction;

select *
from coupon;

commit;
```

#### SERIALIZABLE

- 가장 엄격한 격리수준으로 트랜잭션을 순차 실행한다.
- 여러 트랜잭션은 동시에 레코드에 접근할 수 없기 때문에 데이터 부정합 문제는 발생하지 않는다.
- 다만, 트랜잭션이 순차적으로 처리되어야 하기 때문에 동시 처리 능력이 상당히 희생된다.
- 해당 격리 수준에서는 순수 select 작업에서도 대상 레코드에 넥스트 키 락을 읽기 잠금으로 사용한다.
    - 다른 트랜잭션에서 락이 걸린 부분에 다른 트랜잭션에서 절대로 삽입 및 갱신이 불가하다.
