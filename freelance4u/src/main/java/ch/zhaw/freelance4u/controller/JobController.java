package ch.zhaw.freelance4u.controller;

import java.util.Optional;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobCreateDTO;
import ch.zhaw.freelance4u.model.JobType;
import ch.zhaw.freelance4u.repository.JobRepository;
import ch.zhaw.freelance4u.service.CompanyService;
import ch.zhaw.freelance4u.service.UserService;

@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    UserService userService;

    @Autowired
    OpenAiChatModel chatModel;

    @PostMapping()
    public ResponseEntity<Job> createJob(@RequestBody JobCreateDTO fDto) {
        if (!userService.userHasRole("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            if (!companyService.existsById(fDto.getCompanyId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // Use AI to improve the job title based on the description
            String improvedTitle = generateImprovedTitle(fDto.getTitle(), fDto.getDescription());

            Job fDAO = new Job(improvedTitle, fDto.getDescription(), fDto.getJobType(), fDto.getEarnings(),
                    fDto.getCompanyId());

            Job savedJob = jobRepository.save(fDAO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String generateImprovedTitle(String currentTitle, String description) {
        String prompt = String.format(
                "Improve the following job title based on the job description. Only return the improved title, nothing else.\n\n" +
                        "Current Title: %s\n" +
                        "Description: %s\n\n" +
                        "Improved Title:",
                currentTitle, description);

        ChatResponse response = chatModel.call(new Prompt(prompt));
        return response.getResult().getOutput().getContent().trim();
    }

    @GetMapping()
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) JobType type,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<Job> allJobs;

        if (min == null && type == null) {
            allJobs = jobRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        } else {
            if (min != null && type != null) {
                allJobs = jobRepository.findByJobTypeAndEarningsGreaterThan(type, min,
                        PageRequest.of(pageNumber - 1, pageSize));
            } else if (min != null) {
                allJobs = jobRepository.findByEarningsGreaterThan(min,
                        PageRequest.of(pageNumber - 1, pageSize));
            } else {
                allJobs = jobRepository.findByJobType(type, PageRequest.of(pageNumber - 1,
                        pageSize));
            }
        }

        return new ResponseEntity<>(allJobs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getCompanyById(@PathVariable String id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            return ResponseEntity.ok(job.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable String id) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        jobRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("DELETED");
    }

}
