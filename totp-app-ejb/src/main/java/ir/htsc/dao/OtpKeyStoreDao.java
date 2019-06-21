package ir.htsc.dao;

import ir.htsc.entity.OTPKeyStore;
import ir.htsc.produces.DatabaseTotp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MSH on 5/27/2019.
 */
@Stateless
@TransactionManagement
@TransactionAttribute
public class OtpKeyStoreDao extends AbstractDaoFacade<OTPKeyStore> {

    @Inject
    @DatabaseTotp
    private EntityManager em;

    public OtpKeyStoreDao() {
        super(OTPKeyStore.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OTPKeyStore findByUUID(String uuid) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("uuid", uuid);
        OTPKeyStore single = findSingleWithNamedQuery(OTPKeyStore.FIND_BY_UUID, parameter);
        return single;
    }
}
