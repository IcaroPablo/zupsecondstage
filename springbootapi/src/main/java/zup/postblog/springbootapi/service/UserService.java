package zup.postblog.springbootapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import zup.postblog.springbootapi.exception.ConstraintException;
import zup.postblog.springbootapi.exception.ResourceNotFoundException;
import zup.postblog.springbootapi.model.Address;
import zup.postblog.springbootapi.model.User;
import zup.postblog.springbootapi.model.UserAndAddresses;
import zup.postblog.springbootapi.repository.UserRepository;
import zup.postblog.springbootapi.repository.AddressRepository;

@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository;
	
    public UserAndAddresses getUserInformationById(Long userId) throws ResourceNotFoundException {
    	User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("There is no User for the id " + userId));
        List<Address> addresses = addressRepository.findByUserId(userId);
        UserAndAddresses userdata = new UserAndAddresses(user, addresses);
        
        return userdata;
    }
    
    public User createUser(User user) throws DataIntegrityViolationException, Exception{
    	try {
    		return userRepository.save(user);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error :( but i know some details that could help: " + e.getMessage());
    	}
    }
    
    public Address createAddress(Long userId, Address address) throws ResourceNotFoundException, DataIntegrityViolationException, Exception{
    	User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("There is no User for this id: " + userId));
    	address.setUser(user);
    	
    	try {
    		return addressRepository.save(address);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error :( but i know some details that could help: " + e.getMessage());
    	}
    }
}
