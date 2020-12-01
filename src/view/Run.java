package view;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Utils;
import model.Node;
import model.User;

public class Run extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4675656334916718284L;
	private JPanel contentPane;
	private JTextField txtPort;
	private JTextField txtBroadcastIP;
	private JComboBox comboSex;
	private JTextField txtName;
	private JTextField txtAge;
	private JTextField txtPhone;

	private Node me;
	private InetAddress host;
	private Set<String> connectedRoom;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Run frame = new Run();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Run() {
		
		try {
			this.host=Utils.getLocalIP();
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connectedRoom=new HashSet<String>();
		
		setTitle("Chat Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 15));
		
		JLabel lblNewLabel = new JLabel("Port *");
		contentPane.add(lblNewLabel);
		
		txtPort = new JTextField();
		txtPort.setText("1501");
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Broadcast IP *");
		contentPane.add(lblNewLabel_1);
		
		txtBroadcastIP = new JTextField();
		txtBroadcastIP.setText("224.0.0.15");
		contentPane.add(txtBroadcastIP);
		txtBroadcastIP.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Name");
		contentPane.add(lblNewLabel_3);
		
		txtName = new JTextField();
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Age");
		contentPane.add(lblNewLabel_4);
		
		txtAge = new JTextField();
		contentPane.add(txtAge);
		txtAge.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Sex");
		contentPane.add(lblNewLabel_5);
		
		comboSex = new JComboBox();
		comboSex.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female", "Other"}));
		contentPane.add(comboSex);
		
		JLabel lblNewLabel_6 = new JLabel("Phone");
		contentPane.add(lblNewLabel_6);
		
		txtPhone = new JTextField();
		contentPane.add(txtPhone);
		txtPhone.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("");
		contentPane.add(lblNewLabel_2);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleBtnJoin();
			}
		});
		contentPane.add(btnJoin);
	}

	protected void handleBtnJoin() {
		// TODO Auto-generated method stub
		try {
			final int port=Integer.parseInt(this.txtPort.getText());
			final String ip=this.txtBroadcastIP.getText();
			/*if(this.connectedRoom.contains(port+":"+ip)) {
				JOptionPane.showMessageDialog(this, "You are already in this room", "Info", JOptionPane.INFORMATION_MESSAGE);
				return;
			}*/
			this.connectedRoom.add(port+":"+ip);
			this.me=new Node(port, ip);
			this.me.run();
			this.me.send(("0New player joined: "+host.getHostAddress()+" :)").getBytes());
			
			if(this.txtName.getText().equals("")) this.txtName.setText(host.getHostAddress());
			if(this.txtAge.getText().equals("")) this.txtAge.setText("-1");
			User user=new User(this.txtName.getText(), Integer.parseInt(this.txtAge.getText()), (String)this.comboSex.getSelectedItem(), this.txtPhone.getText());
			RoomForm rf=new RoomForm(me, host, user);
			rf.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					connectedRoom.remove(port+":"+ip);
				}
			});
			rf.setVisible(true);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Port is a number", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Broadcast IP not match", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

}
