package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import model.Message;
import model.Node;
import model.User;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RoomForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6768358705199090686L;
	private JPanel contentPane;
	private JTextField txtContent;
	private JTextArea txtHistory;
	private JPanel list;
	private JLabel lblTitle;
	
	private Node me;
	private InetAddress host;
	private HashMap<String, User> users;
	private HashMap<String, JLabel> labels;
	
	/**
	 * Create the frame.
	 */
	public RoomForm(Node me, InetAddress host, User user) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				handleWindowClosed();
			}
		});
		this.me=me;
		this.host=host;
		this.users=new HashMap<String, User>();
		this.labels=new HashMap<String, JLabel>();
		
		setTitle("Room "+this.me.getGroup().getHostAddress()+":"+this.me.getPort());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		txtHistory = new JTextArea();
		txtHistory.setWrapStyleWord(true);
		txtHistory.setEditable(false);
		txtHistory.setLineWrap(true);
		((DefaultCaret)txtHistory.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollTxt=new JScrollPane(txtHistory);
		
		contentPane.add(scrollTxt, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 10, 0));
		
		txtContent = new JTextField();
		txtContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					handleBtnSend();
				}
			}
		});
		panel.add(txtContent);
		txtContent.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleBtnSend();
			}
		});
		panel.add(btnSend);
		
		list = new JPanel();
		FlowLayout flowLayout = (FlowLayout) list.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		flowLayout.setHgap(50);
		contentPane.add(list, BorderLayout.NORTH);
		
		Thread worker=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						String msg=new String(me.receive(1450), "UTF-8");
						String head=msg.substring(0, 1);
						msg=msg.substring(1);
						switch (head) {
						case "0":
							if(!msg.equals("")) {
								txtHistory.append(msg+"\n");
								while(txtHistory.getLineCount()>1000) {
									txtHistory.replaceRange("", 0, txtHistory.getLineEndOffset(0));
								}
							}
							break;
						case "1":
							//msg=1.2.3.4?name?20?sex?phone
							String[] token1=msg.split("\\?");
							User temp;
							users.put(token1[0], temp=new User(token1[1], Integer.parseInt(token1[2]), token1[3], token1[4]));
							addToList(token1[0], temp);
							break;
						case "2":
							//msg=host has left :(
							String[] token2=msg.split(" ");
							String lHost=token2[0];
							users.remove(lHost);
							removeFromList(lHost);
							break;
						default:
							break;
						}
						Thread.sleep(300);
					} catch (InterruptedException | IOException | NullPointerException | BadLocationException e) {
						// TODO Auto-generated catch block
						break;
					}
				}
			}
		});
		worker.start();
		try {
			this.me.send(("1"+host.getHostAddress()+"?"+user.getName()+"?"+user.getAge()+"?"+user.getSex()+"?"+user.getPhone()).getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lblTitle = new JLabel("Users: ");
		list.add(lblTitle);
	}

	private void removeFromList(String lHost) {
		// TODO Auto-generated method stub
		list.remove(labels.get(lHost));
		list.revalidate();
		list.repaint();
		users.remove(lHost);
		labels.remove(lHost);
	}

	private void addToList(String lHost, User user) throws IOException {
		// TODO Auto-generated method stub
		JLabel lblTemp = new JLabel(user.getName());
		lblTemp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new UserInfoForm(user).setVisible(true);
			}
		});
		if(labels.containsKey(lHost)) return;
		this.me.send(("1"+host.getHostAddress()+"?"+user.getName()+"?"+user.getAge()+"?"+user.getSex()+"?"+user.getPhone()).getBytes());
		labels.put(lHost, lblTemp);
		list.add(lblTemp);
		list.revalidate();
		list.repaint();
	}
	
	protected void handleWindowClosed() {
		// TODO Auto-generated method stub
		try {
			FileWriter w=new FileWriter("chat-"+new Message().getCreatedTime()+".log", true);
			w.write(txtHistory.getText());
			w.close();
			
			this.me.send(("2"+this.host.getHostAddress()+" has left :(").getBytes());
			this.me.leave();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void handleBtnSend() {
		// TODO Auto-generated method stub
		Message msg=new Message(this.host.getHostAddress(), this.txtContent.getText());
		try {
			this.me.send(("0"+msg.toString()).getBytes());
			this.txtContent.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
