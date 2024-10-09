package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.data.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.data.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer=new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        List<Long> petIds=customerDTO.getPetIds();
        CustomerDTO customerToCustomerDTO;
        try {
            customerToCustomerDTO=convertCustomerToCustomerDTO(customerService.saveCustomer(customer,petIds));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failed to Save Customer",e);
        }
        return customerToCustomerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
       List<Customer> customers= customerService.getCustomers();
       return customers.stream().map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer;
        try {
            customer=customerService.getCustomerByPetId(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find Customer by Pet Id:"+petId,e);
        }
        return convertCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee=new Employee(employeeDTO.getId(),employeeDTO.getName(),employeeDTO.getSkills(),employeeDTO.getDaysAvailable());
        EmployeeDTO employeeToEmployeeDTO;
        try {
            employeeToEmployeeDTO=convertEmployeeToEmployeeDTO(employeeService.saveEmployee(employee));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could Not Save Employee",e);
        }
        return employeeToEmployeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
     Employee employee;
     try {
         employee=employeeService.getEmployee(employeeId);
     }catch (Exception e){
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee with Id:"+employeeId+" couldn't be found"+e);
     }
     return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.setAvailability(daysAvailable,employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't set Availability for Employee:"+employeeId,e);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
       List<Employee> employees=employeeService.findEmployeeByService(employeeDTO.getDate(),employeeDTO.getSkills());
       return employees.stream().map(this::convertEmployeeToEmployeeDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer){
        List<Long> petIds = customer.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setPetIds(petIds);
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO=new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }

}
