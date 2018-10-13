package vw.app.happymypet.domains.vet;

import lombok.Getter;
import vw.app.happymypet.domains.base.NamedEntity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
class Specialty extends NamedEntity implements Serializable {
}
