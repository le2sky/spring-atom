### 성능 테스트

> 출처 : 테코톡 베베 허브 성능 테스트

#### 정의와 사용 이유

- 정의 : API 요청이 많은 상황에서 서버가 어떻게 동작하는지 확인하는 테스트하는 것이다.
- 사용 이유 :
    - 성능 테스트를 하지 않은 채로 배포하게 된다면,
        - 처리 속도 저하
        - 서버 응답 불가
        - 서버 올류 발생
    - 위와 같은 예기치 못한 상황이 발생할 수 있다.
    - 가용성(시스템이 서비스를 정상적으로 제공할 수 있는 상태)을 높이기 위해서 사용한다.
    - 서비스에서 설정한 목표치를 달성하기 위해 진행할 수 있다.
        - ex) 초당 1000건의 요청 처리, 모든 조회 요청을 1초 이내로 응답한다.

#### 성능 테스트 지표

- 처리량
    - 초당 1000건의 요청 처리 -> Throughput(처리량)
    - 초당 처리하는 작업의 수를 의미
    - `RPS(Request Per Second)` : 1초에 처리하는 HTTP 요청의 수
    - 처리량은 서브 시스템 중 가장 처리량이 낮은 부분(병목 구간)으로 계산할 수 있다.
    - `병목 구간` : 하위 시스템 중 가장 낮은 처리량을 가지는 부분
    - 성능을 개선하거나 시스템 확장으로 병목 구간이 해소된다면 병목 구간은 이동된다.
    - 병목 구간이 아닌 곳을 성능 개선한다면 전체적인 성능 개선에 실패할 수 있다.
    - (point) 병목 구간을 찾고, 병목 구간을 개선하는 것이 핵심이다.
- 응답 시간
    - 모든 조회 요청을 1초 이내로 응답 -> Latency(응답 시간)
    - 시스템이 요청을 받고 응답할때까지의 시간을 의미한다.
    - 시스템이 요청을 처리할 때 까지 대기하는 시간도 응답 시간에 포함된다.
    - 각 서브 시스템 응답 시간의 총합을 응답 시간이라고 한다.

#### 성능 테스트 지표의 상관 관계

- 처리량을 개선하면 짧은 대기 시간으로 응답 시간을 개선할 수 있다.
- 응답 시간을 개선하면 요청을 빠르게 처리하니 처리량이 개선된다.

#### 성능 테스트의 종류

- 스모크 테스트
    - 최소한의 부하를 주어 시스템이 정상적으로 동작하는지 확인한다.
    - 부하테스트를 주기 이전에 시스템이 정상 동작을 하는지 확인하는데 사용되기도 한다.
    - (하늘) 에피타이저 느낌
- 스파이크 테스트
    - 사용량이 급증하는 상황에서 시스템이 견디고 성능에 문제가 없는지 확인한다.
- 부하 테스트
    - 목표값에 해당되는 부하를 견딜 수 있을지 확인한다.
    - 시스템 구성이 변경되었을때, 이전과 비슷한 부하를 견딜 수 있는지 확인할 수 있다.
- 스트레스 테스트
    - 시스템의 최대치에 해당되는 부하를 받았을 떄 시스템이 어떻게 동작하는지 확인한다.
    - 부하 테스트 대비 50퍼센트 혹은 그 이상의 부하를 주며, 부하 테스트 진행 이후에 실시하는 것이 좋다.
    - 스트레스를 먼저하면 병목 구간을 찾기 어렵기 때문이다.
    - (하늘) 스트레스를 먼저하면 각 서브 시스템들 중에서 어디가 부하를 못 견디는지 확인하기 어렵기 때문일 것 같다.
- 내구 테스트
    - 평균 사용률로 일정 부하를 지속적으로 주며 시스템이 문제되는 지점을 확인하며, 흡수 테스트라고도 한다.
    - 가령, 메모리 누수와 같이 시스템을 장기간 켜두었을 때, 발생하는 문제를 내구 테스트를 통해 확인 가능하다.
- 중단점 테스트
    - 임계 지점을 찾기 위해 부하를 점진적으로 증가시키며 진행한다.
    - 이를 통해, 시스템의 한계 지점을 파악할 수 있다.

#### 성능 테스트 도구

- Jmeter
    - 많은 릴리즈와 개선 사항을 통해 고도로 유지되며 관리되고 있다.
    - 다양한 프로토콜을 제공한다. (http, https, rest, soap, ftp, db, mail...)
    - 인텔리제이 플러그인이 존재한다.
    - (단점) 스크립트가 복잡하며 문서가 부족하다.
    - (단점) 한 대의 worker에서 사용 가능한 사용자 수가 매우 한정적이다. -> 단계별 스레드 할당 방식
    - (단점) gui 모드에서 많은 메모리 사용
    - (단점) 그래프 가독성이 낮음

- k6
    - 공식 문서가 깔끔하다.
    - 스크립트가 간편하다. (js 기반)
    - 메모리가 적음
    - gui가 다양하다.
    - (단점) 유료 플랜이 존재
    - (단점) 확장성의 한계 -> 부하 분산 테스트나 대용량에는 단일 사용으로는 무리가 있다.

- nGrinder
    - 자바와 비슷한 스크립트 언어 지원
    - 한국어 지원
    - 완성도 높은 뼈대코드 지원
    - gui 가독성이 좋음
    - (단점) 프로세스가 분리되어 있다. (agent, controller)
    - (단점) 테스트 스크립트 IDE 미지원

#### 테스트 계획 수립

- 응답의 최소 레이턴시 정하기
    - 서비스가 1초 이상 지연되면 사용자는 7퍼센트
    - 서비스가 3초 이상 지연되면 사용자는 40퍼센트가 빠져나간다.
- 서비스가 peak일 때의 사용자 수를 구한다.
    - 사용자가 가장 많을 떄 보다 더 넉넉하게 테스트한다.
    - (tip) think time : 사용자가 다음 요청을 보내기 전까지 활동하는 시간
