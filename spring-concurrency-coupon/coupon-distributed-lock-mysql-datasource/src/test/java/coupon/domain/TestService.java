package coupon.domain;

class TestService {

    public synchronized void 한명씩_드루와ㅋㅋ(Long threadId) {
        System.out.println("안녕하세요 형님. 형님의 아이디는 : " + threadId + " 입니다.");
    }
}
