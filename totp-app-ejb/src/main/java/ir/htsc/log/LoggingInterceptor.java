package ir.htsc.log;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.perf4j.log4j.Log4JStopWatch;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Antonio Goncalves
 *         Training - Beginning with The Java EE 7 Platform
 *         http://www.antoniogoncalves.org
 *         --
 */
@javax.interceptor.Interceptor
@Loggable
public class LoggingInterceptor implements Serializable {

    private final String ERROR_MESSAGE="Goto stackTrace.log for detail! message: ";
    private static AtomicLong refrenceId=new AtomicLong(0);
    private Logger LOGGER_SERVICE_TIMING = Logger.getLogger("serviceTiming");
    private Logger LOGGER_STACKTRACE = Logger.getLogger("stackTrace");
    private Logger LOGGER_CRITICAL = Logger.getLogger("critical");

    @Inject
    private Logger LOGGER;

    // ======================================
    // =          Business methods          =
    // ======================================

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        String clazz = ic.getMethod().getDeclaringClass().getSimpleName();
        String method = ic.getMethod().getName();
        String format= StringFormatHelper.getFormatForStackTrace(clazz,method);
        Object[] objs = ic.getParameters();
        String entityClazzName = "";
        Object parameter = null;
        if (objs.length > 0) {
            parameter = objs[0];
            entityClazzName = parameter.getClass().getSimpleName();
        }
        Log4JStopWatch stopwatch = new Log4JStopWatch();
        try {

            return ic.proceed();

        } catch (Exception ex) {
            refrenceId.incrementAndGet();
            String refId="refId: "+String.format("%-6s",refrenceId+"");
            LOGGER.error(refId+ERROR_MESSAGE+ex.getMessage());
            LOGGER_STACKTRACE.log(Level.ERROR,refId+format, ex);
            throw new Exception(ex);
        } finally {
            stopwatch.stop("SRV_"+StringFormatHelper.getFormatForStatisticsTiming(clazz, method));
            long diff = stopwatch.getElapsedTime();
            String msgLog="[" + String.format("%-6d", diff) + " ms]" + format+
                    (parameter!=null ?" ,value:" + parameter:"");
            if(diff>30000)
                LOGGER_CRITICAL.warn("SRV_"+msgLog);
            LOGGER_SERVICE_TIMING.info(msgLog);
        }

    }

}
