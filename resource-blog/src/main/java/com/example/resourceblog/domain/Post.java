package com.example.resourceblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post implements Serializable {
    @Id
    private String id;

    private String user;

    private Instant createdAt;

    private String title;

    @Lob
    private String content;
}
