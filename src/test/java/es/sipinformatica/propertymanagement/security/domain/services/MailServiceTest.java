package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

@SpringBootTest
class MailServiceTest {
    @Mock
    private MailService mailService;

    @Captor
    private ArgumentCaptor<String> messageCaptor;
    @Captor
    private ArgumentCaptor<Boolean> messageBoleanCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User userBuilder;
    Set<Role> roles = new HashSet<>();

    @BeforeEach
    void userInit() {
        this.roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
        this.roles.add(Role.builder().name(ERole.ROLE_CUSTOMER).build());
        this.userBuilder = User.builder().username("TestEmail")
                .email("testemail@gmail.com").phone("adminServicephoneTest")
                .dni("A08001851").password("password").roles(roles)
                .activationKey(RandomStringUtils.randomAlphanumeric(20)).build();

    }

    @Test
     void shouldSendMail() throws MessagingException {
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean());
        mailService.sendEmail("testemail@gmail.com", "Test Subject", "Test mail content", false, true);
        verify(mailService).sendEmail(messageCaptor.capture(), messageCaptor.capture(), messageCaptor.capture(),
                messageBoleanCaptor.capture(), messageBoleanCaptor.capture());
        assertTrue(messageBoleanCaptor.getValue());
        assertEquals("Test mail content", messageCaptor.getValue());
    }

    @Test
     void shouldSendSimpleMail() throws MessagingException {
        doNothing().when(mailService).sendSimpleEmail(anyString(), anyString(), anyString());
        mailService.sendSimpleEmail("testemail@gmail.com", "Test Subject", "Test mail content");
        verify(mailService).sendSimpleEmail(messageCaptor.capture(), messageCaptor.capture(), messageCaptor.capture());
        assertEquals("Test mail content", messageCaptor.getValue());
    }

    @Test
     void shouldSendActivationEmail() throws MessagingException {
        doNothing().when(mailService).sendActivationEmail(any(), anyString());
        mailService.sendActivationEmail(userBuilder, "http://localhost:8080/api/auth/register");
        verify(mailService).sendActivationEmail(userCaptor.capture(), messageCaptor.capture());
        assertEquals("http://localhost:8080/api/auth/register", messageCaptor.getValue());
        assertEquals("TestEmail", userCaptor.getValue().getUsername());
        assertEquals("testemail@gmail.com", userCaptor.getValue().getEmail());
    }
}
