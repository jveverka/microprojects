package one.microproject.auth.service;

import one.microproject.auth.dto.UserData;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserData> getAll();

    Optional<UserData> get(String username);

}
