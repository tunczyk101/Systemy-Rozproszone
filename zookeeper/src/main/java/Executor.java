import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
    String znode;
    String[] exec;
    ZooKeeper zk;
    DataMonitor dm;
    Process child;


    public Executor(String hostPort, String znode, String[] exec) throws IOException {
        this.znode = znode;
        this.exec = exec;

        zk = new ZooKeeper(hostPort, 5000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }


    public static void main(String[] args) {
        if (args.length < 2){
            System.err.println("""
                    Incorrect input: too few arguments
                    USAGE: hostPort program [args ...]
                    """);
            System.exit(2);
        }

        String hostPort = args[0];
        String znode = "/z";
        String[] exec = new String[args.length - 1];
        System.arraycopy(args, 1, exec, 0, exec.length);
        try {
            new Executor(hostPort, znode, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showTree() {
        try {
            List<String> children = zk.getChildren(znode, dm);
            System.out.println("Tree: \n" + znode + "\n" + showChildren(znode, children, "\t"));

        } catch (KeeperException | InterruptedException e) {
            System.out.println("/z node does not exist");
        }
    }

    private String showChildren(String parent, List<String> children, String depth) throws KeeperException, InterruptedException {
        StringBuilder subTree = new StringBuilder();

        for (String child : children){
            String path = parent + "/" + child;
            String childString = depth + child + "\n";
            subTree.append(childString);
            List<String> grandchildren = zk.getChildren(path, dm);
            subTree.append(showChildren(path, grandchildren, depth + "\t"));
        }

        return subTree.toString();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (dm != null)
            dm.process(watchedEvent);
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean run = true;

        while (run){
            try {
                System.out.println("Enter 'tree' to print current tree or 'stop' to close");

                String line = reader.readLine();

                switch (line){
                    case "tree" -> showTree();
                    case "stop" -> {
                        closing(0);
                        run = false;
                    }
                    default -> System.out.println("Unrecognised " + line + " command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exists() {
        try {
            child = Runtime.getRuntime().exec(exec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
//            notifyAll();
            if (child != null)
                child.destroy();
        }
    }
}
