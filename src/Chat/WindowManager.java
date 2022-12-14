package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WindowManager implements Runnable{
	// User Info
	String user_id = null;
	static int logout = 0;
	
	// Net
	ServerSocket listeningSocket = null;
	Socket connectionSocket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	String inMsg = "0";
	String outMsg = null;
	
	// WindowManager Constructor
	public WindowManager(ServerSocket listeningSocket, String user_id) {
		this.listeningSocket = listeningSocket;
		this.user_id = user_id;
	}
	
	// Thread를 inner class로 만들었다.
	class WindowManagerThread implements Runnable {
		static MainGUI mainGUI = null; // 쓰레드까지는 mainGUI 하나가지고 공유를 해야하기 때문에 MainGUI를 stataic으로 한다
		Socket connectionSocket = null;
		
		// Constructor
		public WindowManagerThread(Socket connectionSocket) {
			this.connectionSocket = connectionSocket;
		}
		
		@Override
		public void run() {	
			System.out.println("window manater Thread 시작");
			try {
				 String responseMsg = null;
				 in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				 out = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
				 
				 // 기억하자! 서버쪽에서 여기로 화면을 제어하는 활동을 다 하고 나면 반드시 FIN을 보내서 끊자!!!!!!!!!!!
				 while(!inMsg.equals("FIN")) {
					 // receive response from server
					 inMsg = in.readLine();
					 
					 if(inMsg.equals("FIN"))
						 break;
					 
					 System.out.println("WindowManaterThread: Server Window Control command: " + inMsg);
					 
					 if(inMsg.equals("LOGOUT")) {
						 logout = 1; // 이게 잘 넘어가겠지?
						 break;      // 로그아웃은 FIN개념을 포함한다.
					 }
					 
					 switch (inMsg) {
					 	case "FirstMain":
					 		WindowManagerThread.mainGUI = new MainGUI(user_id);
					 		responseMsg = "FIN" + "\n";
					 		break;
					 		
				     	case "NewUserIn":
				     		WindowManagerThread.mainGUI.disposeWindow();
				     		WindowManagerThread.mainGUI = new MainGUI(user_id);
				     		responseMsg = "FIN" + "\n";
				     		break;
				     		
						case "Update_UserInfo":
							WindowManagerThread.mainGUI.disposeWindow();
				     		WindowManagerThread.mainGUI = new MainGUI(user_id);
				     		responseMsg = "FIN" + "\n";
				     		break;
							
						case "OpenChatRoom":
							break;
						
						case "CloseChatRoon":
							break;
					}
					 System.out.println("WindowManager: outmsg:" + responseMsg);
					 out.write(responseMsg);
					 out.flush();
					 System.out.println("WindowManager: flush 완료");
				 }

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Window managerthread 끝");
		}
	}

	@Override
	public void run() {
		while(true) {
			// KakaoServer도 이런 식으로 나가는거 해줘야 함!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// 그리고 나가거나 없어졌을 때 디비 업데이트도 해줘야 하고 !!!!!!!!!!!!!!!!!1
			// 여기 뿐만 아니라 다른 곳에서도
			if(logout == 1) break;
				
			try {
				System.out.println("window manager고 지금 서버로부터 창 관련 요청 받고있어");
				connectionSocket = listeningSocket.accept();
				Thread windowMgrThread = new Thread(new WindowManagerThread(connectionSocket));
				windowMgrThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
			
			// 여기에 도달할 때는 로그아웃이 된 경우 
			// 로그아웃 한 결과 도달하는 코드이기 때문에 전체 창을 다 없애야 한다.
			// 따라서 dispose하지 않고 exit()를 한다. dispose와 eixt()의 차이는 dispose는 한 개의 창만 지우는 것이라면 exit()는 프로그램 종료를 뜻한다.
			// 즉, 전체 창을 다 지운다.
			
			// 일단 그 전에 정보를 다 지우고 떠나야 한다!!!!!!!!!!!!]
			// 정보 지우는 거는 이따 채팅 다 하고 하면 된다!!!!!!!!!!!!!!!!
			
			// 정보지우기
			
			// EXIT
		
	}
}


