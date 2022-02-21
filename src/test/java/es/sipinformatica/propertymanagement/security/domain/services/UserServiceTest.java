package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    private UserSignupRequest userSignupRequest;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private List<User> userList;
    private User userDefault;

    @BeforeEach
    void init() {
        userSignupRequest = UserSignupRequest.builder().username("username").password("1Ppassword").dni("Z6762555P")
                .email("mail@www.es").phone("123478526").build();
        userList = new ArrayList<>();
        userDefault = new User();
        BeanUtils.copyProperties(userSignupRequest, userDefault);
        userList.add(userDefault);
    }

    @Test
    void shouldRegisterUser() {
        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(any(User.class))).thenReturn(userDefault);        
       
        User userRegistred = userService.registerUser(userSignupRequest);
        log.info("userRegistred: {}", userRegistred);
        log.info("userList: {}", userList.get(0));
        Mockito.verify(userRepository, Mockito.times(1)).delete(userList.get(0));

        assertEquals(userSignupRequest.getUsername(), userRegistred.getUsername());
        assertNotEquals(userSignupRequest.getPassword(), userRegistred.getPassword());
        assertEquals(userSignupRequest.getDni(), userRegistred.getDni());
        assertEquals(userSignupRequest.getEmail(), userRegistred.getEmail());
        assertEquals(userSignupRequest.getPhone(), userRegistred.getPhone());
        assertEquals("ROLE_MANAGER", userRegistred.getRoles().stream().findFirst().get().getName().toString());
        assertEquals(20, userRegistred.getActivationKey().length());

    }  

    @Test
    void shouldFindUser() {       
        
        when(userRepository.findAll()).thenReturn(userList);

        User userFindUsername = userService.findUser("username");
        User userFindDni = userService.findUser("Z6762555P");
        User userFindEmail = userService.findUser("mail@www.es");
        User userFindPhone = userService.findUser("123478526");
        log.info("userFind: {}", userFindUsername);

        assertEquals(userDefault.getUsername(), userFindUsername.getUsername());
        assertEquals("mail@www.es", userFindEmail.getEmail());
        assertEquals("123478526", userFindPhone.getPhone());
        assertEquals("Z6762555P", userFindDni.getDni());

    }

    @Test
    void shouldActivationUser() {
        Optional<User> userDefault = Optional.of(new User());
        BeanUtils.copyProperties(userSignupRequest, userDefault);
        userDefault.get().setActivationKey("tokenactivationKey");

        when(userRepository.findByActivationKey(any())).thenReturn(userDefault);

        Optional<User> userActivated = userService.activateUser("tokenactivationKey");
        log.info("userActivated: {}", userActivated);

        assertEquals(userDefault.get().getUsername(), userActivated.get().getUsername());
        assertEquals(userDefault.get().getEmail(), userActivated.get().getEmail());
        assertEquals(userDefault.get().getActivationKey(), userActivated.get().getActivationKey());
        assertTrue(userActivated.get().getIsEnabled());
    }  

    @Test
    void shouldDeleteExpiredUsers() {
        List<User> userList = new ArrayList<>();
        User userDefault = new User();
        BeanUtils.copyProperties(userSignupRequest, userDefault);
        userDefault.setActivationKey("tokenactivationKey");
        userList.add(userDefault);
        when(userRepository.findAll()).thenReturn(userList);

        userService.deleteExpiredUsers();

        Mockito.verify(userRepository, Mockito.times(1)).delete(userDefault);
    }

}
