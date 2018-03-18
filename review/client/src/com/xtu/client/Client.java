package com.xtu.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端
 * @author 黄宇  
 */
public class Client extends Thread{
	
	private EnterFrame enterFrame;
	public String username ;
	private Client client;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private int threadID;
	private ChatFrame chatFrame;
	public boolean flag_exit = false;
	private String mess;//客户端读取来自服务器的消息
	public EnterFrame getEnterFrame() {
		return enterFrame;
	}
	public void setEnterFrame(EnterFrame enterFrame) {
		this.enterFrame = enterFrame;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	//主方法  程序的入口
	public static void main(String[] args){
		//创建客户端对象
		Client client = new Client();
		client.setClient(client);
		//创建窗口对象
		EnterFrame enterFrame = new EnterFrame(client);
		enterFrame.setVisible(true);//让窗口显示
	}
	
	public String login(String username,String ip ,String post){
		this.username = username;
		//在登陆方法中创建Socket 对象
		try {
			socket = new Socket(ip,Integer.parseInt(post));
		} catch (NumberFormatException e) {
			return "端口号必须在 1024 到 65535 之间";
		} catch (UnknownHostException e) {
			return "服务器地址错误";
		} catch (IOException e) {
			return "连接异常";
		}
		return "true";
	}
	
	/**
	 * 打开聊天界面
	 * @param username 当前登陆的用户名 显示在窗口上
	 */
	public void showChatFrame(String username) {
		//初始化io流
		initData();
		ChatFrame chatFrame = new ChatFrame(username,client);
		this.chatFrame = chatFrame;
		chatFrame.setVisible(true);
	}
	
	private void initData() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 客户端不停地接收来自服务端的信息
	 */
	public void run(){
		while(flag_exit){
			//客户端读取数据
			try {
				 mess = dis.readUTF();
				 System.out.println(mess);
				//如果信息中包含@clientThread 说明是第二次握手
				if(mess.contains("@clientThread")){
					//切割消息的内容
					int index = mess.indexOf("@clientThread");
					threadID = Integer.parseInt(mess.substring(0,index));
					//将用户名信息发送给服务器
					dos.writeUTF(username+"@login"+threadID+"@login");
				}else{
					if(mess.contains("@chat")){
						chatFrame.setDisMess(mess);
					}else{
						if(mess.contains("@serverexit")){
							chatFrame.exitServer();
						}else{
							if(mess.contains("@userlist")){
								chatFrame.setDisUser(mess);
							}
						}
					}
				}
			} catch (IOException e) {
				mess = null;
				this.flag_exit = false;
			}
		}
	}
	// 发送数据
	public void tranMess(String mess) {
		try {
			dos.writeUTF(username+"@chat"+threadID+"@chat"+mess+"@chat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 突出聊天
	public void exitChat() {
		if(socket!=null){
			try {
				socket.close();
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//该方法在客户端退出聊天室的时候调用
	public void exitClient() {
		try {
			dos.writeUTF(username+"@exit"+threadID+"@exit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getThreadID() {
		return threadID;
	}
}
