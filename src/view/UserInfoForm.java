package view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.User;

public class UserInfoForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -787468217522110922L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAge;
	private JTextField txtSex;
	private JTextField txtPhone;

	/**
	 * Create the frame.
	 */
	public UserInfoForm(User user) {
		setTitle(user.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 220);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 15));
		
		JLabel lblNewLabel = new JLabel("Name");
		contentPane.add(lblNewLabel);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setText(user.getName());
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Age");
		contentPane.add(lblNewLabel_1);
		
		txtAge = new JTextField();
		txtAge.setEditable(false);
		txtAge.setText(user.getAge()+"");
		contentPane.add(txtAge);
		txtAge.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Sex");
		contentPane.add(lblNewLabel_2);
		
		txtSex = new JTextField();
		txtSex.setEditable(false);
		txtSex.setText(user.getSex());
		contentPane.add(txtSex);
		txtSex.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Phone");
		contentPane.add(lblNewLabel_3);
		
		txtPhone = new JTextField();
		txtPhone.setEditable(false);
		txtPhone.setText(user.getPhone());
		contentPane.add(txtPhone);
		txtPhone.setColumns(10);
	}

}
