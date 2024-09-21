package org.mma.training.java.spring.controller;

import java.util.List;
import java.util.Optional;

import org.mma.training.java.spring.model.User;
import org.mma.training.java.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	UserRepository userRepository;
	@GetMapping("/users")

	public ResponseEntity<List<User>>getUsers(){
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable long id) {

		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/users/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		return new ResponseEntity<User>(userRepository.save(user), HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
		return userRepository.findById(id)
				.map(user -> {
					user.setFirstName(userDetails.getFirstName());
					user.setLastName(userDetails.getLastName());
					user.setEmail(userDetails.getEmail()); 
					// Update other fields as necessary
					userRepository.save(user);
					return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		return userRepository.findById(id)
				.map(user -> {
					userRepository.delete(user);
					return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
				})
				.orElseGet(() -> new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}


/*     
    @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userRepository.delete(user);
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
        }
    }
 */


