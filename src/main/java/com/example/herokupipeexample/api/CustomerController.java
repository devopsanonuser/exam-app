package com.example.herokupipeexample.api;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.example.herokupipeexample.modal.Customer;
import com.example.herokupipeexample.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;

@RestController
public class CustomerController {

    private CustomerRepository customerRepository;
    private final MetricRegistry registry;
    private final Timer timer;
    private final Counter counter;
    private Logger logger;

    public CustomerController(CustomerRepository customerRepository, MetricRegistry registry) {
        this.customerRepository = customerRepository;
        this.registry = registry;
        this.timer = registry.timer(name(Customer.class, "responses"));
        this.counter = registry.counter(name(Customer.class, "jobs"));
        this.logger = LoggerFactory.getLogger(CustomerController.class);
    }

    @RequestMapping("/")
    public String welcome() {
        registry.meter("GET/").mark();
        counter.inc();

        Marker marker = MarkerFactory.getMarker("Info");
        logger.info(marker, "Request to / in customer api");

        return "Welcome to this small REST. It will accept a GET on /list with a request parameter lastName, and a POST to / with a JSON payload with firstName and lastName as values.";
    }

    @RequestMapping("/list")
    public List<Customer> find(@RequestParam(value="lastName") String lastName) {
        registry.meter("GET/list").mark();
        counter.inc();
        final Timer.Context context = timer.time();

        try {
            return customerRepository.findByLastName(lastName);
        } finally {
            logger.info("Request to /list in customer api");
            context.stop();
        }
    }

    @PostMapping("/")
    Customer newCustomer(@RequestBody Customer customer) {
        registry.meter("POST/").mark();
        counter.inc();
        logger.info("Request to POST new customer in customer api");

        return customerRepository.save(customer);
    }
}
