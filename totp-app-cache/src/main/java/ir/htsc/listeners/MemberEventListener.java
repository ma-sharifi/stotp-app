package ir.htsc.listeners;

import com.tangosol.net.MemberEvent;
import com.tangosol.net.MemberListener;
import com.tangosol.util.Base;
import org.apache.log4j.Logger;

/**
 * Created by MSH on 6/13/2019.
 * https://docs.oracle.com/cd/E15357_01/coh.360/e15723/cluster_member.htm#COHDG5174
 * The <member-listener> element can be used within the <distributedscheme>,
 <replicated-scheme>, <optimistic-scheme>, <invocationscheme>,
 and <proxy-scheme> elements. See Cache Configuration Elements for a
 reference of valid cache configuration elements.
 */
public class MemberEventListener extends Base implements MemberListener {
    private static Logger LOGGER = Logger.getLogger("memberEventListener");

    /**
     * Note:
     A MemberListener implementation must have a public default constructor
     when using the <member-listener> element to add a listener to a service.
     */
    public MemberEventListener() {
    }

    @Override
    public void memberJoined(MemberEvent memberEvent) {

        LOGGER.info("Member Joined -> service: "+memberEvent.getService() +" "+memberEvent);
    }

    @Override
    public void memberLeaving(MemberEvent memberEvent) {
        LOGGER.info("Member Leaving-> service: "+memberEvent.getService() +" "+memberEvent);
    }

    @Override
    public void memberLeft(MemberEvent memberEvent) {

        LOGGER.info("Member Left   -> service: "+memberEvent.getService() +" "+memberEvent);
    }
}
