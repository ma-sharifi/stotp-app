package ir.htsc.listeners;

/**
 * Created by MSH on 6/13/2019.
 */

import com.tangosol.util.Base;
import com.tangosol.util.MapEvent;
import com.tangosol.util.MapListener;
import org.apache.log4j.Logger;

/**
 * A MapListener implementation that prints each event as it receives
 * them.
 * https://docs.oracle.com/middleware/1212/coherence/COHDG/api_events.htm#COHDG5190
 */
public class EventListener extends Base
        implements MapListener {
    private static Logger LOGGER = Logger.getLogger("mapEventListener");
    public void entryInserted(MapEvent evt) {
        LOGGER.info("Entry Inserted: "+evt);//out(evt);
    }

    public void entryUpdated(MapEvent evt) {
        LOGGER.info("Entry Updated: "+evt);
    }

    public void entryDeleted(MapEvent evt) {
        LOGGER.info("Entry Deleted: "+evt);
    }
}