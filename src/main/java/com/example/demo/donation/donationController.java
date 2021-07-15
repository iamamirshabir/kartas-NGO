package com.example.demo.donation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/donation")
public class donationController {
	
	@Autowired
	private final donationrepo repository;	
	
	donationController(donationrepo repository) {
		    this.repository = repository;
		   
		  }
	  @CrossOrigin(origins = "http://localhost:3000") 
	  @GetMapping("/")
	  public
	  List<Donation> all(){
		  List<Donation> donation = repository.findAll();
		  return donation;
	  }
	  
	  @CrossOrigin(origins = "http://localhost:8089") 
	  @GetMapping("/{id}")
	  public
	  ResponseEntity<?> getById(@RequestParam(name = "id") Long id){
		  Optional<Donation> optionalUser = repository.findById(id);
		  if (!optionalUser.isPresent()) {
	            return ResponseEntity.unprocessableEntity().build();
	        }
		  return ResponseEntity.ok().body((optionalUser.get()));
	   }
	  
	  @CrossOrigin(origins = "http://localhost:8089") 
	  @PostMapping("/")
	  ResponseEntity<?> newUser(@RequestBody Donation newUser ) {
		  Donation donation = repository.save(newUser);
		  return ResponseEntity.ok(donation);
	  }
	  
	  @PutMapping("/{id}")
	  ResponseEntity<?> replaceDonor(@RequestBody Donation newDonation, @PathVariable Long id) {
		  Donation updatedUser = repository.findById(id)
				  .map(donation ->{
					  donation.setD_amount(newDonation.getD_amount());
					  donation.setD_onday(newDonation.getD_onday());					 
					  donation.setDonor(newDonation.getDonor());
					  return repository.save(donation);
				  })
				  .orElseGet(() ->{
					  newDonation.setDonation_id(id);
					  return repository.save(newDonation);
				  });
		  
		  return ResponseEntity.ok(updatedUser);
				  
					 
	  }
	  
	  @DeleteMapping("/{id}")
	  void deleteStudent(@PathVariable Long id) {
	    repository.deleteById(id);
	  }
	  

}

	