package com.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public Stats() {
    }

    public int getId() {
        return id;
    }

}
