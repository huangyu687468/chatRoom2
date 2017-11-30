package com.xtu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * �������߳���  ������ÿ���ͻ��˽�������
 * @author Administrator
 */
public class ServerThread extends Thread{
	
	private ServerSocket serverSocket;
	public ServerFrame serverFrame;
	public boolean flag_exit = false;
	//�����������ÿ���ͻ��˵�����
	public Vector<ClientThread> clients;
	//���� ��������Ҫ���͸��ͻ��˵���Ϣ
	public Vector<String> message;
	//����ÿ���ͻ��˵��߳�id ���û���
	public Map<Integer,String> users;
	public ServerThread(ServerFrame serverFrame) {
		clients = new Vector<ClientThread>();
		message = new Vector<String>();
		users = new HashMap<Integer,String>();
		this.serverFrame = serverFrame;
		//����ServerSocket����
		try {
			serverSocket = new ServerSocket(5000);
		} catch (IOException e) {
			serverFrame.showMessage();
		}
		
		// ������ͻ��˹㲥���߳���
		BroadCast broadCast = new BroadCast(this);
		broadCast.flag_exit = true;
		broadCast.start();
	}
	
	//��дrun����
	public void run(){
		//�ڸ÷����У���������Ҫ���ϵؽ������Կͻ��˵���������
		Socket socket ;
		while(flag_exit){
			//ͨ��ServerSocket ��ȡSocket����
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				flag_exit = false;
				socket = null;
			}
			if(socket!=null){
				ClientThread clientThread = new ClientThread(socket,this);
				clientThread.flag_exit = true;
				//�����߳�
				clientThread.start();
				
				// ����ÿ���û���Ӧ��clientThread����
				synchronized(clients){
					clients.addElement(clientThread);
				}
				
				//Ϊ����ɵڶ������֣����������ͻ��˷��͵Ķ���
				// �߳�id
				synchronized(message){
					//��ȡ�߳�id 
					int threadID = (int)clientThread.getId();
					users.put(threadID, "@login@");//����
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
