package ru.otus.server.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberClient;
import ru.otus.protobuf.generated.NumberServer;
import ru.otus.protobuf.generated.NumberServiceGrpc;

public class ServerNumberService extends NumberServiceGrpc.NumberServiceImplBase {

    @Override
    public void get(NumberClient request, StreamObserver<NumberServer> responseObserver) {
        int firstNumber = request.getFirst();
        int lastNumber = request.getLast();

        for (int i = 0; i < (lastNumber - firstNumber); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NumberServer result = NumberServer.newBuilder()
                    .setResult(firstNumber + (i + 1))
                    .build();

            responseObserver.onNext(result);
        }
        responseObserver.onCompleted();
    }
}
