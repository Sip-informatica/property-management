package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MailServiceTest {
    @MockBean
    private JavaMailSender javaMailSender;
    @Autowired
    private MailService mailService;
    private User userDefault;
    private UserSignupRequest userSignupRequest;

    @BeforeEach
    void init() {
        userSignupRequest = UserSignupRequest.builder().username("username").password("1Ppassword").dni("Z6762555P")
                .email("mail@www.es").phone("123478526").build();
        userDefault = new User();
        BeanUtils.copyProperties(userSignupRequest, userDefault);
    }

    @Test
    void shouldSendSimpleMail() {
        mailService.sendSimpleEmail("to@sip.es", "subject@sip.es", "content");
        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(emailCaptor.capture());

        List<SimpleMailMessage> actualList = emailCaptor.getAllValues();
        log.info("Actual list: {}", actualList);
        assertEquals("to@sip.es", actualList.get(0).getTo()[0]);
        assertEquals("subject@sip.es", actualList.get(0).getSubject());
        assertEquals("content", actualList.get(0).getText());

    }

    @Test
    void shouldSendMail() throws MailException, MessagingException, IOException {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailService.sendEmail("to@sip.es", "subject@sip.es", "content", false, true);

        ArgumentCaptor<MimeMessage> emailCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).createMimeMessage();

        verify(javaMailSender).send(emailCaptor.capture());
        MimeMessage message = (MimeMessage) emailCaptor.getValue();

        assertEquals("to@sip.es", message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("subject@sip.es", message.getSubject());
        assertEquals("content", message.getContent());

    }

    @Test
    void shouldSendEmailFromTemplate() throws MailException, MessagingException, IOException {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailService.sendEmailFromTemplate(userDefault, "activationEmail", "siteUrl", "email.activation.subject");
        ArgumentCaptor<MimeMessage> emailCaptor = ArgumentCaptor.forClass(MimeMessage.class);

        verify(javaMailSender).send(emailCaptor.capture());
        MimeMessage message = (MimeMessage) emailCaptor.getValue();
        
        assertEquals("mail@www.es", message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("Activación de Usuario", message.getSubject());
        assertEquals("text/html;charset=UTF-8", message.getDataHandler().getContentType());
        assertTrue(message.getContent().toString().contains(
                "Su cuenta de usuario ha sido creada, por favor, haga clic en el enlace de abajo para activarla:"));
    }

    @Test
    void shouldSendActivationEmail() throws MailException, MessagingException, IOException {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailService.sendActivationEmail(userDefault, "siteUrl");
        ArgumentCaptor<MimeMessage> emailCaptor = ArgumentCaptor.forClass(MimeMessage.class);

        verify(javaMailSender).send(emailCaptor.capture());
        MimeMessage message = (MimeMessage) emailCaptor.getValue();

        assertEquals("mail@www.es", message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("Activación de Usuario", message.getSubject());
        assertEquals("text/html;charset=UTF-8", message.getDataHandler().getContentType());
        assertTrue(message.getContent().toString().contains(
                "siteUrl/activate/null"));
    }
}
