package ch.zhaw.freelance4u.config;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.zhaw.freelance4u.model.Company;
import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.tools.FreelancerTools;

@Configuration
public class OpenAiConfig {

    private final FreelancerTools freelancerTools;

    public OpenAiConfig(FreelancerTools freelancerTools) {
        this.freelancerTools = freelancerTools;
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel,
                                 List<FunctionCallback> functionCallbacks) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory()),
                        new SimpleLoggerAdvisor())
                .defaultFunctions(functionCallbacks.toArray(new FunctionCallback[0]))
                .build();
    }

    @Bean
    @org.springframework.context.annotation.Description("Information about the jobs in the database.")
    public FunctionCallback getAllJobsFunction() {
        return FunctionCallback.builder()
                .function("getAllJobs", (Supplier<List<Job>>) () -> freelancerTools.getAllJobs())
                .build();
    }

    @Bean
    @org.springframework.context.annotation.Description("Information about the companies in the database.")
    public FunctionCallback getAllCompaniesFunction() {
        return FunctionCallback.builder()
                .function("getAllCompanies", (Supplier<List<Company>>) () -> freelancerTools.getAllCompanies())
                .build();
    }

    public static class CreateCompanyRequest {
        public String name;
        public String email;
    }

    @Bean
    @org.springframework.context.annotation.Description("Create a new company with the given name and email. Returns the created company.")
    public FunctionCallback createCompanyFunction() {
        return FunctionCallback.builder()
                .function("createCompany", (Function<CreateCompanyRequest, Company>) req ->
                        freelancerTools.createCompany(req.name, req.email))
                .inputType(CreateCompanyRequest.class)
                .build();
    }

    public static class CreateJobRequest {
        public String title;
        public String description;
        public String jobType;
        public double earnings;
        public String companyName;
    }

    @Bean
    @org.springframework.context.annotation.Description("Create a new job with the given title, description, jobType (TEST, IMPLEMENT, REVIEW, or OTHER), earnings, and company name. If the company doesn't exist, it will be created with a default email. Returns the created job.")
    public FunctionCallback createJobFunction() {
        return FunctionCallback.builder()
                .function("createJob", (Function<CreateJobRequest, Job>) req ->
                        freelancerTools.createJob(req.title, req.description, req.jobType, req.earnings, req.companyName))
                .inputType(CreateJobRequest.class)
                .build();
    }
}
