package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PetRepository petRepository;

    public Customer saveCustomer(Customer customer, List<Long> petIds) {
       List<Pet> allPetsForCustomer=new ArrayList<>();
       if (petIds!=null && !petIds.isEmpty()){
           allPetsForCustomer=petIds.stream()
                   .map((petId)->petRepository.getOne(petId))
                   .collect(Collectors.toList());
       }
       customer.setPets(allPetsForCustomer);
       return customerRepository.save(customer);
    }


    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomerByPetId(long petId) {
        return petRepository.getOne(petId).getCustomer();
    }
}
