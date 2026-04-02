package com.project.stayEase.entity;


import jakarta.persistence.*;

@Entity
public class BedType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;   // KING, QUEEN, TWIN

}
