package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.User;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void add(Long ID);

    default void addAll(List<Long> IDs) {
        IDs.forEach(this::add);
    }

    Optional<User> find(Long ID);

    List<User> findAll();

    default List<User> findAll(List<Long> IDs) {
        List<User> users = new LinkedList<>();
        IDs.forEach(id -> find(id).ifPresent(users::add));
        return users;
    }

    void remove(Long ID);

    default void removeUser(User user) {
        remove(user.ID());
    }

    default void removeAll(List<Long> IDs) {
        IDs.forEach(this::remove);
    }

    default void removeAllUsers(List<User> users) {
        removeAll(users.stream().map(User::ID).toList());
    }

    void dropTable();
}
