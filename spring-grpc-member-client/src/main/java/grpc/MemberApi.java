package grpc;

import lombok.RequiredArgsConstructor;
import member.grpc.MemberProto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class MemberApi {

    private final MemberGrpcServiceClient client;

    @PostMapping("/members")
    public String signUp() {
        MemberProto.MemberCreateResponse member = client.createMember(
                MemberProto.MemberRequest
                        .newBuilder()
                        .setEmail("email")
                        .setName("atom")
                        .setPassword("leehaneul")
                        .build()
        );

        return member.toString();
    }
}
