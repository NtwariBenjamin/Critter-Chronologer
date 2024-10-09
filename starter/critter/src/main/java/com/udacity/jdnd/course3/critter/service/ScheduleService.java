package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.data.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
       List<Pet> pets=petRepository.findAllById(petIds);
       List<Employee> employees=employeeRepository.findAllById(employeeIds);
       schedule.setPets(pets);
       schedule.setEmployee(employees);
       return scheduleRepository.save(schedule);
    }


    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
       Pet pet=petRepository.getOne(petId);
        List<Schedule> schedules=scheduleRepository.findByPets(pet);
        return schedules;
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Employee employee=employeeRepository.getOne(employeeId);
        List<Schedule> schedules=scheduleRepository.findByEmployee(employee);
        return schedules;
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer=customerRepository.getOne(customerId);
        List<Schedule> schedules=scheduleRepository.findByPetsIn(customer.getPets());
        return schedules;
    }
}
