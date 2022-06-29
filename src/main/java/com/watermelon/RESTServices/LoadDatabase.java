package com.watermelon.RESTServices;

import com.watermelon.RESTServices.employee.Employee;
import com.watermelon.RESTServices.employee.EmployeeRepository;
import com.watermelon.RESTServices.order.Order;
import com.watermelon.RESTServices.order.OrderRepository;
import com.watermelon.RESTServices.order.Status;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            EmployeeRepository employeeRepository,
            OrderRepository orderRepository
    ) {
        return args -> {
            employeeRepository.save(new Employee("Bilbo", "Baggins", "Burglar"));
            employeeRepository.save(new Employee("Frodo", "Baggins", "Theif"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("Iphone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> log.info("Preloaded " + order));
        };

    }
}