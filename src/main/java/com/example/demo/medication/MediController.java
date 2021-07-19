package com.example.demo.medication;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(value = "/api/medication")
public class MediController {
	
	@Autowired
	private final mediRepo repository;
	
	MediController(mediRepo repository) {
		    this.repository = repository;
		   
		  }
	  @CrossOrigin(origins = "http://localhost:3000") 
	  @GetMapping("/")
	  @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
	  
	  public
	  List<Medication> all(){
		  List<Medication> medi = repository.findAll();
		  return medi;
	  }
	  
	  @CrossOrigin(origins = "http://localhost:8089") 
	  @GetMapping("/{id}")
	  @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
	  
	  public
	  ResponseEntity<?> getById(@RequestParam(name = "id") Long id){
		  Optional<Medication> optionalUser = repository.findById(id);
		  if (!optionalUser.isPresent()) {
	            return ResponseEntity.unprocessableEntity().build();
	        }
		  return ResponseEntity.ok().body((optionalUser.get()));
	   }
	  
	  @CrossOrigin(origins = "http://localhost:3000") 
	  @PostMapping("/")
	  @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
	  
	  ResponseEntity<?> newUser(@RequestBody Medication newUser ) {
		  Medication medi = repository.save(newUser);
		  return ResponseEntity.ok(medi);
	  }
	  
	  @PutMapping("/{id}")
	  @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
	  
	  ResponseEntity<?> replaceMedication(@RequestBody Medication newMedication, @PathVariable Long id) {
		  Medication updatedUser = repository.findById(id)
				  .map(medi ->{
					  medi.setAmount(newMedication.getAmount());
					  medi.setEligible(false);
					 
					  return repository.save(medi);
				  })
				  .orElseGet(() ->{
					  newMedication.setId(id);
					  return repository.save(newMedication);
				  });
		  
		  return ResponseEntity.ok(updatedUser);
				  
					 
	  }
	  
	  @PutMapping("/grant/{id}")
	  @PreAuthorize("hasRole('ADMIN')")
	  
	  ResponseEntity<?> grantMedication(@RequestBody Medication newMedication, @PathVariable Long id) {
		  Medication updatedUser = repository.findById(id)
				  .map(medi ->{
					  medi.setEligible(true);
					 
					  return repository.save(medi);
				  })
				  .orElseGet(() ->{
					  newMedication.setId(id);
					  return repository.save(newMedication);
				  });
		  
		  return ResponseEntity.ok(updatedUser);
				  
					 
	  }
	  
	  @DeleteMapping("/{id}")
	  @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
	  
	  void deleteStudent(@PathVariable Long id) {
	    repository.deleteById(id);
	  }
	  

}
