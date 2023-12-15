package one.microproject.auth.service.impl;

import one.microproject.auth.dto.UserData;
import one.microproject.auth.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Map<String, UserData> users = new HashMap();

    public UserServiceImpl() {
        users.put("juraj", new UserData("juraj", "secret"));
    }

    @Override
    public List<UserData> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<UserData> get(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public Optional<UserData> delete(String username) {
        return Optional.ofNullable(users.remove(username));
    }

    @Override
    public void save(UserData userData) {
        users.put(userData.userName(), userData);
    }

}
