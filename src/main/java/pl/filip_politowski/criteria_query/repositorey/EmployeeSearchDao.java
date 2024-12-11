package pl.filip_politowski.criteria_query.repositorey;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import pl.filip_politowski.criteria_query.dao.EmployeeSearchRequest;
import pl.filip_politowski.criteria_query.model.Employee;
import pl.filip_politowski.criteria_query.model.EmployeePage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository

public class EmployeeSearchDao {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public EmployeeSearchDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }


    public List<Employee> findAllByCriteriaQuery(EmployeeSearchRequest employeeSearchRequest) {
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<Employee> root = criteriaQuery.from(Employee.class);
        if (employeeSearchRequest.getFirstName() != null) {
            Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + employeeSearchRequest.getFirstName() + "%");
            predicates.add(firstNamePredicate);
        }
        if (employeeSearchRequest.getLastName() != null) {
            Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + employeeSearchRequest.getLastName() + "%");
            predicates.add(lastNamePredicate);
        }
        if (employeeSearchRequest.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + employeeSearchRequest.getEmail() + "%");
            predicates.add(emailPredicate);
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    public Page<Employee> findAllByCriteriaQueryPage(EmployeePage employeePage, EmployeeSearchRequest employeeSearchRequest) {

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate predicate = getPredicate(employeeSearchRequest, root);

        criteriaQuery.where(predicate);
        setOrder(employeePage, criteriaQuery, root);

        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeePage.getPage() * employeePage.getSize());
        typedQuery.setMaxResults(employeePage.getSize());

        Pageable pageable = getPageable(employeePage);

        long employeesCount = getEmployeesCount(employeeSearchRequest);
        System.out.println(employeesCount);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private Predicate getPredicate(EmployeeSearchRequest employeeSearchRequest, Root<Employee> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(employeeSearchRequest.getFirstName())) {
            predicates.add(
                    criteriaBuilder.like(root.get("firstName"), "%" + employeeSearchRequest.getFirstName() + "%")
            );
        }
        if (Objects.nonNull(employeeSearchRequest.getLastName())) {
            predicates.add(
                    criteriaBuilder.like(root.get("lastName"), "%" + employeeSearchRequest.getLastName() + "%")
            );
        }
        if (Objects.nonNull(employeeSearchRequest.getEmail())) {
            predicates.add(
                    criteriaBuilder.like(root.get("email"), "%" + employeeSearchRequest.getEmail() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(EmployeePage employeePage, CriteriaQuery<Employee> criteriaQuery, Root<Employee> root) {
        if (employeePage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(employeePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(employeePage.getSortBy())));
        }
    }

    private Pageable getPageable(EmployeePage employeePage) {
        Sort sort = Sort.by(employeePage.getSortBy(), employeePage.getSortBy());
        return PageRequest.of(employeePage.getPage(), employeePage.getSize(), sort);
    }

    private long getEmployeesCount(EmployeeSearchRequest employeeSearchRequest) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        Predicate predicate = getPredicate(employeeSearchRequest, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
