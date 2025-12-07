package ch.zhaw.freelance4u.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ch.zhaw.freelance4u.model.MailInformation;
import ch.zhaw.freelance4u.util.ServiceUtils;
import reactor.core.publisher.Mono;

@Service
public class MailValidatorService {
    private final WebClient webClient;

    public MailValidatorService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://disify.com/api")
                .filter(ServiceUtils.logRequest())
                .filter(ServiceUtils.logResponse())
                .build();
    }

    public Mono<MailInformation> validateEmail(String email) {
        return webClient.get()
                .uri("/email/" + email)
                .retrieve()
                .bodyToMono(MailInformation.class);
    }

    public boolean isEmailValid(MailInformation mailInfo) {
        return mailInfo != null
                && Boolean.TRUE.equals(mailInfo.getFormat())
                && Boolean.FALSE.equals(mailInfo.getDisposable())
                && Boolean.TRUE.equals(mailInfo.getDns());
    }
}
