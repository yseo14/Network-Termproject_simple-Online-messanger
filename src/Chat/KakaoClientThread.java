package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KakaoClientThread implements Runnable {
   // client Info
   String id = null;
   String pw = null;
   String wantToChat = "No";
   String wantToLogout = "No";
   Scanner sc = new Scanner(System.in);
   
   // Net
   String serverIP = "localhost";
   int serverPort = 8888;
   static Socket socket = null;
   BufferedReader in = null;
   PrintWriter out = null;
   String inMsg = null;
   String outMsg = null;
   
   // Msg
   String separator = "/";
   
   // Constructor: default
   public KakaoClientThread(String id, String pw) {
      this.id = id;
      this.pw =pw;
   }
   
   /* Request Msg */
   private String createConnectionEndMsg() {
	   String reqMsg = "FIN" + "\n";
	   return reqMsg;
   }
   
   // create Login_RequestMsg
   private String createLoginReqMsg(String id, String pw) {
      String reqMsg = "LOGIN" + separator + id + separator + pw + "\n";
      return reqMsg;
   }
   
   // create WindowManager 생성했고, 해당 정보(windowManager ip, port) DB에 넣어달라고 요청하는 메시지
   private String createLoginReqMsg(String id, String ip, int port) {
      String reqMsg = "INSERT_WindowManagerInfo" +  separator + id + separator + ip + separator + port + "\n";
      return reqMsg;
   }
   
   // create getChatReqThread 생성요청메시지
   private String createGetChatReqMsg() {
	   String reqMsg = "CREATE_GetChatReqThread" + "\n";
       return reqMsg;
   }
   
   private String createMainGUIReqMsg(String id2) {
	   String reqMsg = "SHOW_MainGUI" +  separator + id + "\n";
       return reqMsg;
   }

   @Override
   public void run() {         
      try  {
    	 Socket socket = new Socket(serverIP, serverPort);
		 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		 out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	   	 
	   	 // send login-requestMsg to Server
	     outMsg = createLoginReqMsg(id, pw);
	     out.write(outMsg);
	     out.flush();
	     
	     // receive response from server
	     inMsg = in.readLine();
	     System.out.println(inMsg);
	     
	     // if login allowed
		 if(inMsg.equals("LOGIN_ALLOWED")) {
			 // Window Manager 생성
			 ServerSocket listeningSocket = new ServerSocket(0);
			 Thread windowManager = new Thread(new WindowManager(listeningSocket, id));
			 windowManager.start();
			 
			 String ip = "localhost"; // 자신 IP
			 int port = listeningSocket.getLocalPort();
			 
			 // 서버에게 해당 정보를 넣어달라고 요청한다.
			 outMsg = createLoginReqMsg(id, ip, port);
			 out.write(outMsg);
			 out.flush();
			 
			 inMsg = in.readLine();
			 
			 // 서버한테 이거 만들었으니까 mainGUI 띄워달라고 말해줘야돼
			 outMsg = createMainGUIReqMsg(id);
			 out.write(outMsg);
			 out.flush();
			 
			 inMsg = in.readLine();

			// 서버에게 getChatReqThread 실행 메시지 보내기
		    outMsg = createGetChatReqMsg();
		    out.write(outMsg);
		    out.flush();
		    
		    // receive response from server
	        inMsg = in.readLine();
	        
	        if(inMsg.equals("CREATED_getChatReqThread")) {
		    	// 정리해주고 닫아주는 건 나중에
	        }	
		 } 
	  } catch (IOException e) {
	     e.printStackTrace();
	  }
      System.out.println("쓰레드 종료");
   }

}