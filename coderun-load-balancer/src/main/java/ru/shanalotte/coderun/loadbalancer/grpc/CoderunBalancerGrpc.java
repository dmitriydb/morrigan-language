package ru.shanalotte.coderun.loadbalancer.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.50.1)",
    comments = "Source: coderun.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CoderunBalancerGrpc {

  private CoderunBalancerGrpc() {}

  public static final String SERVICE_NAME = "ru.shanalotte.coderun.loadbalancer.grpc.CoderunBalancer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage,
      ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> getRunCodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "runCode",
      requestType = ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage.class,
      responseType = ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage,
      ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> getRunCodeMethod() {
    io.grpc.MethodDescriptor<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage, ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> getRunCodeMethod;
    if ((getRunCodeMethod = CoderunBalancerGrpc.getRunCodeMethod) == null) {
      synchronized (CoderunBalancerGrpc.class) {
        if ((getRunCodeMethod = CoderunBalancerGrpc.getRunCodeMethod) == null) {
          CoderunBalancerGrpc.getRunCodeMethod = getRunCodeMethod =
              io.grpc.MethodDescriptor.<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage, ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "runCode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage.getDefaultInstance()))
              .setSchemaDescriptor(new CoderunBalancerMethodDescriptorSupplier("runCode"))
              .build();
        }
      }
    }
    return getRunCodeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CoderunBalancerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerStub>() {
        @java.lang.Override
        public CoderunBalancerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoderunBalancerStub(channel, callOptions);
        }
      };
    return CoderunBalancerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CoderunBalancerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerBlockingStub>() {
        @java.lang.Override
        public CoderunBalancerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoderunBalancerBlockingStub(channel, callOptions);
        }
      };
    return CoderunBalancerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CoderunBalancerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoderunBalancerFutureStub>() {
        @java.lang.Override
        public CoderunBalancerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoderunBalancerFutureStub(channel, callOptions);
        }
      };
    return CoderunBalancerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CoderunBalancerImplBase implements io.grpc.BindableService {

    /**
     */
    public void runCode(ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage request,
        io.grpc.stub.StreamObserver<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRunCodeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRunCodeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage,
                ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage>(
                  this, METHODID_RUN_CODE)))
          .build();
    }
  }

  /**
   */
  public static final class CoderunBalancerStub extends io.grpc.stub.AbstractAsyncStub<CoderunBalancerStub> {
    private CoderunBalancerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoderunBalancerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerStub(channel, callOptions);
    }

    /**
     */
    public void runCode(ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage request,
        io.grpc.stub.StreamObserver<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRunCodeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CoderunBalancerBlockingStub extends io.grpc.stub.AbstractBlockingStub<CoderunBalancerBlockingStub> {
    private CoderunBalancerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoderunBalancerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerBlockingStub(channel, callOptions);
    }

    /**
     */
    public ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage runCode(ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRunCodeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CoderunBalancerFutureStub extends io.grpc.stub.AbstractFutureStub<CoderunBalancerFutureStub> {
    private CoderunBalancerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoderunBalancerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage> runCode(
        ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRunCodeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RUN_CODE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CoderunBalancerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CoderunBalancerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RUN_CODE:
          serviceImpl.runCode((ru.shanalotte.coderun.loadbalancer.grpc.CodeRunRequestMessage) request,
              (io.grpc.stub.StreamObserver<ru.shanalotte.coderun.loadbalancer.grpc.CodeRunResultMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CoderunBalancerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CoderunBalancerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ru.shanalotte.coderun.loadbalancer.grpc.Coderun.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CoderunBalancer");
    }
  }

  private static final class CoderunBalancerFileDescriptorSupplier
      extends CoderunBalancerBaseDescriptorSupplier {
    CoderunBalancerFileDescriptorSupplier() {}
  }

  private static final class CoderunBalancerMethodDescriptorSupplier
      extends CoderunBalancerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CoderunBalancerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CoderunBalancerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CoderunBalancerFileDescriptorSupplier())
              .addMethod(getRunCodeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
