package pl.filip_politowski.criteria_query.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSearchRequest {
    private String firstName;
    private String lastName;
    private String email;
}
