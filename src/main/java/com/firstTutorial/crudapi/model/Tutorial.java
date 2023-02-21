package com.firstTutorial.crudapi.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name="tutorials")
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "published")
    private boolean published;




}
