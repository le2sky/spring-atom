### 자바 스레드 동기화 방식

- 스레드 동기화 : 공유 자원에 동시에 접근하지 못하도록 접근 순서를 제어하는 방법
- synchronized를 이용한 암묵적인 동기화
- java.util.concurrent.locks의 lock 클래스들을 이용한 명시적인 동기화

### synchronized, wait(), notify()

#### synchronized 키워드

임계 영역을 설정한다. 가능하면 메서드 전체에 lock을 거는 것보다는 최소화하는 것이 효율적이다.

1. 메서드 전체 critical section
    - `public synchronized void sample()`
2. 특정 영역을 critical section으로 지정
    - `synchronized(객체 참조변수) {}`

#### wait(), notify()

- Object 내부에 정의, synchronized 블럭 내부에서만 사용 가능
- wait() : 임계 영역에서 더 이상 수행할 작업이 없는 경우 해당 스레드는 해당 객체의 waiting pool에서 대기(시간 설정 가능)
- nofify() : 해당 객체의 waiting pool에 있던 스레드 중 임의의 스레드 하나만 통지
- sleep의 경우 lock을 릴리즈하지 않음
- (참고)  join() 메서드는 명시된 시간만큼 해당 스레드가 죽기를 기다림. 만약 아무런 매개변수를 지정하지 않으면 죽을 때까지 계속 대기함.

#### 한계

- 스레드를 구별하여 통지하는 것처럼 동기화를 세세하게 컨트롤해야하는 경우에 한계가 있다.
- 특정 스레드는 notify를 받지 못하여 기아현상이 발생할 수 있다.

### ReentrantLock, Condition

#### ReentrantLock

- 가장 일반적인 Lock
- await(), signal()을 이용해 특정 조건에서 lock을 풀고 나중에 다시 lock을 얻어 작업 수행이 가능하다.
- ReentrantLock을 이용하면 다양한 기능을 사용 가능
    - 폴링과 시간 제한이 있는 락 확보
    - 블록을 벗어나는 구조의 락
    - 인터럽트 걸 수 있는 락 확보 방법
    - condition을 적용하여 대기 중인 스레드를 선별적으로 깨울 수 있음

#### 사용 방법

- lock() : lock 잠금
- unlock() : lock 해제
- isLocked() : lock 잠겼는지 확인
- tryLock() : 폴링(임계 영역 수행 시간이 짧은 경우 스핀락 방식이 효율적일 수 있다는 것 같은데, lock의 경우는 확정적인 컨텍스트 스위칭이 있으니까 그런거겠쥬?)
- tryLock(long timeout, TimeUnit unit) throws InterruptedException

#### Condition

- condition을 사용하면 스레드의 종류에 따라 구분된 waiting pool에서 따로 기다리도록 하여 선별적인 통지를 가능하게 한다.
- lock.newCondition()
- wait -> await(), awaitUninterruptibly()
- wait(long timeout) -> await(long time, TimeUnit unit), long awaitNanos(long nanosTimeout), awaitUntil(Date deadline)
- notify -> signal()
- notifyAll -> signalAll()

### synchronized vs ReentrantLock 중 무엇이 나은가?

- ReentrantLock은 synchronized보다 유연하다.(공정성 여부, 타임아웃, 블록 구조가 아니어도 락을 적용.. 등등)
- 암묵적인 락은 명시적인 락에 비해서 상당한 장점을 가진다.
    - 코드에 나타나는 표현 방법도 익숙하고 간결하고, 기존 프로그램 중에서 암묵적인 락을 사용하는 경우가 많으니 일관성 측면에서 나을 수 있다.
    - 또한, ReentrantLock은 암묵적인 락에 비해 더 위험할 수도 있다.(finally 블록에 unlock 메서드를 넣어서 락을 해제하도록 하지 않으면..?)
- ReentrantLock은 암묵적인 락으로만 해결하기 어려운 복잡한 상황에서 사용하기 위한 고급 동기화 기능이다. 일반적으로 다음과 같은 상황에 사용하는 것을 권장한다.
    - 락을 확보할 때, 타임아웃을 지정해야 하는 경우
    - 폴링의 형태로 락을 확보하고자 하는 경우
    - 락을 확보하느라 대기 상태에 들어가 있을 때, 인터럽트를 걸 수 있어야 하는 경우
