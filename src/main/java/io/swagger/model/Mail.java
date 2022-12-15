package io.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "mail object")
@Validated
@Entity
@Table
@Data
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromAddresses;
    private String fromPersonals;
    private String subject;
    private Date sentDate;
    private String textFromMessage;

    @Valid
    @ElementCollection
    private Set<String> hasAccess;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "mail_group_a",
            joinColumns = { @JoinColumn(name = "id") },
            inverseJoinColumns = { @JoinColumn(name = "name") }
    )
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<AccessGroup> groups = new HashSet<>();

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", fromAddresses='" + fromAddresses + '\'' +
                ", fromPersonals='" + fromPersonals + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate=" + sentDate +
                ", textFromMessage='" + textFromMessage + '\'' +
                ", hasAccess=" + hasAccess +
                '}';
    }
}
