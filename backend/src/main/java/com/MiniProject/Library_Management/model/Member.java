package com.MiniProject.Library_Management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;
    private String email;
    @Column(nullable = false)
    private String password;
    @Builder.Default
    private Integer booksIssued = 0;

    @Builder.Default
    private Integer maxBooksAllowed = 5;
}