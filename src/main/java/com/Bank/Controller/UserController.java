package com.Bank.Controller;


import com.Bank.Dto.RegisterRequest;
import com.Bank.Model.User;
import com.Bank.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            // Create a new User
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());

            User registeredUser = userService.Register(user);

            return new ResponseEntity<>("User registered successfully with profile ID: " + registeredUser.getId(), HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // Gracefully catch our custom Service throw validation rules (like duplicate names)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
