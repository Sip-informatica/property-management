package es.sipinformatica.propertymanagement.security.domain.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import es.sipinformatica.propertymanagement.security.data.model.User;

@Service
public class MailService {
    private static final String USER = "user";

    private static final String BASEURL = "baseUrl";
    @Value("${spring.mail.username}")
    private String from = "";   
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private MessageSource messageSource;

    @Async
    public void sendActivationEmail(User user, String siteUrl) throws MessagingException {
        sendEmailFromTemplate(user, "activationEmail", siteUrl, "email.activation.subject");
    }
    @Async
    public void sendResetPasswordEmail(User user, String siteUrl) throws MessagingException {
        sendEmailFromTemplate(user, "passwordResetEmail", siteUrl, "email.reset.subject");
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String siteUrl, String titleKey ) throws MessagingException {
        Context context = new Context();
        context.setVariable(USER, user);
        context.setVariable(BASEURL, siteUrl);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, LocaleContextHolder.getLocale());
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml)
            throws MessagingException, MailException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, "UTF-8");
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(content, isHtml);
        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendSimpleEmail(String to, String subject, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);

    }    

}
