# 매일메일

## 요구사항

### 메일안에 링크를 타고 들어왔을때

1. 사용자는 메일로 매일 면접 질문을 받는다.
2. 사용자가 면접 질문을 프론트엔드/백엔드로 나누어서 선택할 수 있다.
3. 메일 안에서 답변 보러가기 같은 트리거 버튼을 클릭했을때 상세페이지로 넘어간다.

### 사용자가 직접 매일메일로 접속했을때

1. 모달이 띄워지고, 자기 분야와 이메일을 입력할수 있는 폼이 띄워진다.
2. 신청이 완료 됐을시에, 사용자가 신청 완료 메세지와 함께 메일이 전송될 정보를 보여준다. (ex: 매일 몇시에 보내질건지)

### 어드민 페이지

1. 보안을 고려해서 아톰과 버건디만 등록 가능하도록 한다.
2. 질문과 답변을 입력하고 분야를 선택할수 있는 폼이 있다. (마크다운 적용)

### JSON

1. POST /subscribe (메일 주소 등록)

```
{
    email : string
    category : string(frontend | backend)
}
```

2. GET /question/:id (면접 질문 상세 정보)

```
{
    id : number,
    title: string
    content : string
    category: string(frontend | backend)
}
```

3. POST /admin/question (면접 질문 등록)

```
{
    title: string
    content : string
    category: string(frontend | backend)
}
```

---

## 1단계

1. 전체 기능 구현은 수요일 오전(08/28)까지
2. 목요일 배포 (보류)
