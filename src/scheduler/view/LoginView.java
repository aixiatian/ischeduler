package scheduler.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import scheduler.SchedulerContext;
import scheduler.SchedulerUtil;


public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	JLabel userLabel = null;
	JLabel errLabel = null;
	JTextField userText = null;
	JLabel pwdLabel = null;
	JTextField pwdText = null;
	JButton loginButton = null;
	ButtonGroup bg;
	JRadioButton nom;
	JRadioButton test;
	SchedulerContext sm;
	OperateView ov ;

	public OperateView getOv() {
		return ov;
	}

	public void setOv(OperateView ov) {
		this.ov = ov;
	}

	public SchedulerContext getSm() {
		return sm;
	}

	public LoginView(){
	      this.setSize(300, 250); 
	      this.getContentPane().setLayout(null); 
	      this.add(errLabel = SchedulerUtil.getJLabel("",10, 10, 70, 18), null);
	      errLabel.setForeground(Color.red);
	      this.add(getRadioGroup());
	      this.add(userLabel = SchedulerUtil.getJLabel("UserName",34, 60, 70, 18), null); 
	      this.add(userText = SchedulerUtil.getJTextField("",96, 60, 160, 20), null); 
	      this.add(pwdLabel = SchedulerUtil.getJLabel("Password",34, 80, 70, 18), null); 
	      this.add(pwdText = SchedulerUtil.getPwdTextField("",96, 80, 160, 20), null); 
	      this.add(loginButton = SchedulerUtil.getButton("Login",103, 130, 71, 27), null); 
	      setLoginBtnListener();
	      this.setTitle("Schadualer");
	      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      this.addKeyListener(new KeyAdapter() {
	    	  @Override
	    	public void keyPressed(KeyEvent e) {
	    		  if(e.getKeyCode() != KeyEvent.VK_ENTER)
	    		  super.keyPressed(e);
	    		  else
	    			  login();
	    	}
		});
	}
	
	public static void main(String[] args) {
		LoginView l = new LoginView();
		l.setVisible(true);
	}
	
	private void setLoginBtnListener(){
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				login();
			}
		});
	}
	@Override
	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			login();
		}
	}
	
	public boolean login(){
		String username = userText.getText();
		String pwd = pwdText.getText();
		int idx = 0;
		if(nom.isSelected())
			idx = 1;
		
		sm = new SchedulerContext(idx);
		boolean suc = sm.login(username, pwd);
		if(!suc)
			errLabel.setText("µÇÂ¼Ê§°Ü!");
		else{
			errLabel.setText("");
			this.setVisible(false);
			ov = new OperateView(sm);
			ov.setVisible(true);
		}
		return suc;
	}
	
	private JPanel getRadioGroup(){
		JPanel jp = new JPanel(new FlowLayout());
		jp.setSize(300, 30);
		test = new JRadioButton("²âÊÔ", true);
		test.setBounds(34, 20, 20, 30);
		jp.add(test);
		nom = new JRadioButton("ÕýÊ½", false);
		nom.setBounds(60, 20, 20, 30);
		jp.add(nom);
		bg = new ButtonGroup();
		bg.add(test);
		bg.add(nom);
		jp.setBounds(34, 20, 200, 30);
		return jp;
	}
}
