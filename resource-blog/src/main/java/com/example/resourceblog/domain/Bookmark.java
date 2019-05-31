package com.example.resourceblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bookmark implements Serializable {
    @Id
    private String id;

    private String title;

    private Instant createdAt;

    private String user;

    @OneToOne
    private Post post;
}
