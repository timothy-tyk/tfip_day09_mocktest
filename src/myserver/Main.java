package myserver;

import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) {
    Integer PORT=3000;
    String[] docRoot = {"./target"};
    //Find Port, if not default to 3000
    for (Integer i=0;i<args.length;i++){
      if(args[i].equals("--port")){
        PORT = Integer.parseInt(args[i+1]);
      }
      if(args[i].equals("--docRoot")){
        docRoot = args[i+1].split(":");
      }
    }
    System.out.println(PORT);
    System.out.println(docRoot.length);

    // HttpServer server = new HttpServer();
    // server.checkPaths(docRoot);
    
    ExecutorService threadpool = Executors.newFixedThreadPool(3);
    HttpServer thr = new HttpServer(PORT, docRoot);
    threadpool.submit(thr);

  }
}
