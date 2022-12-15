package io.swagger.repository;

import io.swagger.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.Valid;
import java.util.*;

public interface MailRepository  extends JpaRepository<Mail, Long> {

    List<Mail> findAllByIdInAndHasAccessContains(Collection<Long> id, @Valid Set<String> hasAccess);

    List<Mail> findAllByHasAccessContains(@Valid Set<String> hasAccess);

@Query(nativeQuery = true, value = "SELECT id,from_addresses," +
        "from_personals,sent_date, subject" +
        " ,text_from_message FROM mail mail0_  " +
        " where (:hasAccess) in (select " +
        "                hasaccess1_.has_access " +
        "            from " +
        "                mail_has_access hasaccess1_ " +
        "            where " +
        "                mail0_.id=hasaccess1_.mail_id) " +
        "ORDER BY id ASC\n" +
        "LIMIT 1")
    Mail findFirstByHasAccessContains(@Valid Set<String> hasAccess);

    Optional<Mail> findByHasAccessContainsAndSentDateAndSubject(@Valid Set<String> hasAccess, Date sentDate, String subject);

    List<Mail> findAllByHasAccessContainsAndSubject( @Valid Set<String> hasAccess, String subject);

    void deleteAllByIdInAndFromAddresses(Collection<Long> ids, String fromAddresses);
}
