package ch.zhaw.freelance4u.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailInformation {
    private String email;
    private Boolean format;
    private Boolean disposable;
    private Boolean dns;
}
