package com.watermelon.RESTServices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(
            EmployeeRepository employeeRepository,
            EmployeeModelAssembler employeeModelAssembler
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = this.employeeRepository.findAll().stream()
                .map(employee -> employeeModelAssembler.toModel(employee))
                .collect(Collectors.toList());

        return CollectionModel.of(
                employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
        );
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return this.employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeModelAssembler.toModel(employee);
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
