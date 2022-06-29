package com.watermelon.RESTServices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel =
                this.employeeModelAssembler.toModel(
                        this.employeeRepository.save(newEmployee)
                );
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeModelAssembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(
            @RequestBody Employee newEmployee,
            @PathVariable Long id
    ) {

        Employee updatedEmployee = this.employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return this.employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return this.employeeRepository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = this.employeeModelAssembler.toModel(updatedEmployee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        this.employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
