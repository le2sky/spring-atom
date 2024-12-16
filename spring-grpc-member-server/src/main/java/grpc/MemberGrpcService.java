package grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import member.grpc.MemberProto;
import member.grpc.MemberServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class MemberGrpcService extends MemberServiceGrpc.MemberServiceImplBase {

    private final MemberService memberService;

    @Override
    public void createMember(
            MemberProto.MemberRequest request,
            StreamObserver<MemberProto.MemberCreateResponse> responseObserver
    ) {
        log.info("request = {}", request.toString());
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(request.getId(), request.getEmail(), request.getPassword(), request.getName());
        Member member = memberService.createMember(memberSignUpRequest);
        MemberProto.MemberCreateResponse memberCreateResponse = MemberProto.MemberCreateResponse.newBuilder()
                .setId(member.getId())
                .setEmail(member.getEmail())
                .setName(member.getName())
                .setPassword(member.getPassword())
                .build();

        responseObserver.onNext(memberCreateResponse);
        responseObserver.onCompleted();
    }
}
