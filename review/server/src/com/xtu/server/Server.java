package com.xtu.server;

/**
 * ������
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
		//��������������
		Server server = new Server();
		ServerFrame serverFrame = new ServerFrame(server);
		server.setServerFrame(serverFrame);
		serverFrame.setVisible(true);
	}

	/**
	 * ����������
	 */
	public void startServer() {
		serverThread = new ServerThread(serverFrame);
		serverThread.flag_exit = true;// �򿪿���
		serverThread.start();
	}

	//ֹͣ������
	public void stopServer() {
		//���߿ͻ���  �������Ѿ�ֹͣ
		synchronized(serverThread.message){
			serverThread.message.addElement("@serverexit");
		}
		
		//�÷��������������Ϣ
		serverFrame.setDisMess("@exit");
		serverThread.stopServer();
	}

}