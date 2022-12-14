package Chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/* << processChatReqThread >>
 * 생성 장소
 * GetChatReqThread의 run() 메서드
 * 
 * 기능
 * 1) 채팅 수락 후 채팅 기능 활성화
 * 2) 채팅 거절 기능 활성화
 */

public class ProcessChatReqThread implements Runnable {
	// Net
	static Socket connectionSocket = null;
	
	// Input
	String status = null;
	Scanner sc = new Scanner(System.in);
	BufferedReader in = null;
	PrintWriter out = null;
	// Constructor
	public ProcessChatReqThread(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {
		System.out.println("채팅을 수락하시겠습니까? (Y/N)");
		status = sc.next(); // 대소문자 관계 없이 Y or N로 대답
		
		// 채팅을 수락한 경우 
		if(status.equalsIgnoreCase("Y")) {
			try {
				in = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(this.connectionSocket.getOutputStream()));
				
				out.write("Y/");
				out.flush();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		// 채팅을 수락하지 않은 경우 
		
	}

}
