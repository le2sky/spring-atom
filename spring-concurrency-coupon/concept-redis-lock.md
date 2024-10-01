### 레디스(Remote Dictionary Server)

> [NHN FORWARD 2021] Redis 야무지게 사용하기

#### Redis를 캐시로 사용하기

- 캐시란 무엇인가?
    - 데이터의 원래 소스보다 더 빠르고 효율적으로 액세스할 수 있는 임시 데이터 저장소를 캐시라고 한다.
    - 같은 데이터 반복적 액세스, 원본 보다 빠른 접근 속도가 필요, 변하지 않는 데이터가 캐시를 사용하기 적합
- Redis as Cache
    - 단순한 key-value 구조
    - in-memory data store
    - 빠른 성능
- 캐싱 전략
    - 레디스를 캐시로 사용할 때, 어떻게 배치하는가가 시스템 성능에 영향을 준다.
    - 캐싱 전략은 데이터 유형과 액세스 패턴을 잘 고려하여 선택해야 한다.
        - Look-Aside(Lazy Loading) 전략
            - 가장 일반적인 전략, cache look up 이후, cache miss인 경우 db를 경유하여 데이터를 캐시에 적재
            - 레디스가 다운되더라도 장애로 이어지지 않는다. 다만, cache에 붙던 커넥션이 db로 가면서 부하가 생길 수 있다.
            - 초반 캐시로 인한 성능 저하 : Cache Warming으로 미리 데이터 적재
        - Write-Around 전략
            - 쓰기 시 DB 업데이트, 단점은 Redis 데이터와 다를 수 있음
        - Write-Through 전략
            - DB 저장시 캐시와 DB 모두 쓴다, 캐시가 항상 최신이지만, 저장할때마다 두 지점에 저장하는 비효율, 불필요한 최신화 문제

#### Redis 데이터 타입

- Strings : 기본적인 데이터 타입, set 커맨드는 모두 string 형태로 사용
- BitMaps : 비트 단위의 연산 가능
- Lists : 데이터를 순서대로 저장, 큐로 사용하기 적절
- Hashes : 키안에 또다시 여러 키값 페어로 데이터를 저장
- Sets : 중복되지 않은 문자열의 집합
- Sorted Sets : 중복되지 않지만, score 기준으로 정렬, score 같으면 사전순
- HyperLogLogs : 많은 데이터 다룰 때, 사용 중복되지 않은 값의 수를 셀때 사용
- Streams : 로그를 저장하기 가장 좋음

#### Redis 아키텍처 및 HA 기능

- Replication : master, replica
- Sentinel : master, replica, sentinel node(일반 노드 모니터링)
- Cluster : master 3, sharding

#### Replication

- 단순한 복제 연결
- replicaof를 이용해 간단하게 복제 연결 가능
- 비동기식 복제
- ha 기능이 없으므로 장애시 수동 복구
    - 마스터 장애시 replica 노드에 접속하여 복제 중단
    - 애플리케이션 연결 설정 변경 및 재배포

#### Sentinel

- 자동 페일오버 가능한 HA 구성
- Sentinel 노드가 다른 노드를 감시
- 마스터가 비정상 상태일 경우 자동 페일오버 -> replica가 master가 된다.
- 연결 정보 변경 필요 없음 -> application은 sentinel만 알면 된다.
- sentinel 노드는 항상 3대 이상의 홀수로 존재, 과반수 이상의 sentinel이 동의해야 페일오버 진행

#### Cluster

- 스케일 아웃과 HA 구성
- 키를 여러 노드에 자동으로 분할하여 저장
- 모든 노드가 서로를 감시하여 마스터 비정상일 경우 자동으로 페일오버
- 3대의 마스터 노드가 필요하다. -> 리플리카 노드를 하나씩 추가하는게 일반적

#### 선택 기준

- HA 기능 여부에 따라 Sentinel, Cluster
    - 샤딩 기능 필요시 Cluster, 아니면 Sentinel
- HA 불필요한 경우, Replcation, Stand-Alone
    - 복제 필요한 경우 Replication, 아니면 Stnd-Alone

### Redis를 이용한 락

- 프로그래밍에서 i++는 다음과 같이 쪼개진다. (아토믹하지 않음)
    - register1 = i
    - register1 = register1 + 1
    - i = register1
- 반면, Redis 에서 아토믹 연산을 보장하는 커맨드를 지원해준다고한다. (개별 명령의 경우 싱글 스레드의 특성으로 인해 원자적이다.)
- 레디스의 setnx 명령어는 값이 존재하지 않으면 값을 설정한다.
    - [참고 : sentnx는 아토믹한가?](https://stackoverflow.com/questions/48716438/is-setnx-truly-atomic)
- 이를 이용하면 레디스에 값이 존재하지 않으면 설정하고, 설정 여부를 반환받아 락의 획득 여부를 판단할 수 있다.

#### Rettuce 클라이언트를 이용한 방식

- 이 경우 직접 setnx, setex를 이용한 분산락을 직접 구현해야 한다.
- 직접 retry, timeout 기능을 구현해야하는 번거로움이 있다.

#### Redission 클라이언트를 이용한 방식

- Reddission은 별도의 Lock interface를 지원한다.
- Pub/Sub 방식을 이용하여 락이 해제되면 락을 구독하는 클라이언트가 신호를 받고 락 획득을 시도한다.
