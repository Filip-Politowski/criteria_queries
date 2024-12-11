package pl.filip_politowski.criteria_query.model;

import lombok.Data;
import org.springframework.data.domain.Sort;
@Data
public class EmployeePage {
    private int page = 0;
    private int size = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "firstName";
}
