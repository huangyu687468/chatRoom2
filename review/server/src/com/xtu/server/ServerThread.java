package com.xtu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 服务器线程类  用来跟每个客户端建立连接
 * @author Administrator
 */
public class ServerThread extends Thread{
	
	private ServerSocket serverSocket;
	public ServerFrame serverFrame;
	public boolean flag_exit = false;
	//保存服务器与每个客户端的连接
	public Vector<ClientThread> clients;
	//保存 服务器需要发送给客户端的信息
	public Vector<String> message;
	//保存每个客户端的线程id 和用户名
	public Map<Integer,String> users;
	public ServerThread(ServerFrame serverFrame) {
		clients = new Vector<ClientThread>();
		message = new Vector<String>();
		users = new HashMap<Integer,String>();
		this.serverFrame = serverFrame;
		//创建ServerSocket对象
		try {
			serverSocket = new ServerSocket(5000);
		} catch (IOException e) {
			serverFrame.showMessage();
		}
		
		// 开启向客户端广播的线程类
		BroadCast broadCast = new BroadCast(this);
		broadCast.flag_exit = true;
		broadCast.start();
	}
	
	//重写run方法
	public void run(){
		//在该方法中，服务器需要不断地接收来自客户端的连接请求
		Socket socket ;
		while(flag_exit){
			//通过ServerSocket 获取Socket对象
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				flag_exit = false;
				socket = null;
			}
			if(socket!=null){
				ClientThread clientThread = new ClientThread(socket,this);
				clientThread.flag_exit = true;
				//开启线程
				clientThread.start();
				
				// 保存每个用户对应的clientThread对象
				synchronized(clients){
					clients.addElement(clientThread);
				}
				
				//为了完成第二次握手，服务器忘客户端发送的东西
				// 线程id
				synchronized(message){
					//获取线程id 
					int threadID = (int)clientThread.getId();
					users.put(threadID, "@login@");//保存
					message.add(threadID+"@clientThread");
				}
			}
			
		}
	}

	public void stopServer() {
		if(this.isAlive()){
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
