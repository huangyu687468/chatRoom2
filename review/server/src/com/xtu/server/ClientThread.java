package com.xtu.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ������������ϵؽ��պͷ�����Ϣ   ��ͻ���
 * ÿһ���ͻ��˶����Ӧһ������Ķ���
 * @author Administrator
 */
public class ClientThread extends Thread{
	
	public DataInputStream dis;//������ȡ����
	public DataOutputStream dos; // ��ͻ��˷�������
	private Socket socket;
	private ServerThread serverThread;
	public boolean flag_exit = false;
	public ClientThread(Socket socket, ServerThread serverThread) {
		this.socket = socket;
		this.serverThread = serverThread;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// �÷����᲻�ϵؽ������ݣ����浽message��
	public void run(){
		while(flag_exit){
			try {
				String mess = dis.readUTF();//
				System.out.println(mess);
				if(mess.contains("@login")){
					//�ַ����и���ַ�������
					String[] userInfo = mess.split("@login");
					int threadID = Integer.parseInt(userInfo[1]);
					System.out.println("========");
					//���û������浽  users
					int userID = Integer.parseInt(userInfo[1]);
					serverThread.users.remove(threadID);
					if(serverThread.users.containsValue(userInfo[0])){
						int userid=0;
						for(int i = 0; i < serverThread.clients.size(); i++){
							int id = (int)serverThread.clients.get(i).getId();//�õ�ÿ���̵߳�id
							if(serverThread.users.get(id).equals(userInfo[0])){//�õ��߳�id��Ӧ���û���
								serverThread.users.remove(id);
								serverThread.users.put(id, userInfo[0] + "_" + (++userid));//��������
								break;
							}
						}
						serverThread.users.put(Integer.parseInt(userInfo[1]), userInfo[0] + "_" +(++userid));
					}else{
						serverThread.users.put(userID, userInfo[0]);
					}
					//��ʱ�û���ȫ�����浽��map����  users��
					//��������users������ȡ��ÿ���û���
					StringBuffer sb = new StringBuffer();
					synchronized(serverThread.clients){
						for(int i = 0;i<serverThread.clients.size();i++){
							//���ÿ���߳�id
							System.out.println("----------");
							userID = (int)serverThread.clients.get(i).getId();
							//�����߳�id��ȡ�û���
							String username = serverThread.users.get(userID);
							sb.append(username+"@userlist"+userID+"@userlist");
						}
					}
					
					mess = sb.toString();
					//���û���ʾ�ڷ�����������ұ�
					serverThread.serverFrame.setDisUser(mess);
				}else{
					if(mess.contains("@chat")){
						String[] str = mess.split("@chat");
						//����Ϣ��ʾ������������
						StringBuffer sb  = new StringBuffer();
						String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						sb.append(str[0]+"   "+date+"\n");
						sb.append(str[2]+"\n"+"@chat");
						mess = sb.toString();
						//����Ϣ��ʾ������������
						serverThread.serverFrame.setDisMess(mess);
					}else{
						//��ʾ�ͻ����˳�
						
						if(mess.contains("@exit")){
//							String[] str = mess.split("@exit");
//							int threadID = Integer.parseInt(str[1]);
//							serverThread.users.remove(threadID);
//							StringBuffer sb = new StringBuffer();
//							synchronized(serverThread.clients){
//								for(int i = 0; i<serverThread.clients.size();i++){
//									ClientThread clientThread = serverThread.clients.get(i);
//									int userID = (int)clientThread.getId();
//									if(str[0].equals(serverThread.users.get(userID))){
//										serverThread.clients.remove(clientThread);
//										i--;
//									}else{
//										sb.append()
//									}
//								}
//							}
						}
					}
				}
				//����Ϣ���浽������
				synchronized(serverThread.message){
					//System.out.println(33);
					if( mess!= null){
						//System.out.println(mess);
//						serverThread.message.add(mess);
						serverThread.message.addElement(mess);
					}
				}
			} catch (IOException e) {
				
			}
			
		}
	}
}