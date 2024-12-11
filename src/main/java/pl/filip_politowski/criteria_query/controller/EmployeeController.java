package pl.filip_politowski.criteria_query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.filip_politowski.criteria_query.dao.EmployeeSearchRequest;
import pl.filip_politowski.criteria_query.model.Employee;
import pl.filip_politowski.criteria_query.model.EmployeePage;
import pl.filip_politowski.criteria_query.service.EmployeeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/criteria")
    public List<Employee> getSearchEmployees(EmployeeSearchRequest employeeSearchRequest) {
        return employeeService.getEmployeesFromPredicates(employeeSearchRequest);
    }

    @GetMapping("/criteria/page")
    public ResponseEntity<Page<Employee>> getEmployees(EmployeePage employeePage,
                                                       EmployeeSearchRequest employeeSearchRequest) {
        return new ResponseEntity<>(employeeService.getEmployeesFromPredicatesPage(employeePage, employeeSearchRequest),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployee();
    }
}
