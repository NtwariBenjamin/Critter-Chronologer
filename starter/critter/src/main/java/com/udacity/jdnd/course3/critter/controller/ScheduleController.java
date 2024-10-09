package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    PetService petService;
    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
       Schedule schedule=new Schedule(scheduleDTO.getDate(),scheduleDTO.getActivities());
       ScheduleDTO scheduleToScheduleDTO;
       try {
          scheduleToScheduleDTO=convertScheduleToScheduleDTO(scheduleService.createSchedule(schedule,scheduleDTO.getEmployeeIds(),scheduleDTO.getPetIds()));
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not create schedule!",e);
       }
       return scheduleToScheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedule=scheduleService.getAllSchedules();
        return schedule.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules;
        try {
            schedules=scheduleService.getScheduleForPet(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find schedule for pet:"+petId,e);
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());

    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
       List<Schedule> schedules;
       try {
           schedules=scheduleService.getScheduleForEmployee(employeeId);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find Employee schedule with Id:"+ employeeId,e);
       }
       return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules;
        try {
            schedules=scheduleService.getScheduleForCustomer(customerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find schedule for Pet with Customer id:"+customerId,e);
        }
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }
    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule){
        List<Long> employeeIds = schedule.getEmployee().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        ScheduleDTO scheduleDTO=new ScheduleDTO();
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        BeanUtils.copyProperties(schedule,scheduleDTO);
        return scheduleDTO;
    }
}
