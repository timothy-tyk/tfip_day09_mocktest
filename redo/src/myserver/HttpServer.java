package myserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
  private Integer PORT;
  private String[] directories;

  private final String PATHDOESNOTEXIST = "Error: File Path Does Not Exist!";
  private final String PATHISNOTDIRECTORY = "Error: File Path Is Not Directory!";
  private final String FILEISNOTREADABLE = "Error: File Is Not Readable!";

  public HttpServer(){}

  public void startServer(Integer PORT, String[] directories) throws IOException{
    //Check if directories exist, is directory, is readable by Server
        for(String directory: directories){
        Path path = Paths.get(directory);
        if(!path.toFile().exists()){
          System.out.println(PATHDOESNOTEXIST);
          System.exit(1);
        } 
        if(!path.toFile().isDirectory()){
          System.out.println(PATHISNOTDIRECTORY);
          System.exit(1);
        } 
        if(!Files.isReadable(path)){
          System.out.println(FILEISNOTREADABLE);
          System.exit(1);
        }
      }
      //Starts the Server
      ExecutorService threadpool = Executors.newFixedThreadPool(3);
      ServerSocket server = new ServerSocket(PORT);
      System.out.printf("Listening on Port %d\n",PORT);
      while(true){
        Socket socket = server.accept(); // accept incoming connection
        System.out.printf("Connection on Port %d by %d\n",PORT,socket.getPort());
        HttpClientConnection clientHandler = new HttpClientConnection(socket,directories);
        threadpool.submit(clientHandler);
        
        
      }
  }

}
