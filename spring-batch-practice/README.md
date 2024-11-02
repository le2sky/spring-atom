#### 배치 애플리케이션와 spring batch

- 단발성으로 대용량의 데이터를 처리하는 애플리케이션
- 단발성으로 큰 데이터를 읽고, 가공하고, 저장하면 서버는 순식간에 CPU, IO 자원을 모두 사용하여 다른 리퀘스트를 처리하기 어렵다.
- 직접 배치 애플리케이션을 구성하기 위해서 비즈니스 로직 외에 부가 기능을 신경 써야 한다.
- spring batch는 이를 도운다.
- 배치 애플리케이션의 조건
    - 대용량 데이터
    - 자동화
    - 견고성
    - 신뢰성
    - 성능
- quartz는 스케줄러의 역할을 수행하고 대용량 데이터 배치 처리에 대한 기능을 지원하지 않음
- 스케줄마다 quartz가 배치를 실행하는 구조

#### memo

#### job, step, tasklet, (reader, processor, writer)
- job : 하나의 배치 작업
- step : 하나의 job 내부 여러 step
- tasklet, (reader, processor, writer) : 하나의 step 내부 tasklet, (reader, processor, writer) 묶음이 존재
- tasklet 하나와 reader, processor, writer 한 묶음이 같은 레벨
- = reader, processor가 끝나고 tasklet으로 마무리를 못함
- tasklet은 component, bean과 비슷한 역할

#### spring batch 메타 데이터

- 이전에 실행한 job이 어떤 것이 있는지
- 최근 실패한 batch parameter가 어떤 것이 있고, 성공항 job은 어떤 것들이 있는지
- 다시 실행한다면 어디서 부터 시작하면 좋은지
- 어떤 job의 step들이 있었고, step들 중 성공한 step과 실패한 step들은 무엇이 있는지
