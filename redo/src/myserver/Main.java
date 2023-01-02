package myserver;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    Integer PORT=3000;
    String[] directories = {"./target"};

    for(Integer i=0;i<args.length;i++){
      if(args[i].equals("--port")){
        PORT = Integer.parseInt(args[i+1]);
      }
      if(args[i].equals("--docRoot")){
        directories = args[i+1].split(":");
      }
    }
    // System.out.println(PORT);
    // System.out.println(directories.length);
    HttpServer server = new HttpServer();
    try {
      server.startServer(PORT, directories);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
