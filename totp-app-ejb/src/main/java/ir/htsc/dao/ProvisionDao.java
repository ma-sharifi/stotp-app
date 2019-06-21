package ir.htsc.dao;

import ir.htsc.entity.Provision;
import ir.htsc.produces.DatabaseTotp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by MSH on 6/13/2019.
 */
@Stateless
@TransactionManagement
@TransactionAttribute
public class ProvisionDao extends AbstractDaoFacade<Provision> {
    @Inject
    @DatabaseTotp
    private EntityManager em;

    public ProvisionDao() {
        super(Provision.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
