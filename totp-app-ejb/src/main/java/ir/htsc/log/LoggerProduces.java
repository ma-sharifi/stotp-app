package ir.htsc.log;

import org.apache.log4j.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by m-asvadi on 7/4/2018.
 */
public class LoggerProduces {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint){
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
