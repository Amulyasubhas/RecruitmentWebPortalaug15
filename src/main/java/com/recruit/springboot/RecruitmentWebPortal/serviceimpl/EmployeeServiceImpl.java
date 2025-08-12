


package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

import com.recruit.springboot.RecruitmentWebPortal.DTO.CreateUserDTO;
import com.recruit.springboot.RecruitmentWebPortal.DTO.EmployeeDTO;
import com.recruit.springboot.RecruitmentWebPortal.entity.Employee;
import com.recruit.springboot.RecruitmentWebPortal.entity.Role;
import com.recruit.springboot.RecruitmentWebPortal.repository.EmployeeRepository;
import com.recruit.springboot.RecruitmentWebPortal.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create user
    @Override
    public String createUser(CreateUserDTO dto) {
        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            return "User with this email already exists!";
        }

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());

        // Encode password before saving
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            employee.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return "Invalid role. Allowed values: ADMIN, RECRUITER, HR";
        }

        employeeRepository.save(employee);
        return "User created successfully!";
    }

    // Update user
    @Override
    public String updateUser(Long id, CreateUserDTO dto) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setName(dto.getName());
            emp.setEmail(dto.getEmail());

            // Encode password before updating
            emp.setPassword(passwordEncoder.encode(dto.getPassword()));

            try {
                emp.setRole(Role.valueOf(dto.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return "Invalid role. Allowed values: ADMIN, RECRUITER, HR";
            }

            employeeRepository.save(emp);
            return "User updated successfully!";
        }).orElse("User not found!");
    }

    // Delete user
    @Override
    public String deleteUser(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "User deleted successfully!";
        }
        return "User not found!";
    }

    // Add employee (basic)
    @Override
    public String addEmployee(EmployeeDTO dto) {
        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            return "Employee with this email already exists!";
        }

        Employee emp = new Employee();
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        employeeRepository.save(emp);
        return "Employee added successfully!";
    }

    // Get all employees
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    // Update employee (basic)
    @Override
    public String updateEmployee(Long id, EmployeeDTO dto) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setName(dto.getName());
            emp.setEmail(dto.getEmail());
            employeeRepository.save(emp);
            return "Employee updated successfully!";
        }).orElse("Employee not found!");
    }

    // Delete employee
    @Override
    public String deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "Employee deleted successfully!";
        }
        return "Employee not found!";
    }
}
