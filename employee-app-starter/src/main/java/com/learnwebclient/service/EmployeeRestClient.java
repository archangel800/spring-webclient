package com.learnwebclient.service;

import com.learnwebclient.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.learnwebclient.constants.EmployeeConstants.*;

@Slf4j
public class EmployeeRestClient {

    private WebClient webClient;

    public EmployeeRestClient(WebClient webClient) {
        this.webClient = webClient;
    }
    //http://localhost:8081/employeeservice/v1/allEmployees
    public List<Employee> retrieveAllEmployees() {
        return webClient.get().uri(GET_ALL_EMPLOYEES_V1)
                .retrieve()
                .bodyToFlux(Employee.class)
                .collectList()
                .block();
    }
    public Employee retrieveEmployeeById(int employeeId) {
        try {
            return webClient.get().uri(GET_EMPLOYEE_BY_ID_V1, employeeId)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        } catch (WebClientResponseException webClientException) {
            log.error("Error Response Code is {} and the response body is {}", webClientException.getRawStatusCode(), webClientException.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById {}", webClientException);
            throw webClientException;
        } catch (Exception exception) {
            log.error("Exception in retrieveEmployeeId ", exception);
            throw exception;
        }
    }

    public List<Employee> retrieveEmployeeByName(String empName) {
        String employee_name = UriComponentsBuilder.fromUriString(GET_EMPLOYEE_BY_NAME_V1)
                .queryParam("employee_name", empName).toUriString();
        try{
            return webClient.get().uri(employee_name)
                    .retrieve()
                    .bodyToFlux(Employee.class)
                    .collectList()
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error response code is {} and Response body is {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeByName {}", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in retrieveEmployeeByName {}", ex);
            throw ex;
        }

    }
    public Employee addNewEmployee(Employee employee) {
        try {
            return webClient.post().uri(ADD_EMPLOYEE_V1)
                    .syncBody(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error response code is {} and Response body is {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in addNewEmployee {}", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in addNewEmployee {}", ex);
            throw ex;
        }
    }
    public Employee updateEmployee(int employeeId, Employee employee) {
        try {
            return webClient.put().uri(UPDATE_EMPLOYEE_V1, employeeId)
                    .syncBody(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        }
        catch (WebClientResponseException exception) {
            log.error("Error response code is {} and Response body is {}", exception.getRawStatusCode(), exception.getResponseBodyAsString());
            log.error("WebClientResponseException in updateEmployee {}", exception);
            throw exception;
        }
        catch (Exception exception) {
            log.error("Exception in updateEmployee {}", exception);
            throw exception;
        }
    }
    public String deleteEmployee(int employeeId) {
        try {
            return webClient.delete().uri(GET_EMPLOYEE_BY_ID_V1, employeeId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (WebClientResponseException exception) {
            log.error("Error response code is {} and Response body is {}", exception.getRawStatusCode(), exception.getResponseBodyAsString());
            log.error("WebClientResponseException in deleteEmployee {}", exception);
            throw exception;
        }
        catch (Exception exception) {
            log.error("Exception in deleteEmployee {}", exception);
            throw exception;
        }
    }
}
