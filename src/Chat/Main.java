package Chat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JList;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public Main(String id) {
		initialize(id);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String id) {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(240, 230, 140));
		menuPanel.setBounds(0, 0, 386, 130);
		frame.getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("1");
		btnNewButton.setBounds(10, 35, 84, 72);
		menuPanel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent E)
            {
            	try {
        			InetAddress ia = InetAddress.getLocalHost();
        			String ip_str = ia.toString();
        			String ip = ip_str.substring(ip_str.indexOf("/") + 1);
        			
        			 //new ClientGui(id, ip, 5420);
        			 System.out.println(id);
        			 frame.setVisible(false);
        		} catch (UnknownHostException e) {
        			e.printStackTrace();
        		}
    			
            }
        });
		JButton btnNewButton_1 = new JButton("2");
		btnNewButton_1.setBounds(104, 35, 84, 72);
		menuPanel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent E)
            {
            	// 이버튼은 방을 만드는 버튼입니다 
            	//Roommanager thread = new Roommanager(socket);
    			//thread.start();
            	//Roommanager.createRoom() --> run 함수 안에서 실행  
            	
            }
        });
		JButton btnNewButton_2 = new JButton("3");
		btnNewButton_2.setBounds(196, 35, 84, 72);
		menuPanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("4");
		btnNewButton_3.setBounds(290, 35, 84, 72);
		menuPanel.add(btnNewButton_3);
		
		JLabel titleLabel = new JLabel("Chat");
		titleLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		titleLabel.setBounds(0, 0, 110, 27);
		menuPanel.add(titleLabel);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(192, 192, 192));
		mainPanel.setBounds(0, 130, 386, 533);
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JScrollPane activeScroll = new JScrollPane();
		activeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		activeScroll.setBounds(10, 10, 366, 291);
		mainPanel.add(activeScroll);
		
		JPanel userPanel_1 = new JPanel();
		activeScroll.setViewportView(userPanel_1);
		userPanel_1.setLayout(new BoxLayout(userPanel_1, BoxLayout.Y_AXIS));
		
		//DB 연동 후 활동 상태인 친구 목록을 list에 추가
		String[] friendList= {"id1","id2","id3","id4","id5","id6","id1","id2","id3","id4","id5","id6","id1","id2","id3","id4","id5","id6"};
 		JList list = new JList(friendList);
 		list.setBackground(new Color(255, 250, 205));
 		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userPanel_1.add(list);
		
		JScrollPane inactiveScroll = new JScrollPane();
		inactiveScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		inactiveScroll.setBounds(10, 311, 366, 212);
		mainPanel.add(inactiveScroll);
		
		JPanel userPanel_2 = new JPanel();
		inactiveScroll.setViewportView(userPanel_2);
	}
	
	public static void main(String[] args) {
		new Main(null);
	}
}
