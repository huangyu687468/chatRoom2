package com.xtu.server;

/**
 * 服务器
 * @author Administrator
 *
 */
public class Server {
	private ServerThread serverThread;
	private ServerFrame serverFrame;
	
	public ServerFrame getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public static void main(String[] args) {
		//创建服务器对象
		Server server = new Server();
		ServerFrame serverFrame = new ServerFrame(server);
		server.setServerFrame(serverFrame);
		serverFrame.setVisible(true);
	}

	/**
	 * 启动服务器
	 */
	public void startServer() {
		serverThread = new ServerThread(serverFrame);
		serverThread.flag_exit = true;// 打开开关
		serverThread.start();
	}

	//停止服务器
	public void stopServer() {
		//告诉客户端  服务器已经停止
		synchronized(serverThread.message){
			serverThread.message.addElement("@serverexit");
		}
		
		//让服务器界面清除消息
		serverFrame.setDisMess("@exit");
		serverThread.stopServer();
	}

}
