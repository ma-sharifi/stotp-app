package ir.htsc.produces;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Created by MSH on 02/12/2016.
 */
public class DatabaseProducer  {
    @Produces
    @PersistenceContext(unitName = "PU_TOTP")
    @DatabaseTotp
    private EntityManager em;

}
