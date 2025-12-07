package ch.zhaw.freelance4u.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelance4u.model.Company;
import ch.zhaw.freelance4u.model.CompanyCreateDTO;
import ch.zhaw.freelance4u.model.MailInformation;
import ch.zhaw.freelance4u.repository.CompanyRepository;
import ch.zhaw.freelance4u.service.MailValidatorService;
import ch.zhaw.freelance4u.service.UserService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Autowired
    MailValidatorService mailValidatorService;

    @PostMapping()
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateDTO fDto) {
        try {
            MailInformation mailInfo = mailValidatorService.validateEmail(fDto.getEmail()).block();

            if (!mailValidatorService.isEmailValid(mailInfo)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Company fDAO = new Company(fDto.getName(), fDto.getEmail());
            Company savedCompany = companyRepository.save(fDAO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    public ResponseEntity<Page<Company>> getAllCompanies(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        if (!userService.userHasRole("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pageable pageable;
        if (pageNumber == null || pageSize == null) {
            // When no pagination params provided, return everything
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }

        Page<Company> companies = companyRepository.findAll(pageable);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        if (!userService.userHasRole("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok(company.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
