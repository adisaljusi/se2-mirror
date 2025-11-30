package ch.zhaw.freelance4u.tools;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.tool.annotation.Tool;

import ch.zhaw.freelance4u.model.Company;
import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobType;
import ch.zhaw.freelance4u.repository.JobRepository;
import ch.zhaw.freelance4u.service.CompanyService;

public class FreelancerTools {
    private JobRepository jobRepository;
    private CompanyService companyService;

    public FreelancerTools(JobRepository jobRepository, CompanyService companyService) {

        this.jobRepository = jobRepository;
        this.companyService = companyService;
    }

    @Tool(description = "Information about the jobs in the database.")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Tool(description = "Information about the companies in the database.")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Tool(description = "Create a new company with the given name and email. Returns the created company.")
    public Company createCompany(String name, String email) {
        return companyService.createCompany(name, email);
    }

    @Tool(description = "Create a new job with the given title, description, jobType (TEST, IMPLEMENT, REVIEW, or OTHER), earnings, and company name. If the company doesn't exist, it will be created with a default email. Returns the created job.")
    public Job createJob(String title, String description, String jobType, double earnings, String companyName) {
        // Find or create company
        Optional<Company> existingCompany = companyService.findCompanyByName(companyName);
        Company company;

        if (existingCompany.isPresent()) {
            company = existingCompany.get();
        } else {
            // Create company with default email if it doesn't exist
            company = companyService.createCompany(companyName, companyName.toLowerCase().replace(" ", "") + "@example.com");
        }

        // Parse jobType
        JobType parsedJobType;
        try {
            parsedJobType = JobType.valueOf(jobType.toUpperCase());
        } catch (IllegalArgumentException e) {
            parsedJobType = JobType.OTHER;
        }

        // Create and save job
        Job job = new Job(title, description, parsedJobType, earnings, company.getId());
        return jobRepository.save(job);
    }
}
