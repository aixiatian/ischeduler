package scheduler.view;

import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import scheduler.SchedulerContext;
import scheduler.SchedulerUtil;

public class RetryTaskPanel  extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
SchedulerContext sm;
	
	JLabel tip ;
	
	JTextField shellName;
	
	JButton search;
	JButton stop;
	
	TextArea tip_area;
	
	JRadioButton retryradio ;
	JRadioButton passradio ;
	
	public RetryTaskPanel(SchedulerContext sm){
		this.sm = sm;
		this.setSize(300, 500);
		this.setLayout(null);
		this.add(tip = SchedulerUtil.getJLabel("请输入项目名",10, 80, 150, 18), null); 
		this.add(shellName = SchedulerUtil.getJTextField("",10, 100, 270, 23), null); 
		this.add(search = SchedulerUtil.getButton("开始",80, 130, 70, 27),null);
		this.add(stop = SchedulerUtil.getButton("停止",160, 130, 70, 27),null);
		addSearchListener();
		addStopListener();
		this.add(getTipPanel());
		this.add(getRadioGroup());
	    tip_area.setEditable(false);
	}
	
	private TextArea getTipPanel(){
		tip_area = new TextArea();
		tip_area.setBounds(5, 180, 280, 230);
		tip_area.setVisible(true);
		return tip_area;
	}
	
	private void addSearchListener(){
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
//					retry(); 
					tip_area.setText("");
					sm.setOv(tip_area);
					String projname = shellName.getText().trim();
					new Thread(new RetryThread(retryradio.isSelected(), projname)).start();;
				} catch (Exception e2) {
					System.err.println(e2.getMessage());
				}
				search.setEnabled(true);
			}
		});
	}
	private void addStopListener(){
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stop.setEnabled(false);
				try {
					tip_area.append("正在停止重试...\n");
					sm.setStop(true); 
				} catch (Exception e2) {
					System.err.println(e2.getMessage());
				}
				stop.setEnabled(true);
				search.setEnabled(true);
			}
		});
	}
	public void retry(){
		sm.setOv(tip_area);
		String projname = shellName.getText().trim();
		/*if(!SchedulerUtil.isStrNull(projname)){
			if(!projname.contains(":")) {
				sm.retryFailedNodes(projname,retryradio.isSelected());
			}else {
				String[] paras = projname.split(":",-1);
				String starth = "";
				String endh = "";
				String pjname = paras[1];
				if(paras.length > 2) {
					starth = paras[1];
					endh = paras[2];
					pjname = paras[3];
				}
				sm.autoRetry(pjname, starth, endh, SchedulerUtil.str2int(paras[0], 3));
			}
		}*/
		new RetryThread(retryradio.isSelected(), projname).run();
	}
	
	class RetryThread implements Runnable{
		boolean isretry ;
		String projname ;
		
		public RetryThread(boolean isretry,String projname) {
			this.isretry = isretry;
			this.projname = projname;
		}
		@Override
		public void run() {
			
			if(!SchedulerUtil.isStrNull(projname)){
				search.setEnabled(false);
				if(!projname.contains(":")) {
					sm.retryFailedNodes(projname,isretry);
				}else {
					String[] paras = projname.split(":",-1);
					String starth = "";
					String endh = "";
					String pjname = paras[1];
					if(paras.length > 2) {
						starth = paras[1];
						endh = paras[2];
						pjname = paras[3];
					}
					sm.autoRetry(pjname, starth, endh, SchedulerUtil.str2int(paras[0], 3));
				}
				search.setEnabled(true);
			}
			
		}
		
	}
	
	private JPanel getRadioGroup(){
		JPanel jp = new JPanel(new FlowLayout());
		jp.setSize(300, 30);
		/*JLabel j = new JLabel("执行周期:");
		j.setBounds(10, 20, 20, 20);
		jp.add(j);*/
		
		retryradio = new JRadioButton("重试");
		retryradio.setSelected(true);
		passradio = new JRadioButton("跳过");
		
		jp.add(retryradio);
		jp.add(passradio);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(retryradio);
		bg.add(passradio);
		
//		jp.add(bg);
//		r_day = new Checkbox("天", true);
//		r_day.setBounds(50, 20, 20, 30);
//		jp.add(r_day);
//		r_week = new Checkbox("周", false);
//		r_week.setBounds(100, 20, 20, 30);
//		jp.add(r_week);
//		r_month = new Checkbox("月", false);
//		r_month.setBounds(150, 20, 20, 30);
//		jp.add(r_month);
		/*bg = new ButtonGroup();
		bg.add(r_day);
		bg.add(r_week);
		bg.add(r_month);*/
		jp.setBounds(10, 50, 230, 30);
		return jp;
	}
}
