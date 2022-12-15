package io.swagger.service.mail;

import com.google.common.collect.Sets;
import com.sun.mail.imap.protocol.FLAGS;
import io.swagger.model.Mail;
import io.swagger.repository.MailRepository;
import io.swagger.service.auth.HeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.NewsAddress;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiverEmailService {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    HeaderService headerService;

    @Async
    public void readMails(){

        String host = "pop.gmail.com";
        String user = "agregator.mail.okutnii@gmail.com";
        String password = "ggqtfrwjbnswpgct"; // ggZwqyindjpduhbvt62m
        Properties props = new Properties();

        props.setProperty("mail.store.protocol", "pop3s");
        props.setProperty("mail.pop3s.host", host);
        props.setProperty("mail.pop3s.port", "995");
        props.setProperty("mail.pop3s.auth", "true");
        props.setProperty("mail.pop3s.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory" );
        props.setProperty("mail.pop3s.ssl.trust", "*");

        props.put("mail.pop3.host", host);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", true);

        Session emailSession = Session.getDefaultInstance(props);
        try{
            Store store = emailSession.getStore("pop3s");
            store.connect(host, user, password);

            try(Folder emailFolder = store.getFolder("INBOX")) {
                emailFolder.open(Folder.READ_WRITE);

                List<Mail> mails = new ArrayList<>();
                Message[] messages = emailFolder.getMessages();
                for (int i = 0; i < messages.length; i++) {
                    Message msg = messages[i];
                    Mail mail = new Mail();

                    mail.setSentDate(msg.getSentDate());
                    mail.setSubject(msg.getSubject());
                    mail.setTextFromMessage(getTextFromMessage(msg));
                    mail.setSentDate(msg.getSentDate());

                    List<String> fromPersonals = Arrays.stream(msg.getFrom()).map(address -> ((InternetAddress) address).getPersonal()).collect(Collectors.toList());
                    mail.setFromPersonals(String.join(",", fromPersonals));
                    Set<String> fromAddresses = Arrays.stream(msg.getFrom()).map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toSet());
                    mail.setFromAddresses(String.join(",",fromAddresses));

                    HashSet<String> hasAccess = new HashSet<>(fromAddresses);
                    mail.setHasAccess(hasAccess);
                    mails.add(mail);
                    log.info("Mail: " + mail);
//                    msg.setFlag(FLAGS.Flag.DELETED, true);
                    if(i % 20 == 0 && i != 0){
                        mailRepository.saveAll(mails);
                        mails = new ArrayList<>();
                    }
                }
                mailRepository.saveAll(mails);

            }
        }  catch (MessagingException | IOException e) {
            throw new RuntimeException("Mail receiving is failed",e);
        }

    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
}
