package pl.filip_politowski.criteria_query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.filip_politowski.criteria_query.dao.EmployeeSearchRequest;
import pl.filip_politowski.criteria_query.model.Employee;
import pl.filip_politowski.criteria_query.model.EmployeePage;
import pl.filip_politowski.criteria_query.repositorey.EmployeeRepository;
import pl.filip_politowski.criteria_query.repositorey.EmployeeSearchDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeSearchDao employeeSearchDao;
    private final EmployeeRepository employeeRepository;

    public List<Employee> getEmployeesFromPredicates(EmployeeSearchRequest employeeSearchRequest) {
        return employeeSearchDao.findAllByCriteriaQuery(employeeSearchRequest);
    }

    public Page<Employee> getEmployeesFromPredicatesPage(EmployeePage employeePage, EmployeeSearchRequest employeeSearchRequest) {
        return employeeSearchDao.findAllByCriteriaQueryPage(employeePage, employeeSearchRequest);
    }

    public List<Employee> findAllEmployee() {
        return employeeRepository.findAll();
    }
}
