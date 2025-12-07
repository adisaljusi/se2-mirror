package ch.zhaw.freelance4u.util;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import reactor.core.publisher.Mono;

public class ServiceUtils {

    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logMethodAndUrl(request);
            return Mono.just(request);
        });
    }

    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            logStatus(response);
            return Mono.just(response);
        });
    }

    private static void logMethodAndUrl(ClientRequest request) {
        System.out.println("Request: " + request.method() + " " + request.url());
    }

    private static void logStatus(ClientResponse response) {
        System.out.println("Response Status: " + response.statusCode());
    }
}
