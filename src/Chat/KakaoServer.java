package Chat;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KakaoServer{
	
	// manage serverThread list
	List<Thread> serverThreadList;
	
	// Net
	static ServerSocket serverSocket = null;
	static Socket connectionSocket = null;
	
	// Constructor
	public KakaoServer() {
		serverThreadList = new ArrayList<Thread>();         
	}
	
	// Open server
	// setReuseAddress : 아직 ServerSocket을 열기만 하고 TCP 연결을 하는 test를 하지 않았지만
	// 이론상, TCP 연결 후 접속을 닫으면, 접속이 
	public void openServer() {
		try {
			int portN = 8888;
			serverSocket = new ServerSocket(portN);
			serverSocket.setReuseAddress(true);  
			System.out.println("KakaoServer is opened with port " + serverSocket.getLocalPort() + ".");
			
			while(true) {				
				connectionSocket = serverSocket.accept();
				
				// connection success, then create KakaoServerThread and run it
				Thread serverThread = new Thread(new KakaoServerThread(connectionSocket));
				serverThread.start();
				serverThreadList.add(serverThread); 
                
                System.out.println("\n--now Server-thread running state--");
				for(int i = 0; i < serverThreadList.size(); i++) {
					Thread nowThread = serverThreadList.get(i);
                	State state = nowThread.getState();
                	System.out.println(nowThread.getName() + "'s state: " + state);
                }
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// Server 닫기
	public void closeServer() {		
		if(serverSocket != null) {
			try {
				serverSocket.close();
				System.out.println("KakaoServer is closed.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
