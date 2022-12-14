package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KakaoServerThread implements Runnable {
	// connected ClientThrad Into
	String user_id = null;
	String user_pw = null;
	BufferedReader in2 = null;
	PrintWriter out2 = null;
	// manage serverThread list
	List<Thread> chatManagerThreadList;

	// Net
	Socket connectionSocket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	String inMsg = null;
	String outMsg = null;

	// DB
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstm = null;

	// constructor
	public KakaoServerThread(Socket connectionSocket) {
		chatManagerThreadList = new ArrayList<Thread>();
		this.connectionSocket = connectionSocket;
	}
	
	// Request
	private String createFirstGUIRqtMsg() {
		return "FirstMain" + "\n";
	}
	
	private String finishconnectionRqtMsg() {
		return "FIN" + "\n";
	}
	

	// Response
	private String createResponseMsg(String RqstMsg) {
		String separator = "/";
		String[] parsingResult = null;
		String responseMsg = null;

		parsingResult = inMsg.split(separator);

		switch (parsingResult[0].toString()) {
		case "LOGIN":
			// check if received client-info is valid
			responseMsg = checkLogin(parsingResult);
			System.out.println("Login case responseMsg: " + responseMsg);
			break;
		case "CHATWITH":
			// check if received client-info is
			// 내(서버)가 상대방이 채팅이 가능한지->원하는지 물어보고 응답을 가져온다.
			// 가능하면 이미 상대방의 채팅방은 만들어져 있을 것이고 여기(서버)에서 내 채팅방 만들고
			// 응답메시지로 팝업창을 띄워줄 수 있도록 명령을 내린다...
			// 연결을 한 걸 넣고 알아서 한다.???
			// 불가능하면 그냥 거기서 끝
			//
			responseMsg = startChat(parsingResult);
			System.out.println("CHATWITH case responseMsg: " + responseMsg);
			break;
		case "CREATE_GetChatReqThread":
			// create and startGetChatReqThread
			responseMsg = startGetChatReq();
			System.out.println("CREATE_GetChatReqThread case responseMsg: " + responseMsg);
			break;
		case "FollowerInfo": // 유저 팔로워 해야한다.!!!!!!!!
			responseMsg = giveFollowerinfo(parsingResult[1]);
			System.out.println("FollowerInfo case responseMsg: " + responseMsg);
			break;
		case "UserInfo":
			responseMsg = giveUserinfo(parsingResult[1]);
			System.out.println("UserInfo case responseMsg: " + responseMsg);
			break;
		case "INSERT_WindowManagerInfo":
			responseMsg = insertWndMgrInfo(parsingResult[1], parsingResult[2], parsingResult[3]);
			System.out.println("INSERT_WindowManagerInfo case responseMsg: " + responseMsg);
			break;
		case "SHOW_MainGUI":
			responseMsg = showMainGUI(parsingResult[1]);
			System.out.println("SHOW_MainGUI case responseMsg: " + responseMsg);
			break;
		}

		/*
		 * if(parsingResult[0].equals("LOGIN")) { // check if received client-info is
		 * valid responseMsg = checkLogin(parsingResult); }
		 * 
		 * if(parsingResult[0].equals("CHATWITH")) { }
		 */

		return responseMsg;
	}

	private String checkLogin(String[] parsingResult) {
		user_id = parsingResult[1];
		user_pw = parsingResult[2];

		byte[] remoteIp_byte = connectionSocket.getInetAddress().getAddress();
		String remoteIp = new String(
				remoteIp_byte[0] + "." + remoteIp_byte[1] + "." + remoteIp_byte[2] + "." + remoteIp_byte[3]);
		int remotePort = connectionSocket.getPort();
		String responseMsg = null;

		try (Connection conn = JDBC.connection()) {
			stmt = conn.createStatement();
			String checkQry = "select user_id from user where user_id = \"" + user_id + "\" and pwHash = \"" + user_pw
					+ "\"";
			rs = stmt.executeQuery(checkQry);

			if (rs.next()) {
				// store user connection info
				String insertConnInfoQry = "insert into user_connection (user_id, ip, port)" + " values (\'" + user_id
						+ "\', \'" + remoteIp + "\'," + remotePort + ")";
				try {
					pstm = conn.prepareStatement(insertConnInfoQry);
					pstm.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				responseMsg = "allowed" + "\n";

				String active = "active";

				String update = "Update user set status = \'" + active
						+ "\', login_num = login_num +1 where user_id = \'" + user_id + "\'";
				try {
					pstm = conn.prepareStatement(update);
					pstm.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				try {
					pstm = conn.prepareStatement(insertConnInfoQry);
					pstm.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				responseMsg = "LOGIN_ALLOWED" + "\n";
			} else {
				responseMsg = "LOGIN_REGECTED" + "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return responseMsg;
	}

	private String startGetChatReq() {
		String responseMsg = null;
		
		ServerSocket listeningSocket;
		try {
			listeningSocket = new ServerSocket(0);
			String listeningIP = "locahost";
			int listenPort = listeningSocket.getLocalPort();
			
			// DB에 정보 넣기
			String insertQry = "insert into getchatreqinfo (user_id, ip, port)" + " values (\'" + user_id
					+ "\', \'" + listeningIP + "\'," + listenPort + ")";
			try (Connection conn = JDBC.connection()){
				pstm = conn.prepareStatement(insertQry);
				pstm.executeUpdate();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			Thread getChatReqThread = new Thread(new GetChatReqThread(user_id, listeningSocket));
			getChatReqThread.start();
			
			responseMsg = "CREATED_getChatReqThread" + "\n";
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		return responseMsg;
	}

	private String startChat(String[] parsingResult) {
		String counterPartId = parsingResult[1];
		
		Thread chatReqThread = new Thread(new ChatReqThread(user_id, counterPartId));
		chatReqThread.start();
	
		System.out.println("Server측에서 채팅 피요청자(" + counterPartId + "에게 채팅 요청허눈 Thread룰 만들었습니다. ");

		return "ChatReqStart" + "\n";
	}

	private String giveFollowerinfo(String id) {
		String data = null;
		try (Connection con = JDBC.connection()) {

			String s3 = "select Name, today_words, status from user where user_id in (select follower_id from Following where followee_id = \'"
					+ id + "\')";
			stmt = con.createStatement();
			rs = stmt.executeQuery(s3);

			StringBuilder sb = new StringBuilder();

			int id_count = 0;
			while (rs.next()) {
				id_count++;
				sb.append(rs.getString("name"));
				sb.append("/");
				sb.append(rs.getString("today_words"));
				sb.append("/");
				sb.append(rs.getString("status"));
				sb.append("/");
			}

			sb.append("\n");

			data = sb.toString();
			String count = Integer.toString(id_count);
			data = count + "/" + data;
			System.out.println(data.length());

		} catch (SQLException E) {
			E.printStackTrace();
		}

		return data;
	}
	
	private String giveUserinfo(String id) {
		String data = null;
		StringBuilder sb = new StringBuilder();
		
		try (Connection con = JDBC.connection()) {

			String s3 = "select Name, today_words, status from user where user_id  = \'" + id + "\'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(s3);

			rs.next();
			sb.append(rs.getString("name"));
			sb.append("/");
			sb.append(rs.getString("today_words"));
			sb.append("/");
			sb.append(rs.getString("status"));
			sb.append("\n");

			data = sb.toString();
			System.out.println("giveUserInfo: " + data.length());

		} catch (SQLException E) {
			E.printStackTrace();
		}

		return data;
	}
	
	private String insertWndMgrInfo(String id, String ip, String port) {
		int porti = Integer.parseInt(port);
		
		try (Connection con = JDBC.connection()) {

			String s3 = "insert into windowmanagerinfo (user_id, ip, port) values (\'" + id + "\', \'" + ip + "\', " + porti + ")";
			System.out.println("윈도우 메니저 정보 넣기 쿼리: " + s3);
			pstm = con.prepareStatement(s3);
			pstm.executeUpdate();
		} catch (SQLException E) {
			E.printStackTrace();
		}
		
		return "Insert Complete" + "\n";
		
	}
	
	private String showMainGUI(String id) {
		String windowManagerIP = null;
		int windowManagerPort = 0;
		String outMsg = null;
		String inMsg = "0";
		
		// window 정보 얻어와서
		try (Connection con = JDBC.connection()) {

			String s = "select ip, port from windowmanagerinfo where user_id  = \'" + id + "\'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(s);

			rs.next();
			windowManagerIP = rs.getString("ip");
			windowManagerPort = rs.getInt("port");
		} catch (SQLException E) {
			E.printStackTrace();
		}
		
		// TCP 연결하고
		try(Socket socket = new Socket(windowManagerIP, windowManagerPort)) {
			in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out2 = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// 요청해서 Main 띄우고 
			outMsg = createFirstGUIRqtMsg();
			out2.write(outMsg);
			out2.flush();
			System.out.println("Main 띄우도록 클라이언트 창 매니저에게 지시");
			
			inMsg = in2.readLine();
			System.out.println("showMainGUI의 inMsg(298): " + inMsg);
			
			// 연결 끊는 메시지 보내기
			outMsg = finishconnectionRqtMsg();
			out2.write(outMsg);
			out2.flush();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Complete: Create ShowGUI/ from KakaoServer/showMainGUI(311)" + "\n";
	}

	@Override
	public void run() {
		System.out.println("서버쓰레드 구동");
		try {
			in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

			while(true) {
				// receive requestMsg from client
				System.out.println("다시 inMsg기다리는 중");
				inMsg = in.readLine();
				System.out.println("inMsg: " + inMsg);
				
				if(inMsg.equals("FIN")) {
					
						System.out.println("cliet에서" + inMsg + "보냄");
						break;
				}
						
				// Send Response Msg to User Client
				outMsg = createResponseMsg(inMsg);
				System.out.println("outMsg: " + outMsg);
				out.write(outMsg);
				out.flush();
				System.out.println("KakaoServerThread: flush 완료");
			}
			System.out.println("Thread 종료됨");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}