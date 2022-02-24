package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    private UserSignupRequest userSignupRequest;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private List<User> userList;
    private User userDefault;
    private Optional<User> userDefaultOptional;

    @BeforeEach
    void init() {
        userList = new ArrayList<>();
        userDefault = new User();
        userDefaultOptional = Optional.of(new User());
        userSignupRequest = UserSignupRequest.builder().username("username").password("1Ppassword").dni("Z6762555P")
                .email("mail@www.es").phone("123478526").build();

        BeanUtils.copyProperties(userSignupRequest, userDefault);
        userList.add(userDefault);
        BeanUtils.copyProperties(userSignupRequest, userDefaultOptional);
        userDefaultOptional.get().setActivationKey("tokenactivationKey");

        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.findByActivationKey(any())).thenReturn(userDefaultOptional);
        when(userRepository.save(any(User.class))).thenReturn(userDefault);
        when(roleRepository.findByName(any(ERole.class)))
                .thenReturn(Optional.of(Role.builder().name(ERole.ROLE_MANAGER).build()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(userDefault));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userDefault));
        when(userRepository.findByResetKey(any())).thenReturn(Optional.of(userDefault));                        

    }

    @Test
    void shouldRegisterUser() {

        User userRegistred = userService.registerUser(userSignupRequest);
        log.info("userRegistred: {}", userRegistred);
        log.info("userList: {}", userList.get(0));

        Mockito.verify(userRepository, Mockito.times(1)).delete(any(User.class));

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

        Optional<User> userActivated = userService.activateUser("tokenactivationKey");
        log.info("userActivated: {}", userActivated);

        assertEquals(userDefaultOptional.get().getUsername(), userActivated.get().getUsername());
        assertEquals(userDefaultOptional.get().getEmail(), userActivated.get().getEmail());
        assertEquals(userDefaultOptional.get().getActivationKey(), userActivated.get().getActivationKey());
        assertTrue(userActivated.get().getIsEnabled());
    }

    @Test
    void shouldDeleteExpiredUsers() {

        userDefault.setActivationKey("tokenactivationKey");
        userService.deleteExpiredUsers();

        Mockito.verify(userRepository, Mockito.times(1)).delete(userDefault);
    }

    @Test
    void shouldChangePassword() {
        userDefault.setPassword(passwordEncoder.encode("1Ppassword"));
        userService.changePassword("1Ppassword", "new1Ppassword");

        assertNotEquals(userDefault.getPassword(), passwordEncoder.encode("1Ppassword"));
        assertTrue(passwordEncoder.matches("new1Ppassword", userDefault.getPassword()));

    }

    @Test
    void shouldRequestPasswordReset() {
        userDefault.setIsEnabled(true);
        userService.requestPasswordReset("mail@www.es");

        assertEquals(20, userDefault.getResetKey().length());
        assertTrue(userDefault.getResetDate().isAfter(LocalDateTime.now().minusHours(1)));

    }

    @Test
    void shouldFinishPasswordReset() {
        userDefault.setResetKey("tokenResetKey");
        userDefault.setResetDate(LocalDateTime.now());
        userService.finishPasswordReset("tokenResetKey", "new1Ppassword");

        assertNotEquals("tokenResetKey", userDefault.getResetKey());
        assertNotEquals(userDefault.getResetDate(), LocalDateTime.now().minusHours(1));
        assertTrue(passwordEncoder.matches("new1Ppassword", userDefault.getPassword()));

        Mockito.verify(userRepository, times(1)).save(userDefault);

    }
}
