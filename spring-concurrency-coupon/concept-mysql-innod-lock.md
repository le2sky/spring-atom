### MySQL InnoDB의 잠금

```mysql
select *
from performance_schema.data_locks;
```

- InnoDB는 레코드 기반 잠금 방식을 탐재하여 뛰어난 동시성을 제공한다.

#### 락의 적용 요소에 따른 분류

- Shared Lock
    - 행을 읽을 때 사용한다.
    - 한 트랜잭션의 특정 레코드를 읽을 때 s락이 걸린다.
    - 다른 트랜잭션에서는 해당 레코드에 s락을 동시에 적용할 수 있지만 x락은 불가능하다.
    - s락을 획득하려면 먼저 테이블에서 IS락 이상을 획득해야 한다.

- Exclusive Lock
    - 행을 업데이트하거나 삭제할 때 사용된다.
    - 한 트랜잭션에서 특정 레코드를 변경할 경우, x락이 걸리고 해당 트랜잭션이 완료되기 이전까지 다른 트랜잭션에서는
    - 해당 레코드에 x락과 s락 모두 적용 불가하다.
    - x락을 획득하려면 먼저 테이블에 IS락을 획득해야 한다.

- Intention Lock
    - 트랜잭션이 테이블의 레코드에 대해 나중에 필요한 s락, x락을 나타내는 테이블 수준의 락이다.
    - IS락 : 트랜잭션이 테이블의 개별 레코드에 s락을 설정하려는 의도
        - select ... for share -> 테이블에 is락이 걸리고, 이후에 레코드에 s락이 걸림
    - IX락 : 트랜잭션이 테이블의 개별 레코드에 x락을 설정하려는 의도
        - select ... for update -> 테이블에 ix락이 걸리고, 이후에 레코드에 x락이 걸린다.

#### LOCK_MODE 메모

- S/X,GAP : Gap Lock
- S/X : Next Key Lock
- S/X,NOT_GAP : Record Lock

#### Gap Lock

- 실제 존재하지 않는 레코드 공간(간격)을 잠그는 것을 Gap Lock이라고 한다.
- Gap Lock은 PK뿐만 아닌 Secondary Index에도 동일하게 사용된다.
- Double, Datetime와 같은 타입에서는 2개의 값 사이에 중복되지 않는 값의 개수는 거의 무한대에 가깝다.
- Unique하지 않은 인덱스에서는 동일한 값이 중복해서 많이 저장될 수 도 있다.

#### Gap Lock의 필요성

- Repetable Read 격리 수준 보장
- Replication 일관성 보장
- Foreign Key 일관성 보장

#### Gap Lock의 특징과 주의 사항

- Shared Gap Lock = Exclusive Gap Lock
- Next Key Lock = Record Lock + Gap Lock
- Gap Lock은 내부적으로 Shared Lock 형태만 존재한다.
- 순수하게 다른 트랜잭션이 대상 간격에 insert 되는 것을 막는 것이 주 목적이다.
- 여러 트랜잭션의 Gap Lock은 서로 호환된다.
- Gap Lock은 순수하게 레코드 사이의 간격만 잠그는 것이 아닌, 필요에 의해 레코드와 간격을 동시에 잠그기도 함(Next Key Lock)
- 테이블의 레코드 건수가 적을수록 Gap Lock의 간격이 넓어진다.

#### MySQL에서 동등 비교 조건이 인덱스 종류별로 어떤 잠금이 사용되는가?

- Primary Key와 Unique Index
    - 쿼리의 조건이 1건의 결과를 보장하는 경우 : Record Lock만 사용
    - 쿼리의 조건이 1건의 결과를 보장하지 못하는 경우 : Record Lock + Gap Lock 사용
        - 레코드가 없거나, 복합 인덱스를 일부 컬럼만으로 where 조건이 사용된 경우도 포함)
- Non-Unique Secondary Index
    - 쿼리의 결과 대상 레코드 건수에 관계없이 항상 Record Lock + Gap Lock이 사용된다.
