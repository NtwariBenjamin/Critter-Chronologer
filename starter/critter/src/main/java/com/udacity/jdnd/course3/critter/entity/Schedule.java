package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.data.user.EmployeeSkill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToMany(targetEntity = Employee.class)
    private List<Employee> employee;
    @ManyToMany(targetEntity = Pet.class)
    private List<Pet> pets;
    private LocalDate date;
    @ElementCollection
    private Set<EmployeeSkill> activities;

    public Schedule(LocalDate date, Set<EmployeeSkill> activities) {
        this.date = date;
        this.activities = activities;
    }
}
