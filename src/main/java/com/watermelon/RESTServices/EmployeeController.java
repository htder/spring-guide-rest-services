package com.watermelon.RESTServices;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    List<Employee> all() {
        return this.employeeRepository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return this.employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {
        return this.employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException(id)
        );
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(
            @RequestBody Employee newEmployee,
            @PathVariable Long id
    ) {
        return this.employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return this.employeeRepository.save(employee);
                }).orElseGet(() -> {
                    newEmployee.setId(id);
                    return this.employeeRepository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        this.employeeRepository.deleteById(id);
    }
}
