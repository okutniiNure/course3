package io.swagger.api;

import io.swagger.manager.MailManager;
import io.swagger.model.Mail;
import io.swagger.service.mail.ReceiverEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailApiController implements MailApi{

    private final ReceiverEmailService receiverEmailService;

    private final MailManager manager;

    @Override
    public void triggerReadMails(HttpServletResponse response) {
        receiverEmailService.readMails();
    }

    @Override
    public List<Mail> getSavedMails(HttpServletResponse response) {
        return manager.getSavedMails();
    }

    @Override
    public Mail getLatestMail(HttpServletResponse response) {
        return manager.getLatestMail();
    }

    @Override
    public List<Mail> getMailsBySubjectAndDate(String subject, Date sentDate, HttpServletResponse response) {
        return manager.getMailsBySubjectAndDate(subject,sentDate);

    }

    @Override
    public Mail getMailById(Long id, HttpServletResponse response) {
        return manager.getMailById(id);
    }

    @Override
    public void deleteMailsByIds(Set<Long> ids, HttpServletResponse response) {
        manager.deleteMailsByIds(ids);
    }

    @Override
    public List<Mail> addGroupsForMailsByIds(Set<String> groups, Set<Long> ids, HttpServletResponse response) {
        return manager.addGroupsForMailsByIds(groups,ids);
    }

    @Override
    public Mail deleteGroupsForMailById(Set<String> groupNames, Long id, HttpServletResponse response) {
        return manager.deleteGroupsForMailById(groupNames,id);
    }
}
