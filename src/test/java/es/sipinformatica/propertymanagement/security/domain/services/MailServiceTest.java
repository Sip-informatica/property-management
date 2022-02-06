package es.sipinformatica.propertymanagement.security.domain.services;

import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    private User userBuilder;
    Set<Role> roles = new HashSet<>();    

    @BeforeEach
    void userInit() {
        this.roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
        this.roles.add(Role.builder().name(ERole.ROLE_CUSTOMER).build());       
        this.userBuilder = User.builder().username("TestEmail")
                .email("garcia.romero.candido@gmail.com").phone("adminServicephoneTest")
                .dni("A08001851").password("password").roles(roles)
                .activationKey(RandomStringUtils.randomAlphanumeric(20)).build();        
    }

    @Test
    public void shouldSendMail() throws MessagingException {
        mailService.sendEmail("garcia.romero.candido@gmail.com", "Test Subject", "Test mail content", false, true);
    }

    @Test
    public void shouldSendSimpleMail() throws MessagingException {
        mailService.sendSimpleEmail("garcia.romero.candido@gmail.com", "Test Subject", "Test mail content");
    }

    @Test
    public void shouldSendActivationEmail() throws MessagingException {
        
        mailService.sendActivationEmail(userBuilder);
    }
}
