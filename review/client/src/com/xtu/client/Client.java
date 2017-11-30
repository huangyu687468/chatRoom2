package com.xtu.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * �ͻ���
 * @author ����   
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
	private String mess;//�ͻ��˶�ȡ���Է���������Ϣ
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
	//������  ��������
	public static void main(String[] args){
		//�����ͻ��˶���
		Client client = new Client();
		client.setClient(client);
		//�������ڶ���
		EnterFrame enterFrame = new EnterFrame(client);
		enterFrame.setVisible(true);//�ô�����ʾ
	}
	
	public String login(String username,String ip ,String post){
		this.username = username;
		//�ڵ�½�����д���Socket ����
		try {
			socket = new Socket(ip,Integer.parseInt(post));
		} catch (NumberFormatException e) {
			return "�˿ںű����� 1024 �� 65535 ֮��";
		} catch (UnknownHostException e) {
			return "��������ַ����";
		} catch (IOException e) {
			return "�����쳣";
		}
		return "true";
	}
	
	/**
	 * ���������
	 * @param username ��ǰ��½���û��� ��ʾ�ڴ�����
	 */
	public void showChatFrame(String username) {
		//��ʼ��io��
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
	 * �ͻ��˲�ͣ�ؽ������Է���˵���Ϣ
	 */
	public void run(){
		while(flag_exit){
			//�ͻ��˶�ȡ����
			try {
				 mess = dis.readUTF();
				 System.out.println(mess);
				//�����Ϣ�а���@clientThread ˵���ǵڶ�������
				if(mess.contains("@clientThread")){
					//�и���Ϣ������
					int index = mess.indexOf("@clientThread");
					threadID = Integer.parseInt(mess.substring(0,index));
					//���û�����Ϣ���͸�������
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
	// ��������
	public void tranMess(String mess) {
		try {
			dos.writeUTF(username+"@chat"+threadID+"@chat"+mess+"@chat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// ͻ������
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
	
	//�÷����ڿͻ����˳������ҵ�ʱ�����
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