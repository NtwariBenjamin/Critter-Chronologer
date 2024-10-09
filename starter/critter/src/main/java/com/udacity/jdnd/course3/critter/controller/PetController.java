package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.data.pet.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet =new Pet(petDTO.getType(),petDTO.getName(),petDTO.getBirthDate(),petDTO.getNotes());
        PetDTO petToPetDTO;
        try {
            petToPetDTO = convertPetToPetDTO(petService.savePet(pet, petDTO.getOwnerId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failed to save pet",e);
        }

        return petToPetDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
       Pet pet;
       try {
           pet=petService.findPetById(petId);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failed to Retrieve pet By ID",e);
       }
       return convertPetToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets=petService.getAllPets();

        return pets.stream()
                .map(this::convertPetToPetDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try {
            pets = petService.findPetByCustomerId(ownerId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Failed To find Pets of:"+ownerId,e);
        }

        return pets.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }


    private static Pet convertPetDtoToPet(PetDTO petDTO){
        Pet pet=new Pet();
        BeanUtils.copyProperties(petDTO,pet);
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDto = new PetDTO();
        petDto.setOwnerId(pet.getCustomer().getId());
        BeanUtils.copyProperties(pet,petDto);
        return petDto;
    }
}
