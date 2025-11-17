package ch.zhaw.freelance4u.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import ch.zhaw.freelance4u.repository.CompanyRepository;
import ch.zhaw.freelance4u.service.UserService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateDTO fDto) {
        try {
            Company fDAO = new Company(fDto.getName(), fDto.getEmail());
            Company savedCompany = companyRepository.save(fDAO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    public ResponseEntity<Page<Company>> getAllCompanies(
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        if (!userService.userHasRole("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Page<Company> companies = companyRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
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
