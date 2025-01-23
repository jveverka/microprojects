package one.microproject.auth.controller;

import one.microproject.auth.dto.ServerResponse;
import one.microproject.auth.dto.UserData;
import one.microproject.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = URLs.USERS_GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServerResponse<List<UserData>>> getAll() {
        return ResponseEntity.ok(ServerResponse.ok(userService.getAll()));
    }

}
