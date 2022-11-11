package ru.shanalotte.coderun.gateway.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.50.1)",
    comments = "Source: coderun.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CoderunBalancerGrpc {

  private CoderunBalancerGrpc() {}

  public static final String SERVICE_NAME = "ru.shanalotte.coderun.loadbalancer.CoderunBalancer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<CodeRunRequest,
      CodeRunResult> getRunCodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "runCode",
      requestType = CodeRunRequest.class,
      responseType = CodeRunResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<CodeRunRequest,
      CodeRunResult> getRunCodeMethod() {
    io.grpc.MethodDescriptor<CodeRunRequest, CodeRunResult> getRunCodeMethod;
    if ((getRunCodeMethod = CoderunBalancerGrpc.getRunCodeMethod) == null) {
      synchronized (CoderunBalancerGrpc.class) {
        if ((getRunCodeMethod = CoderunBalancerGrpc.getRunCodeMethod) == null) {
          CoderunBalancerGrpc.getRunCodeMethod = getRunCodeMethod =
              io.grpc.MethodDescriptor.<CodeRunRequest, CodeRunResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "runCode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CodeRunRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CodeRunResult.getDefaultInstance()))
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
        @Override
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
        @Override
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
        @Override
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
    public void runCode(CodeRunRequest request,
                        io.grpc.stub.StreamObserver<CodeRunResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRunCodeMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRunCodeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                  CodeRunRequest,
                  CodeRunResult>(
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

    @Override
    protected CoderunBalancerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerStub(channel, callOptions);
    }

    /**
     */
    public void runCode(CodeRunRequest request,
                        io.grpc.stub.StreamObserver<CodeRunResult> responseObserver) {
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

    @Override
    protected CoderunBalancerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerBlockingStub(channel, callOptions);
    }

    /**
     */
    public CodeRunResult runCode(CodeRunRequest request) {
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

    @Override
    protected CoderunBalancerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoderunBalancerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<CodeRunResult> runCode(
        CodeRunRequest request) {
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

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RUN_CODE:
          serviceImpl.runCode((CodeRunRequest) request,
              (io.grpc.stub.StreamObserver<CodeRunResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
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

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Coderun.getDescriptor();
    }

    @Override
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

    @Override
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
