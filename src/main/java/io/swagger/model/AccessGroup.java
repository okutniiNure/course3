package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "AccessGroup object")
@Validated
@Entity
@Table
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"mails"})
public class AccessGroup {
    @Id
    private String name;
    private String owner;
    @Valid
    @ElementCollection
    private Set<String> usernames;


    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Set<Mail> mails = new HashSet<>();

    @Override
    public String toString() {
        return "AccessGroup{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", usernames=" + usernames +
                '}';
    }
}
