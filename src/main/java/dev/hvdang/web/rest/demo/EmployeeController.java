package dev.hvdang.web.rest.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.hvdang.web.rest.demo.EmployeeController.CONTEXT_URL_API_NAME;
import static dev.hvdang.web.sbwebbase.SBWEB_CONFIG.CONTEXT_URL_REST_API;


@RestController
@RequestMapping(value = CONTEXT_URL_API_NAME)
public class EmployeeController
{
  public static final String CONTEXT_URL_API_NAME = CONTEXT_URL_REST_API + "/demo";

  private final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository) {
    this.repository = repository;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/employees")
  List<Employee> all() {
    return repository.findAll();
  }
  // end::get-aggregate-root[]

  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  // Single item

  @GetMapping("/employees/{id}")
  Employee one(@PathVariable Long id) {

    return repository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    return repository.findById(id)
        .map(employee -> {
          employee.setName(newEmployee.getName());
          employee.setRole(newEmployee.getRole());
          return repository.save(employee);
        })
        .orElseGet(() -> {
          newEmployee.setId(id);
          return repository.save(newEmployee);
        });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}