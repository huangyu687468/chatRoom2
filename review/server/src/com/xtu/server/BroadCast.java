package com.xtu.server;

import java.io.IOException;

/**
 * ����������ͣ����ͻ��˹㲥����
 * @author Administrator
 */
public class BroadCast extends Thread{

	private ServerThread serverThread;
	public boolean flag_exit = false;
	
	public BroadCast(ServerThread serverThread) {
		this.serverThread = serverThread;
	}
	
	public void run(){
		//�����ͻ��˷��������أ�
		while(flag_exit){
			boolean flag = true;
			//���������ó����ݣ����ж��������Ƿ�������
			if(serverThread.message.isEmpty()){
				continue;
			}else{
				String mess;
				//ͬ������飬һ���߳��ڲ�����ʱ������
				//������̲߳���
				synchronized(serverThread.message){
					//��ȡ��һ����Ϣ
					mess = serverThread.message.firstElement();
					System.out.println(mess);
					//ɾ������Ϣ
					serverThread.message.remove(mess);
					if(mess.contains("@clientThread")){
						flag = false;
					}
				}
				synchronized(serverThread.clients){
					//�����̼߳��� ȡ��ÿһ���߳�
					for(int i = 0; i<serverThread.clients.size();i++){
						//����߳�ID 
						ClientThread clientThread = serverThread.clients.get(i);
						int threadID = (int)clientThread.getId();
						if(flag){
//							System.out.println(mess);
							if(mess.contains("@chat")||mess.contains("@serverexit")||mess.contains("@userlist")){
								try {
//									System.out.println(11);
									//���͸��ͻ���
									clientThread.dos.writeUTF(mess);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							String value = serverThread.users.get(threadID);
							//��������߳�id�õ�ֵ���ǡ�@login@��˵����û����ɵ�¼
							if("@login@".equals(value)){
								System.out.println(value);
								//����mess ��ɵڶ�������
								try {
									flag = true;
									clientThread.dos.writeUTF(mess);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				
			}
		}
	}
}