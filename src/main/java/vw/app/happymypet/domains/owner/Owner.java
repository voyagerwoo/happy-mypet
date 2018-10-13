package vw.app.happymypet.domains.owner;

import lombok.Data;
import vw.app.happymypet.domains.base.Person;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Owner extends Person {
    @Column(nullable = false)
    private Long userId;
}
