package ch.zhaw.freelance4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobStateAggregationDTO;
import ch.zhaw.freelance4u.model.JobStateChangeDTO;
import ch.zhaw.freelance4u.repository.JobRepository;
import ch.zhaw.freelance4u.service.JobService;
import ch.zhaw.freelance4u.service.UserService;

@RestController
@RequestMapping("/api/service")
public class JobServiceController {
    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserService userService;

    @PutMapping("/assignJob")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO dto) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String freelancerId = dto.getFreelancerId();
        String jobId = dto.getJobId();
        Optional<Job> job = jobService.assignJob(jobId, freelancerId);

        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/completeJob")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO dto) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String freelancerId = dto.getFreelancerId();
        String jobId = dto.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerId);

        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.assignJob(jobId, userEmail);

        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/completejob")
    public ResponseEntity<Job> completeMyJob(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.completeJob(jobId, userEmail);

        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/jobDashboard")
    public ResponseEntity<List<JobStateAggregationDTO>> getDashboard(@RequestParam String companyId) {
        return ResponseEntity.ok(jobRepository.getJobStateAggregation(companyId));
    }

}