import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.KeeperException.Code;

import java.util.List;

public class DataMonitor implements Watcher, AsyncCallback.StatCallback {
    ZooKeeper zk;
    String znode;
    Watcher chainedWatcher;
//    boolean dead;
    DataMonitorListener listener;
    byte prevData[];

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);
        try {
            zk.getChildren(znode, this);
        } catch (KeeperException | InterruptedException e) {
            System.out.println("There is no node: " + znode);
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
//        System.out.println("DM processResult");
        switch (i) {
            case Code.Ok -> listener.exists();
            case Code.NoNode, Code.SessionExpired, Code.NoAuth -> listener.closing(i);
            default -> zk.exists(znode, true, this, null); // Retry errors
        }
    }

    public long countChildren(String parent){
        try {
            List<String> children = zk.getChildren(parent, this);
            return children.size() + children.stream().mapToLong(child -> countChildren(parent + "/" + child)).sum();
        } catch (KeeperException | InterruptedException e) {
            System.out.println("Incorrect node " + parent);
            return -1;
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
//        System.out.println("DM PROCESS");
        String path = watchedEvent.getPath();

        // session expired
        if (watchedEvent.getType() == Event.EventType.None && watchedEvent.getState() == Event.KeeperState.Expired) {
            listener.closing(Code.SessionExpired);
        }

        // children change
        else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            long childrenNumber = countChildren(znode);
            System.out.println("CHILDREN: " + childrenNumber);
            if (childrenNumber != -1)
                System.out.println("Detected children change.\n\tNumber of descendants: " + childrenNumber);
        }

        else if (path != null && path.equals(znode)) {
            // Something has changed on the node, let's find out
            zk.exists(znode, true, this, null);
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(watchedEvent);
        }
    }





    public interface DataMonitorListener {
        /**
         * The existence status of the node has changed.
         */
        void exists();

        /**
         * The ZooKeeper session is no longer valid.
         *
         * @param rc
         *                the ZooKeeper reason code
         */
        void closing(int rc);
    }
}
