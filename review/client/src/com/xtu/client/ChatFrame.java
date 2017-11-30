package com.xtu.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ChatFrame extends JFrame implements ActionListener,KeyListener{

	private static final long serialVersionUID = 1L;
	private Client client;
	private JButton jbt_exit;
	private JButton jbt_clear;
	private JButton jbt_tran;
	private JTextField jtf_mess;
	private JTextArea jta_mess;
	private JList jlt_user;
	private JScrollPane jsp_mess;
	private JScrollPane jsp_user;
	
	
	public ChatFrame(String username,Client client) {
		this.client = client;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setTitle("聊天室"+"  "+username);
		this.setResizable(false);
		this.setSize(450, 325);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_exit.doClick();
			}
		});
		
		this.setLayout(null);
		jsp_mess = new JScrollPane();
		jsp_mess.setBorder(BorderFactory.createTitledBorder("聊天消息"));
		jsp_mess.setBounds(10, 10, 283, 167);
		jsp_mess.setWheelScrollingEnabled(true);
		this.add(jsp_mess);
		jta_mess = new JTextArea();
		jta_mess.setEditable(false);
		jsp_mess.setViewportView(jta_mess);
		
		//输入框
		jtf_mess = new JTextField();
		jtf_mess.setBounds(10, 242, 192, 32);
		jtf_mess.addKeyListener(this);
		this.add(jtf_mess);
		//发送按钮
		jbt_tran = new JButton("发送");
		jbt_tran.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_tran.addActionListener(this);
		jbt_tran.addKeyListener(this);
		jbt_tran.setBounds(212, 241, 93, 32);
		this.add(jbt_tran);
		
		//清除消息按钮
		jbt_clear = new JButton("清除消息");
		jbt_clear.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_clear.addActionListener(this);
		jbt_clear.setBounds(158, 187, 135, 37);
		this.add(jbt_clear);
		
		//退出聊天室按钮
		jbt_exit = new JButton("退出聊天室");
		jbt_exit.setFont(new Font("宋体",Font.PLAIN,14));
		jbt_exit.setBounds(20, 189, 128, 37);
		jbt_exit.addActionListener(this);
		this.add(jbt_exit);
		
		//右边显示在线用户
		jsp_user = new JScrollPane();
		jsp_user.setBorder(BorderFactory.createTitledBorder("在线用户"));
		jsp_user.setBounds(303, 10, 128, 214);
		this.add(jsp_user);
		jlt_user = new JList();
		//jlt_user.setVisibleRowCount(4);
		jsp_user.setViewportView(jlt_user);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbt_tran){
			String mess = jtf_mess.getText().trim();
			if(!mess.equals("")){
				client.tranMess(mess);
				jtf_mess.setText("");
			}else{
				JOptionPane.showMessageDialog(this, "不能发送空消息");
			}
		}
		if(e.getSource() == jbt_clear){
			jta_mess.setText("");
		}
		if(e.getSource() == jbt_exit){
			client.exitClient();
		}
	}
	
	//将信息 显示在聊天界面
	public void setDisMess(String mess) {
		int index = mess.indexOf("@chat");
		mess = mess.substring(0,index);
		jta_mess.append(mess);
		jta_mess.setCaretPosition(jta_mess.getText().length());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			jbt_tran.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	//表示服务器退出
	public void exitServer() {
		JOptionPane.showMessageDialog(this, "服务器已经停止");
		client.flag_exit = false;
		client.exitChat();
	}

	//讲在线用户显示在客户端
	public void setDisUser(String mess) {
		if(mess.contains("@userlist")){
			//得到的字符串数组 保存的是用户名和线程id
			String[] info = mess.split("@userlist");
			String[] info2 = new String[info.length/2];
			//定义一个参数来控制info2
			for (int i=1;i<info.length;i++) {
				int id_user=0;
				try {
					id_user=Integer.parseInt(info[i]);
					if (client.getThreadID() == id_user) {
						if (!client.username.equals(info[i-1])) {
							JOptionPane.showMessageDialog(this,
									"由于有同名的用户登录，所以您的用户名后面加上了编号");
							client.username = info[i - 1];
							this.setTitle("聊天室    " + client.username);
							break;
						}else break;
					}else i++;
				}catch(Exception e) {
					
				}
			}
			int j = 0;
			for(int i = 0;i<info.length;i++){
				info2[j++] = info[i++];
			}
			jlt_user.removeAll();
			jlt_user.setListData(info2);
		}
	}
}
