package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.data.user.EmployeeSkill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.DayOfWeek;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Nationalized
    private String name;
    @ElementCollection
    private Set<EmployeeSkill> skills;
    @ElementCollection
    private Set<DayOfWeek> daysAvailable;
}
