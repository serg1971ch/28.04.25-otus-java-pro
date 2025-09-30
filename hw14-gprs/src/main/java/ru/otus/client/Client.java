package ru.otus.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.otus.client.service.ClientNumberService;

import static ru.otus.ConnectionParams.SERVER_HOST;
import static ru.otus.ConnectionParams.SERVER_PORT;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(SERVER_HOST + ":" + SERVER_PORT)
                .usePlaintext()
                .build();
        var client = new ClientNumberService(channel);
        client.get();
        channel.shutdown();
    }
}