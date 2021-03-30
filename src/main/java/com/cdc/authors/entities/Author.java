package com.cdc.authors.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Author implements Serializable {

    private static final long serialVersionUID = -5385657076704160688L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false, length = 400, columnDefinition = "TEXT")
    @NotBlank
    @Length(max = 400)
    private String description;

    @Column(nullable = false, updatable = false)
    @NotNull
    @CreatedDate
    private Instant creationDate;

    @Deprecated
    public Author() {
    }

    public Author(@NotBlank String name,
                  @NotBlank @Email String email,
                  @NotBlank @Length(max = 400) String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }

}
