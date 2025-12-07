package ch.zhaw.freelance4u.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelance4u.repository.JobRepository;
import ch.zhaw.freelance4u.service.CompanyService;
import ch.zhaw.freelance4u.service.UserService;
import ch.zhaw.freelance4u.tools.FreelancerTools;

@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @Autowired
    OpenAiChatModel chatModel;

    @Autowired
    ChatClient chatClient;

    ChatMemory chatMemory;

    @Autowired
    FreelancerTools freelancerTools;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(required = true) String message) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String content = chatClient.prompt()
                .system("Du bist ein Chatbot. Der den Benutzer Fragen beantwortet über Bestehende Jobs, deren Preis, Beschreibung, Titel und Unternehmen.")
                .user(message)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }
}
