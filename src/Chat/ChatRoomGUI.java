package Chat;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JTextArea;

public class ChatRoomGUI {

	private JFrame frame;
	private JTextField msgTf;
	private JButton enterBtn;
	private JPanel chatPanel;
	private JButton exitBtn;
	
	JTextArea chatArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomGUI window = new ChatRoomGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatRoomGUI() {
		String text = null;
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		msgTf = new JTextField();
		msgTf.setHorizontalAlignment(SwingConstants.LEFT);
		msgTf.setBounds(0, 409, 228, 54);
		frame.getContentPane().add(msgTf);
		msgTf.setColumns(10);
		
		enterBtn = new JButton("전송");
		enterBtn.setBounds(226, 409, 60, 28);
		//enterBtn.addActionListener(new ActionListener() {
	      //   public void actionPerformed(ActionEvent e) {
	        //	 getConversation(msgTf.getText());
	          //}
	       //});
		
		frame.getContentPane().add(enterBtn);
		
		chatPanel = new JPanel();
		chatPanel.setBackground(new Color(255, 255, 255));
		chatPanel.setBounds(0, 0, 286, 405);
		frame.getContentPane().add(chatPanel);
		chatPanel.setLayout(null);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setBackground(new Color(176, 196, 222));
		chatArea.setBounds(0, 10, 286, 395);
		chatPanel.add(chatArea);
		
		exitBtn = new JButton("나가기");
		exitBtn.setFont(new Font("굴림", Font.PLAIN, 8));
		exitBtn.setBounds(226, 435, 60, 28);
		frame.getContentPane().add(exitBtn);
		frame.setVisible(true);
	}
	
	public boolean isEntered() {
		System.out.println("isEntered");
		if(msgTf.getText() == null)
			return false;
		else 
			return true;
	}
		
	
	public void addConversation(String conversation) {
		chatArea.append(conversation + "\n");
	}
	
	public String getConversation() {
		
		return msgTf.getText();
	}
}
