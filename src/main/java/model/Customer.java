package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dob;
    private String phoneNumber;
}
