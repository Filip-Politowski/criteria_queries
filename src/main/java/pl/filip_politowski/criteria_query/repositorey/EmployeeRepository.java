package pl.filip_politowski.criteria_query.repositorey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filip_politowski.criteria_query.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
