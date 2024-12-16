package grpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import member.grpc.MemberProto;
import member.grpc.MemberServiceGrpc;
import org.springframework.stereotype.Component;

@Component
class MemberGrpcServiceClient {

    private final MemberServiceGrpc.MemberServiceBlockingStub blockingStub;

    public MemberGrpcServiceClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = MemberServiceGrpc.newBlockingStub(channel);
    }

    public MemberProto.MemberCreateResponse createMember(MemberProto.MemberRequest request) {
        return blockingStub.createMember(request);
    }
}
