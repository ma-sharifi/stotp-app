package ir.htsc.rest.listener;

import com.tangosol.net.*;
import ir.htsc.cache.listener.TOTPKeyCacheListener;
import ir.htsc.log.Loggable;
import ir.htsc.AppConstants;
import ir.htsc.security.UUIDGenerator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Set;

/**
 * Created by MSH on 5/27/2019.
 */
@WebListener
@Loggable
public class ServletContextListener implements javax.servlet.ServletContextListener {

    public static ServletContext context;
    private static Logger logger_stackTrace = Logger.getLogger("stackTrace");
    @Inject
    private Logger logger;
    @Resource(mappedName = "totp_key_cache")
    private com.tangosol.net.NamedCache cacheKey;
    @Resource(mappedName = "totp_uuid_cache")
    private com.tangosol.net.NamedCache cacheUuid;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        long start = System.currentTimeMillis();
        context = servletContextEvent.getServletContext();
        initializeLog4j(servletContextEvent);
        logger.info("********************** SOTP Server ver: " + AppConstants.VERSION_NO + " Starting... ***************");
        printAppInfo();
        UUIDGenerator.initialize();
        initializeCache();
        logger.info("[" + (System.currentTimeMillis() - start) + "] ms ---------- SOTP Server ver: " + AppConstants.VERSION_NO + " Started and Running --------------");
    }

    private void printAppInfo() {
        String moduleName = "";
        String appName = "";
        try {
            InitialContext ctx = new InitialContext();
            moduleName = (String) ctx.lookup("java:module/ModuleName");
            appName = (String) ctx.lookup("java:app/AppName");
        } catch (NamingException e) {
        }
        try {
            //------------------ HOST -----------
            AppConstants.host_Address = InetAddress.getLocalHost().getHostAddress();
            AppConstants.host_Name = InetAddress.getLocalHost().getHostName();

            logger.info("HOST ADDRESS: " + AppConstants.host_Address);
            logger.info("HOST NAME: " + AppConstants.host_Name);
            logger.info("SERVER_INFO" + ": " + context.getServerInfo());
            logger.info("APPLICATION_SERVER_VIRTUAL_SERVER_NAME" + ": " + context.getVirtualServerName());
            logger.info("APP NAME: " + appName);
            logger.info("MODULE NAME: " + moduleName);
            logger.info("CONTEXT_PATH" + ": " + context.getContextPath());
            logger.info("VERSION_NO" + ": " + AppConstants.VERSION_NO);
            logger.info("BUILD_DATE" + ": " + AppConstants.BUILD_DATE);

        } catch (UnknownHostException ex) {
            logger.error("Exception in app info");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void initializeLog4j(ServletContextEvent sce) {
        //http://www.journaldev.com/10721/log4j-warn-no-appenders-could-be-found-for-logger-please-initialize-the-log4j-system-properly
        String webAppPath = sce.getServletContext().getRealPath("/");
        String log4jFilePath = webAppPath + "WEB-INF/log4j.xml";
        DOMConfigurator.configure(log4jFilePath);
        logger.info("INITIALIZED log4j configuration from file:" + log4jFilePath);
    }

    //https://docs.oracle.com/middleware/1221/coherence/develop-applications/api_cache.htm#COHDG4870
    public int getStorageMembers(NamedCache cache) {
        try {
            int size = ((DistributedCacheService) cache.getCacheService()).getOwnershipEnabledMembers().size();//.getStorageEnabledMembers();
            return size;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception in cacheKey " + "Goto stackTrace.log for detail! message:" + e.getMessage());
            logger_stackTrace.error("Exception in cacheKey " + "Goto stackTrace.log for detail! message:" + e);
        }
        return -1;
    }

    public void initializeCache() throws RuntimeException {
        logger.info("---------------Cache Info-----------------");
        try {
            Member thisMember = CacheFactory.getCluster().getLocalMember();
            Cluster clusterTotal = CacheFactory.getCluster();
            Set<Member> memberSet = null;

            logger.info("Cluster Name: " + clusterTotal.getClusterName());
            logger.info("Cluster Domain Name: " + clusterTotal.getManagement().getDomainName());
            int sc = clusterTotal.getMemberSet().size();
            logger.info("Total Cluster Member Joined: " + sc);
//            CacheService service = cacheKey.getCacheService();
//            Cluster cluster = service.getCluster();
//            logger.info("Cache cluster: "+cluster.getClusterName());
            memberSet = CacheFactory.getCluster().getMemberSet();
            if (memberSet != null) {
                long mc = memberSet.size();
                logger.info("Total Cache Server: " + mc);
                if (sc != mc)
                    logger.error("#ERROR! TOTAL JOINED CLUSTER MEMBER != TOTAL CACHE SERVER " + mc);
            }
            logger.info("This Cache Server's Id: " + thisMember.getId() + " ,Role: " + thisMember.getRoleName() + " ,Address: " + thisMember.getAddress());

            for (Member member : memberSet) {
                if (member != thisMember)
                    logger.info("Cache Server's Id: " + member.getId() + " ,Role: " + member.getRoleName() + " ,Address: " + member.getAddress());
            }
            CacheService service = cacheKey.getCacheService();
            Cluster clusterCacheKey = service.getCluster();
            Enumeration enumeration = clusterCacheKey.getServiceNames();
            while (enumeration.hasMoreElements()) {
                String sName = (String) enumeration.nextElement();
                ServiceInfo serviceInfo = clusterTotal.getServiceInfo(sName);
                if (serviceInfo != null) {
                    logger.info("Cache Service Name: " +serviceInfo.getServiceName()+
                            " ,Service Type: "+ serviceInfo.getServiceType()+
                            " ,service role: "+serviceInfo.getOldestMember().getRoleName()
                            );//" ,ServiceMembers: "+serviceInfo.getServiceMembers()
                }
            }

            cacheKey.addMapListener(new TOTPKeyCacheListener());
            String test = "initialize cacheKey(write to cacheKey and read from cacheKey).";
            cacheKey.put(-1, 1);
            int readFromCache = (int) cacheKey.get(-1);
            cacheKey.remove(-1);
            if (readFromCache != 0) {
                logger.info("Success ->" + test + " Cache size is: " + cacheKey.size());
            } else logger.error("Failed  ->" + test);
            logger.info(cacheKey.getCacheName()+"->Storage enabled node no: " + String.format("%-3d", getStorageMembers(cacheKey)) );

        } catch (Exception ex) {
            logger.error("Exception in cacheKey " + "Goto stackTrace.log for detail! message:" + ex.getMessage());
            logger_stackTrace.error("Exception in cacheKey " + "Goto stackTrace.log for detail! message:" + ex);
//            errorCount++;
        }
    }

}
