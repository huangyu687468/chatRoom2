package com.xtu.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * ��½����
 * @author Administrator
 */
public class EnterFrame extends JFrame implements ActionListener{

	//�������
	private JLabel jlb_user;//�û���
	private JLabel jlb_ip;//ip��ַ
	private JLabel jlb_post;//�˿ں�
	private JTextField jtf_user;
	private JTextField jtf_ip;
	private JTextField jtf_post;
	private JButton jbt_enter;
	private JButton jbt_exit;
	
	
	
	private Client client ;
	public EnterFrame(Client client) {
		this.client = client;
		// ���ô�С
		this.setBounds(300, 200, 320, 280);
		this.setLayout(null);
		this.setResizable(false);
		this.setTitle("������");
		// ������ں�XX��ʱ��  �����ķ�Ӧ
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				jbt_exit.doClick();
			}
		});
		
		jlb_user = new JLabel("�û���");
		jlb_user.setFont(new Font("����",Font.PLAIN,14));
		jlb_user.setBounds(20, 20, 80, 30);
		this.add(jlb_user);// ����ǩ���ӵ���ǰ�Ĵ����м�
		
		jlb_ip = new JLabel("��������ַ");
		jlb_ip.setFont(new Font("����",Font.PLAIN,14));
		jlb_ip.setBounds(20, 70, 80, 30);
		this.add(jlb_ip);// ����ǩ���ӵ���ǰ�Ĵ����м�
		
		jlb_post = new JLabel("�˿ں�");
		jlb_post.setFont(new Font("����",Font.PLAIN,14));
		jlb_post.setBounds(20, 120, 80, 30);
		this.add(jlb_post);// ����ǩ���ӵ���ǰ�Ĵ����м�
		
		jtf_user = new JTextField();
		jtf_user.setBounds(120, 20, 150, 30);
		this.add(jtf_user);
		
		jtf_ip = new JTextField();
		jtf_ip.setBounds(120, 70, 150, 30);
		this.add(jtf_ip);
		//��ȡ������IP��ַ
		try {
			String ip = Inet4Address.getLocalHost().getHostAddress();
			jtf_ip.setText(ip);//��ip��ʾ���ı���
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jtf_post = new JTextField();
		jtf_post.setBounds(120, 120, 150, 30);
		this.add(jtf_post);
		jtf_post.setText("5000");
		
		jbt_exit = new JButton("�˳�������");
		jbt_exit.setFont(new Font("����",Font.PLAIN,14));
		jbt_exit.setBounds(20, 170, 120, 30);
		jbt_exit.addActionListener(this);
		this.add(jbt_exit);
		
		jbt_enter = new JButton("����������");
		jbt_enter.setFont(new Font("����",Font.PLAIN,14));
		jbt_enter.setBounds(160, 170, 120, 30);
		jbt_enter.addActionListener(this);
		this.add(jbt_enter);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//�ж��¼�Դ
		if(e.getSource() == jbt_exit){
			this.setVisible(false);
			System.exit(0);//�ر�JVM
		}
		if(e.getSource() == jbt_enter){
			//��ȡ�����ı��������
			String username = jtf_user.getText().trim();
			String ip = jtf_ip.getText().trim();
			String post = jtf_post.getText().trim();
			if(!username.equals("")){
				if(!ip.equals("")){
					if(!post.equals("")){
						String result = null;
						//���ÿͻ��˵ĵ�½����
						result = client.login(username, ip, post);
						if("true".equals(result)){//˵����½�ɹ�
							this.setVisible(false);
							client.showChatFrame(username);//���������
							client.flag_exit = true;
							client.start();
						}else{
							JOptionPane.showMessageDialog(this, result);
						}
					}else{
						JOptionPane.showMessageDialog(this, "�˿ںŲ���Ϊ�գ�");
					}
				}else{
					JOptionPane.showMessageDialog(this, "ip����Ϊ�գ�");
				}
			}else{
				JOptionPane.showMessageDialog(this, "�û�������Ϊ�գ�");
			}
		}
	}
}