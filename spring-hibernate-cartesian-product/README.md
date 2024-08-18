#### 상황

1. 정상적인 수로 나왔어야할 Category의 수가 뻥튀기 되어 있었다. (cartesian)
2. PreviewProduct에 distinct 키워드를 주었을때, PreviewProduct의 수는 줄어들었지만 Product의 수는 줄어들지 않아서이지 않을까라는 생각이 들었다.
3. 따라서 Product의 수 * Category의 수 만큼 Category가 중복 생성되지 않았나 싶은 가설 -> 어떻게 증명할지는 감이 잡히지 않는다.
4. 당장은 Category의 수가 중복되니 @OneToMany 자료구조를 Set으로 설정했다.
5. 성공적으로 동작하는 것처럼 보였지만 Category가 정렬이 안된채로 내려진다.
6. @OrderBy를 OneToMany에 설정해주면 정렬이 된다. (v1)
7. 다만 v1 쿼리는 전체 데이터를 모두 불러오기 때문에 비효율적이다.
8. 따라서 페이징 처리를 하려고 했으나, one to many fetch를 사용하기 때문에 똑같이 메모리 부족 현상이 발생한다. (v2)

#### 고민

- OrderBy의 동작 원리는 무엇인가?
- Category 중복은 왜 발생했을까?
- OneToMany join fetch를 안쓰면 어떻게 풀어낼 수 있을까?
