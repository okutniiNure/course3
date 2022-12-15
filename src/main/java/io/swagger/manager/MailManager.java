package io.swagger.manager;

import io.swagger.exception.ApiException;
import io.swagger.model.AccessGroup;
import io.swagger.model.Mail;
import io.swagger.repository.AccessGroupRepository;
import io.swagger.repository.MailRepository;
import io.swagger.service.auth.HeaderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailManager {

    private final MailRepository mailRepository;

    private final AccessGroupRepository accessGroupRepository;

    private final HeaderService headerService;


    public List<Mail> getSavedMails() {
        String username = headerService.getUsername();
        log.info("Finding mails for username({})", username);

        return mailRepository.findAllByHasAccessContains(Set.of(username));
    }

    public Mail getLatestMail() {
        String username = headerService.getUsername();
        log.info("Finding latest mail for username({})", username);

        return mailRepository.findFirstByHasAccessContains(Set.of(username));
    }

    public List<Mail> getMailsBySubjectAndDate(String subject, Date sentDate) {
        String username = headerService.getUsername();
        log.info("Finding mails for username({}), sent date({}), subject({})", username, sentDate, subject);
        if(Objects.isNull(sentDate)) {
            return mailRepository.findAllByHasAccessContainsAndSubject(Set.of(username), subject);
        }else {
            return List.of(mailRepository.findByHasAccessContainsAndSentDateAndSubject(Set.of(username), sentDate, subject).orElseThrow(() -> new ApiException(HttpStatus.NO_CONTENT.value(), "No latest mails for this username: " + username)));
        }
    }

    public Mail getMailById(Long id) {
        String username = headerService.getUsername();
        log.info("Finding mails for username({}), id({})", username, id);

        return mailRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NO_CONTENT.value(), "No latest mails for this username: " + username));
    }

    public void deleteMailsByIds(Set<Long> ids) {
        String username = headerService.getUsername();
        mailRepository.deleteAllByIdInAndFromAddresses(ids, username);
    }

    public List<Mail> addGroupsForMailsByIds(Set<String> groups, Set<Long> ids) {
        String username = headerService.getUsername();
        log.info("Finding mails for username({}), id({})", username, ids);
        List<AccessGroup> existingGroups = accessGroupRepository.findAllById(groups);
        if(!existingGroups.isEmpty()) {
            List<Mail> mails = mailRepository.findAllByIdInAndHasAccessContains(ids, Set.of(username));
            existingGroups.forEach(existingGroup ->{
                mails.forEach(mail -> {
                    mail.getGroups().add(existingGroup);
                    mail.getHasAccess().addAll(existingGroup.getUsernames());
                });
            });
            return mailRepository.saveAll(mails);
        }
        throw new ApiException(HttpStatus.NOT_FOUND.value(), "No groups founded for names: " + String.valueOf(groups));
    }

    public Mail deleteGroupsForMailById(Set<String> groupNames, Long id) {
        String username = headerService.getUsername();
        log.info("Finding mails for username({}), id({})", username, id);
        Mail mail = mailRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.OK.value(), "No mails with id: " + id));
        if(!mail.getHasAccess().contains(username)){
            throw new ApiException(HttpStatus.FORBIDDEN.value(), "Not your mail with id: " + id);
        }
        removeGroupFromMail(mail,groupNames);
        return mailRepository.save(mail);

    }

    private void removeGroupFromMail(Mail mail, Set<String> groupNames){
        Set<AccessGroup> existingGroups = mail.getGroups();
        if(existingGroups.isEmpty()) {
            throw new ApiException(HttpStatus.OK.value(), "No groups founded for names: " + String.valueOf(groupNames));
        }
        Set<String> accesses = existingGroups.stream()
                .filter(group -> groupNames.contains(group.getName()))
                .flatMap(accessGroup -> accessGroup.getUsernames().stream())
                .collect(Collectors.toSet());

        accesses.addAll(Arrays.asList(mail.getFromPersonals().split(",")));

        mail.setHasAccess(accesses);
    }
}
