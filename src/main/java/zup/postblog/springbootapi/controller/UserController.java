package zup.postblog.springbootapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import zup.postblog.springbootapi.exception.ConstraintException;
import zup.postblog.springbootapi.exception.ResourceNotFoundException;
import zup.postblog.springbootapi.model.Address;
import zup.postblog.springbootapi.model.User;
import zup.postblog.springbootapi.model.UserAndAddresses;
import zup.postblog.springbootapi.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService service;
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserAndAddresses> retrieveUserData(@PathVariable(value = "id") Long userId)
    		throws ResourceNotFoundException {
        UserAndAddresses userdata = service.getUserInformationById(userId);

        return ResponseEntity.ok().body(userdata);
    }
    
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@Validated @RequestBody User user, BindingResult br)
    		throws DataIntegrityViolationException, Exception{
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createUser(user);
    }
    
    @PostMapping("/users/address/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Address createAddress(@PathVariable(value = "id") Long userId, @Validated @RequestBody Address address, BindingResult br)
    		throws ResourceNotFoundException, DataIntegrityViolationException, Exception{
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createAddress(userId, address);
    }

}