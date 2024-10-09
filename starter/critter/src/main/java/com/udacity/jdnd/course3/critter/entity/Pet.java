package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.data.pet.PetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    private PetType type;
    @Nationalized
    private String name;
    @ManyToOne(targetEntity = Customer.class, optional = false)
    private Customer customer;
    private LocalDate birthDate;
    private String notes;

    public Pet(PetType type, String name, LocalDate birthDate, String notes) {
        this.type = type;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }
}
