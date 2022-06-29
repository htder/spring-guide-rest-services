package com.watermelon.RESTServices;

import com.watermelon.RESTServices.employee.Employee;
import com.watermelon.RESTServices.employee.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee(
                    "Bilbo",
                    "Baggins",
                    "Burglar"
            )));
            log.info("Preloading " + employeeRepository.save(new Employee(
                    "Frodo",
                    "Baggins",
                    "theif"
            )));
        };
    }
}