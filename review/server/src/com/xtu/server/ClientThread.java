package com.xtu.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务端用来不断地接收和发送信息   向客户端
 * 每一个客户端都会对应一个该类的对象
 * @author Administrator
 */
public class ClientThread extends Thread{
	
	public DataInputStream dis;//用来读取数据
	public DataOutputStream dos; // 向客户端发送数据
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
	
	// 该方法会不断地接收数据，保存到message中
	public void run(){
		while(flag_exit){
			try {
				String mess = dis.readUTF();//
				System.out.println(mess);
				if(mess.contains("@login")){
					//字符串切割成字符串数组
					String[] userInfo = mess.split("@login");
					int threadID = Integer.parseInt(userInfo[1]);
					System.out.println("========");
					//将用户名保存到  users
					int userID = Integer.parseInt(userInfo[1]);
					serverThread.users.remove(threadID);
					if(serverThread.users.containsValue(userInfo[0])){
						int userid=0;
						for(int i = 0; i < serverThread.clients.size(); i++){
							int id = (int)serverThread.clients.get(i).getId();//得到每个线程的id
							if(serverThread.users.get(id).equals(userInfo[0])){//得到线程id对应的用户名
								serverThread.users.remove(id);
								serverThread.users.put(id, userInfo[0] + "_" + (++userid));//处理重名
								break;
							}
						}
						serverThread.users.put(Integer.parseInt(userInfo[1]), userInfo[0] + "_" +(++userid));
					}else{
						serverThread.users.put(userID, userInfo[0]);
					}
					//此时用户名全部保存到了map集合  users中
					//接下来从users集合中取出每个用户名
					StringBuffer sb = new StringBuffer();
					synchronized(serverThread.clients){
						for(int i = 0;i<serverThread.clients.size();i++){
							//获得每个线程id
							System.out.println("----------");
							userID = (int)serverThread.clients.get(i).getId();
							//根据线程id获取用户名
							String username = serverThread.users.get(userID);
							sb.append(username+"@userlist"+userID+"@userlist");
						}
					}
					
					mess = sb.toString();
					//将用户显示在服务器界面的右边
					serverThread.serverFrame.setDisUser(mess);
				}else{
					if(mess.contains("@chat")){
						String[] str = mess.split("@chat");
						//将信息显示到服务器界面
						StringBuffer sb  = new StringBuffer();
						String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						sb.append(str[0]+"   "+date+"\n");
						sb.append(str[2]+"\n"+"@chat");
						mess = sb.toString();
						//将消息显示到服务器界面
						serverThread.serverFrame.setDisMess(mess);
					}else{
						//表示客户端退出
						
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
				//将信息保存到容器中
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
