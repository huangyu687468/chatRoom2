package com.xtu.server;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 服务器界面
 * @author Administrator
 */
public class ServerFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Server server;
	private JButton jbt_start ;// 启动按钮
	private JButton jbt_stop;//停止按钮
	private JButton jbt_exit;//退出按钮
	private JScrollPane jsp_mess;//显示消息的界面
	private JScrollPane jsp_user;
	private JTextArea jta_mess;//
	private JList jlt_user;
	
	public ServerFrame(Server server) {
		this.server = server;
		
		this.setTitle("服务器");
//		this.setSize(449, 301);
		this.setBounds(300, 200, 449, 301);
		this.setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_exit.doClick();
			}
		});
		
		this.setLayout(null);
		
		jbt_start = new JButton("启动服务器");
		jbt_start.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_start.setBounds(32, 23, 103, 34);
		jbt_start.addActionListener(this);
		this.add(jbt_start);
		
		jbt_stop = new JButton("停止服务器");
		jbt_stop.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_stop.setBounds(145, 23, 103, 34);
		jbt_stop.setEnabled(false);
		jbt_stop.addActionListener(this);
		this.add(jbt_stop);
		
		jbt_exit = new JButton("退出服务器");
		jbt_exit.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_exit.setBounds(258, 23, 103, 34);
		jbt_exit.addActionListener(this);
		this.add(jbt_exit);
		
		jsp_mess = new JScrollPane();
		jsp_mess.setBorder(BorderFactory.createTitledBorder("消息记录"));
		jsp_mess.setBounds(10, 64, 221, 192);
		jsp_mess.setWheelScrollingEnabled(false);
		this.add(jsp_mess);
		
		jta_mess = new JTextArea();
		jta_mess.setEditable(false);
		jsp_mess.setViewportView(jta_mess);
		
		jsp_user = new JScrollPane();
		jsp_user.setBorder(BorderFactory.createTitledBorder("在线用户"));
		jsp_user.setBounds(258, 65, 157, 191);
		jsp_user.setWheelScrollingEnabled(false);
		this.add(jsp_user);
		
		jlt_user = new JList();
		//jlt_user.setVisibleRowCount(4);
		jsp_user.setViewportView(jlt_user);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//判断事件源
		if(e.getSource() == jbt_start){
			jbt_start.setEnabled(false);//启动按钮失效
			jbt_stop.setEnabled(true);
			server.startServer();
		}
		if(e.getSource() == jbt_stop){
			jbt_start.setEnabled(true);//启动按钮生效
			jbt_stop.setEnabled(false);
			server.stopServer();
		}
		if(e.getSource() == jbt_exit){
			jbt_stop.doClick();// 退出前先关
			System.exit(0);
		}
	}

	public void showMessage() {
		JOptionPane.showMessageDialog(this, "不能同时开启两个服务器");
	    System.exit(0);
	}

	//将聊天信息显示到服务器界面
	public void setDisMess(String mess) {
		if(mess.contains("@chat")){
			int index = mess.indexOf("@chat");
			mess = mess.substring(0,index);
			jta_mess.append(mess);
			jta_mess.setCaretPosition(jta_mess.getText().length());
		}
		if(mess.contains("@exit")){
			//说明服务器已经关闭，清空聊天记录
			jta_mess.setText("");
		}
	}

	/**
	 * 在服务器显示在线用户
	 * @param mess
	 */
	public void setDisUser(String mess) {
		System.out.println(mess+"===================");
		if(mess.contains("@userlist")){
			//得到的字符串数组 保存的是用户名和线程id
			String[] info = mess.split("@userlist");
			String[] info2 = new String[info.length/2];
			//定义一个参数来控制info2
			int j = 0;
			for(int i = 0;i<info.length;i++){
				info2[j++] = info[i++];
			}
			jlt_user.removeAll();
			jlt_user.setListData(info2);
		}
	}
}

