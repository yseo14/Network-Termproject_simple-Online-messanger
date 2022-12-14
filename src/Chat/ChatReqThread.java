package Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/************* FOR CLINET *****************/

public class ChatReqThread implements Runnable {
   // ChatRoom Info
   String requester_id = null;
   String counterpart_name = null;
   String counterpart_id = null;
   String counterpart_ip = null;
   int counterpart_port = -1;
   
   Scanner sc = new Scanner(System.in);
   
   // Net 
   Socket socket = null;
   BufferedReader in = null;
   BufferedWriter out = null;
   String inMsg = null;
   String outMsg = null;
   
   // DB
   Statement stmt = null;
   ResultSet rs = null;
   PreparedStatement pstm = null;
   
   // Constructor: default
   public ChatReqThread(String requester_id, String counterpart_name) {
      this.requester_id = requester_id;
      this.counterpart_name = counterpart_name;
   }
      
   // create Chat_RequestMsg
   private String createChatReqMsg(String counterPartID) {
      String separator = "/";
      String reqMsg = "CHATWITH" + separator + counterPartID + "\n";
         
      return reqMsg;
   }

   @Override
   public void run() {
	    // 쓰레드 처리하지 말고 그냥 여기서 하자!
   		// 디비에서 상대방 정보를 받아오자.
   		try (Connection conn = JDBC.connection()) {
   			stmt = conn.createStatement();
   			System.out.println("ChatReqThread(63): " + counterpart_name);
   			
   			String receiveQry = "select user_id from user where name = \'" + counterpart_name + "\'";
   			rs = stmt.executeQuery(receiveQry);
   			rs.next();
   			counterpart_id = rs.getString(1);
   			
   			receiveQry = "select ip, port from getchatreqinfo where user_id = " + "\'" + counterpart_id + "\'";
   			rs = stmt.executeQuery(receiveQry);

   			rs.next();
   			String gcr_ip = rs.getString(1);
   			int gcr_port = rs.getInt(2);
   			System.out.println("Get ChatRequest id: " + gcr_ip + "port: " + gcr_port);

   		} catch (SQLException e) {
   			e.printStackTrace();
   		}
      
   		String serverIP = "localhost";
   		int serverPort = 8888;
   		
        try {
        Socket socket = new Socket(serverIP, serverPort);
           in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           
           // send chat-requestMsg with counterPartID to Server
           outMsg = createChatReqMsg(counterpart_name);
           System.out.println("outMsg ChatReqThread 90:" + outMsg);
           out.write(outMsg);
           out.flush();
           
           System.out.println("ChatReqThread: 서버로 부터 채팅 요청 메시지를 보내고 기다리는 중입니다.");
           inMsg = in.readLine();
           System.out.println("ChatReqThread: 서버로부터 메시지가 도착했습니다." + inMsg);
           
           if(inMsg.equals("Y")) {
              String localhost = "localhost";
              
              //요청자 채팅방만들기
              ServerSocket serverSocket_requester = new ServerSocket(0);
              int requesterPort = serverSocket_requester.getLocalPort();
              
              ChatRoom ChatRoom_requester = new ChatRoom(serverSocket_requester);
              
              //피요청자 채팅방 만들기
              ServerSocket serverSocket_participant = new ServerSocket(0);
              int participantPort = serverSocket_participant.getLocalPort();
              
              // DB넘기기
              try (Connection con = JDBC.connection())
               {
                   String s2 = "insert into Chat (req_ip, req_port, part_ip, part_port) values (" + localhost + "," + participantPort + "," + localhost +  "," +requesterPort + ")";
                   pstm = con.prepareStatement(s2);
                   pstm.executeUpdate();
               } catch (SQLException E) {
                   E.printStackTrace();
               }
              
              ChatRoom ChatRoom_participant = new ChatRoom(serverSocket_participant);
              
              
              //채팅 매니저 시작하기 
              Thread chatManagerThread = new Thread(new ChatManagerThread(requester_id, counterpart_id));
              chatManagerThread.start();
           }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    
   	
         
         
         // receive response from server
         // 채팅방 요청자가 자신의 KakaoServerThread에게 채팅 요청을 보내고 응답을 기다리는 중
         // 어떤 응담이 와야할까?
         // (1) 채팅이 수락된다면, ChatManagerThread가 만들어질 것이고 아니야 그냥 TCP연결할까?
         // 
         // (2) 채팅이 거절된다면,  
 		// 만약 거절이면 거절 메시지 보내고 끝내.
 		// 즉, ChatReqThread를 없애자.????
         
               
   }
}