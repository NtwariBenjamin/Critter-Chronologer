package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee=employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }


    public List<Employee> findEmployeeByService(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> employees=employeeRepository.findByDaysAvailable(date.getDayOfWeek()).stream()
              .filter(employee -> employee.getSkills().containsAll(skills))
              .collect(Collectors.toList());
      return employees;
    }
}
