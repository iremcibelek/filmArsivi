package com.example.filmArsivi.Repository;

import com.example.filmArsivi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Bu importu sakın unutma!

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Geri dönüş tipini Optional yaptık, böylece orElseThrow çalışabilecek
    Optional<User> findByUsername(String username);
}