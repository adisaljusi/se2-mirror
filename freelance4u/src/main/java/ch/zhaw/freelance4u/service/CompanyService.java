package ch.zhaw.freelance4u.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.freelance4u.model.Company;
import ch.zhaw.freelance4u.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public boolean existsById(String id) {
        return companyRepository.existsById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}
