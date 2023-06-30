package com.learnwebclient.service;

import com.learnwebclient.dto.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.lang.reflect.Executable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRestClientTest {

    private static final String baseUrl
            = "http://localhost:8081/employeeservice";

    private WebClient webClient = WebClient.create(baseUrl);

    EmployeeRestClient employeeRestClient = new EmployeeRestClient(webClient);

    @Test
    void retrieveAllEmployees() {
       List<Employee> employees = employeeRestClient.retrieveAllEmployees();
        System.out.println("employeeList: " + employees);
       assertTrue(employees.size() > 0);

    }

    @Test
    void getEmployeeById() {
        Employee employee = employeeRestClient.retrieveEmployeeById(1);
        System.out.println("Employee: " + employee);
        assertEquals("Chris", employee.getFirstName());
        assertNotNull(employee);
    }

    @Test
    void getEmployeeById_NotFound() {
        int empId = 10;
        Assertions.assertThrows(WebClientException.class, () -> employeeRestClient.retrieveEmployeeById(empId));
    }
    @Test
    void getEmployeesByName() {
        List<Employee> employees = employeeRestClient.retrieveEmployeeByName("Chris");
        Assertions.assertEquals(employees.size(), 1);
        Assertions.assertTrue(employees.size() > 0);
        Assertions.assertEquals(employees.get(0).getFirstName(), "Chris");
    }

    @Test
    void getEmployeesByName_NotFound() {
        Assertions.assertThrows(WebClientException.class, () -> employeeRestClient.retrieveEmployeeByName("Chriss"));
    }
    @Test
    void addEmployee() {
        Employee employee = Employee.builder()
                .age(21)
                .gender("female")
                .firstName("Nuca")
                .lastName("Macharidze")
                .role("Backend Developer")
                .build();
        Employee emp = employeeRestClient.addNewEmployee(employee);

        Assertions.assertEquals(emp.getFirstName(), employee.getFirstName());

        Assertions.assertTrue(emp.getId() != null);
    }

    @Test
    void addEmployee_thenReturn400BadRequest() {

        Employee employee = Employee.builder()
                .age(21)
                .gender("female")
                .firstName(null)
                .lastName("Macharidze")
                .role("Backend Developer")
                .build();
        Assertions.assertThrows(WebClientException.class, () -> employeeRestClient.addNewEmployee(employee));
    }

    @Test
    public void updateEmployee() {
        Employee employee = Employee.builder()
                .age(20)
                .firstName("Chris")
                .lastName("Evans")
                .gender("male")
                .role("Backend Dev")
                .build();
        Employee employee1 = employeeRestClient.updateEmployee(1, employee);
        Assertions.assertEquals(employee1.getRole(), employee.getRole());
    }
    @Test
    public void updateEmployee_400BadRequest() {
        Employee employee = Employee.builder()
                .age(20)
                .lastName("Evans")
                .firstName("Chris")
                .gender("male")
                .role("Backend Dev")
                .build();
        Assertions.assertThrows(WebClientException.class, () -> employeeRestClient.updateEmployee(199, employee));
    }

    @Test
    public void deleteEmployee() {
        Employee employee = Employee.builder()
                .age(21)
                .gender("female")
                .firstName("Nuca")
                .lastName("Macharidze")
                .role("Backend Developer")
                .build();
        Employee emp = employeeRestClient.addNewEmployee(employee);
        Assertions.assertEquals(emp.getFirstName(), employee.getFirstName());
        Assertions.assertTrue(emp.getId() != null);

        String message = employeeRestClient.deleteEmployee(emp.getId());
        Assertions.assertEquals(message, "Employee deleted successfully.");


    }
}
