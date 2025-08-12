package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

import com.recruit.springboot.RecruitmentWebPortal.entity.Employee;
import com.recruit.springboot.RecruitmentWebPortal.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PasswordEncodingRunner implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            String raw = emp.getPassword();
            if (raw != null && !raw.startsWith("$2a$")) { // Not encoded
                emp.setPassword(passwordEncoder.encode(raw));
                employeeRepository.save(emp);
                System.out.println("Encoded: " + emp.getEmail());
            }
        }
    }
}
