#### 초기 설정

- https://github.com/naver/ngrinder/releases
- ngrinder-controller-{version}.war 다운로드
- `java -Djava.io.tmpdir=/Users/haneul/Desktop/ngrinder-temp -jar ngrinder-controller-{version}.war --port=8300`
- 초기 ID, PW : admin, admin
- 에이전트 설치
- `tar -xvf ngrinder-agent-{version}-localhost.tar`
- `cd ngrinder-agent`
- `./run_agent.sh`

#### 용어 정리

- Agent : 성능 측정 에이전트
- Vuser per agent : 에이전트 당 설정 가상 사용자 수(동시 요청 사용자)
- Process / Thread : 하나의 에이전트에 생성할 프로세스와 스레드
- Script : 성능 측정 시 각 에이전트에서 실행할 스크립트
- Duration : 성능 측정 수행 시간
- Run Count : 스레드 당 테스트 코드 수행 횟수
- Enable Ramp-up : 가상 사용자를 점진적으로 늘리도록 활성화
    - Initial Count : 시작 가상 사용자 수
    - Initial Sleep Time : 테스트 시작 시간
    - Incremental Step : 프로세스와 스레드를 증가시키는 수
    - Interval : 프로세스와 스레드 증가 간격
