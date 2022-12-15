package myserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpClientConnection implements Runnable{
  private Socket socket;
  private String[] directories;
  public HttpClientConnection(){}
  public HttpClientConnection(Socket socket, String[] directories){
    this.socket = socket;
    this.directories = directories;
  }

  @Override
  public void run() {
    try {
      OutputStream os = socket.getOutputStream();
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader bfr = new BufferedReader(isr);
      String request = bfr.readLine();
      System.out.println(request);

      String reqMethod = request.split(" ")[0];
      HttpWriter clientOut = new HttpWriter(os);
      switch(reqMethod){
        case "GET":
          String resource = request.split(" ")[1];
          if(resource.equals("/")) {resource = "/index";} 
          System.out.println(resource);

          boolean found = false;
          for(String directory:directories){
            Path filePath = Paths.get(String.format("%s%s", directory, resource));
            if(filePath.toFile().exists()){
              System.out.println("Resource found");
              try {
                //Action 3 - Resource Exist
                String png = "Content-Type: image/png\r\n";
                //Action 4 - png?
                if(filePath.endsWith(".png")){
                  clientOut.writeString("HTTP/1.1 200 OK\r\n\nContent-Type: image/png\r\n\n\r\n");
                }else{
                  clientOut.writeString("HTTP/1.1 200 OK\r\n");
                }
                
                clientOut.writeBytes(Files.readAllBytes(filePath));
                clientOut.flush();
                // clientOut.close();
                found = true;
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            //Action 2 - Resource does not exist
            if(!found){
              System.out.println("Resource not found");
              clientOut.writeString(String.format("HTTP/1.1 404 Not Found\r\n\n \r\n\n%s not found\r\n",resource));
              clientOut.flush();
              
            }

          }
          break;
        default:
        // Action 1 - Not a GET Method
          try {
            clientOut.writeString(String.format("HTTP/1.1 405 Method Not Allowed\r\n\n \r\n\n%s not supported\r\n",reqMethod));
            clientOut.flush();
            // clientOut.close();
            // os.close();
            // socket.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
      }
      
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally{
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
  };

  
}
