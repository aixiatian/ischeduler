package scheduler.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import scheduler.SchedulerContext;
import scheduler.SchedulerUtil;

public class OperateView extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JTabbedPane jtp;
	
	boolean isstart = false;
	
	SchedulerContext sm;
	
	public SchedulerContext getSm() {
		return sm;
	}

	public void setSm(SchedulerContext sm) {
		this.sm = sm;
	}
	StartTaskPanel stp ;
	TipPanel tip;
	RetryTaskPanel retry;
	JLabel tbl ;
	SearchTaskPanel serchp;
	public OperateView(SchedulerContext sm){
		this.sm = sm;
		this.setSize(300, 520); 
	      this.getContentPane().setLayout(new BorderLayout()); 
	      jtp = new JTabbedPane();
	      jtp.setSize(300, 500);
	      
	      stp = new StartTaskPanel(sm);
	      jtp.addTab("��������", stp);
	      serchp = new SearchTaskPanel(sm);
	      jtp.addTab("������Ŀ", serchp);
	      
	      retry = new RetryTaskPanel(sm);
	      jtp.addTab("��������", retry);
	      
	      tip = new TipPanel();
	      jtp.addTab("˵��", tip);
	      this.add(jtp,BorderLayout.CENTER);
	      this.setTitle("iScheduler");
	      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      this.setResizable(false);
	      this.addWindowListener(new WindowAdapter(){
	    	   public void windowClosing(WindowEvent we){
				   writeTip2Log(stp.getTip_area());
				   writeTip2Log(retry.getTip_area());
				   writeTip2Log(serchp.getTip_area());
	    	   }
	    	  });
	      JToolBar tb = new JToolBar();

	      tb.setBounds(0, 500, 300, 20);
	      JButton bt = new JButton("�л�");
//	      addTooBarListener(this,tb);
	      bt.addActionListener(new ActionListener() {
	    	  
	    	  @Override
	    	  public void actionPerformed(ActionEvent e) {
	    		  // TODO Auto-generated method stub
	    		  swithSys();
	    	  }
	      });
	      tb.add(bt,BorderLayout.WEST);
	      tb.addSeparator();
	      tb.add(tbl = new JLabel((SchedulerUtil.getCurrentScheduler(sm.getHostIdx()))+"����-(��ǰ�û�:"+sm.username+")"),BorderLayout.EAST);
	      if(sm.getHostIdx() == 2){
			  bt.setVisible(false);
		  }
	      this.add(tb,BorderLayout.SOUTH);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("sina.png")));
//		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/sina.png"));
	}
	
	public void swithSys(){
		int r = JOptionPane.showConfirmDialog(this, "ȷ���л���"+(sm.getHostIdx()==1?"����":"��ʽ")+"������","tip",JOptionPane.OK_CANCEL_OPTION);
		if(r == 0){
			try {
				
				sm.setHostIdx((sm.getHostIdx()==1?0:1));
				sm.login("", "");
				tbl.setText((sm.getHostIdx()==1?"��ʽ":"����")+"����-(��ǰ�û�:"+sm.username+")");
			} catch (Exception e) {
				JOptionPane.showConfirmDialog(this, "ȷ���л���"+(sm.getHostIdx()==1?"����":"��ʽ")+"����ʧ�ܣ�","tip",-1);
			}
		}
	}

	public void writeTip2Log(TextArea tx){
		if(tx != null && !"".equals(tx.getText()))
			SchedulerUtil.writeLog(tx.getText(), System.currentTimeMillis()+"_close");
	}
	
	public static void main(String[] args) {
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		new OperateView(null).setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
