**Bulk Insert**
> batch insert 라고도 하는 것 같다.

- DB의 다양한 요소들에 의해, 쿼리 한번이 이루어지면 그 전후에 이루어지는 작업이 있다. (Transaction, Index 등)
- DB에 1,000,000개의 데이터를 삽입하는 상황을 생각해보자.
- DB에 1개의 데이터를 넣을때 N, 쿼리 전후에 M이라는 값이 소모된다고 가정해보자.
- 만약 쿼리 하나에 1개의 데이터를 INSERT 하게 되면,
- 소모값은 (1,000,000 * N + 1,000,000 * M)이다.
- 하지만 쿼리 하나에 1,000개의 데이터를 INSERT 하게 되면,
- 소모값은 (1,000,000 * N + 1,000 * M)이다.
- 출처 : [Bulk Inserting - MySQL 다량의 데이터 넣기](https://dev.dwer.kr/2020/04/mysql-bulk-inserting.html)
- 출처 : [우아한형제들 - MySQL 환경의 스프링부트에 하이버네이트 배치 설정해 보기](https://techblog.woowahan.com/2695/)
- 출처 : [컬리 - BULK 처리 Write에 집중해서 개선해보기](https://helloworld.kurly.com/blog/bulk-performance-tuning/)
- 출처 : [넥스트리 - jpa 배치 인서트 vs jdbc 배치 인서트](https://www.nextree.io/jpavsjdbc/)
