package ir.htsc.cache.listener;

import com.tangosol.util.MapEvent;
import com.tangosol.util.MapListener;
import org.apache.log4j.Logger;

/**
 * Created by MSH on 5/27/2019.
 */
public class TOTPKeyCacheListener implements MapListener {
    private final Logger LOGGER_NODE = Logger.getLogger("cacheMapListener");

    @Override
    public void entryInserted(MapEvent mapEvent) {
        LOGGER_NODE.info(mapEvent.getKey()+"->insert to cache with id: "+mapEvent.getId());

    }

    @Override
    public void entryUpdated(MapEvent mapEvent) {
        LOGGER_NODE.info(mapEvent.getKey()+"->update to cache with id: "+mapEvent.getId());
    }

    @Override
    public void entryDeleted(MapEvent mapEvent) {
        LOGGER_NODE.info(mapEvent.getKey()+"->delete from cache with id: "+mapEvent.getId());

    }
}
