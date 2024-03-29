package es.sipinformatica.propertymanagement.security.data.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.sipinformatica.propertymanagement.security.data.model.User;
import lombok.NonNull;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone (String phone);
    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);	   
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Boolean existsByDni(String dni);
    Optional<User> findByActivationKey(String token);
    Optional<User> findByUsernameAndActivationKey(@NonNull String username, Object object);
    Optional<User> findByEmailAndActivationKey(@NonNull String email, Object object);
    Optional<User> findByPhoneAndActivationKey(@NonNull String phone, Object object);
    Optional<User> findByDniAndActivationKey(String dni, Object object);
    Optional<User> findByResetKey(String resetToken);       
           
}
