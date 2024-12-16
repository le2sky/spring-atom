package grpc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(MemberSignUpRequest request) {
        Member member = new Member(null, request.email(), request.password(), request.name());
        return memberRepository.save(member);
    }
}
