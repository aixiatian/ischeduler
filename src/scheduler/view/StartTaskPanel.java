package scheduler.view;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.SchedulerContext;
import scheduler.SchedulerUtil;
import scheduler.entity.Node;

public class StartTaskPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	/*int num_per_time = 30;//ÿ���ύ�������
	int task_timespan_sec = 2;//�ύ��������֮���ʱ���� ��λ����
	int sleep_for_next = 6;//��������֮��ȴ�ʱ�� ��λ������
	String datefrom = "20150531";
	String dateto = "20160224";
	String projName = "loganalysis_mds_mbportal_apache";//��Ŀ��
	String nodeName = "loganalysis_mds_mbportal_apache_clients";//������
*/	
	JLabel l_num_per_time;
	JLabel l_task_timespan_sec;
	JLabel l_sleep_for_next;
	JLabel l_datefrom;
	JLabel l_dateto;
	JLabel l_projName;
	JLabel l_nodeName;
	JLabel l_status;
	JLabel l_timer_before;
	JLabel l_timer_end;
	JLabel l_influence;
	
	JTextField t_num_per_time;
	JTextField t_task_timespan_sec;
	JTextField t_sleep_for_next;
	JTextField t_datefrom;
	JTextField t_dateto;
	JTextField t_projName;
	JTextField t_nodeName;
	JTextField t_timer;
	
	TextArea tip_area;
	
	Checkbox  r_day;
	Checkbox  r_week;
	Checkbox  r_month;
	Checkbox  r_influence;
//	ButtonGroup bg;
	
	public TextArea getTip_area() {
		return tip_area;
	}

	JButton b_start;
	JButton b_stop;
	
	SchedulerContext sm;
	
	public StartTaskPanel(SchedulerContext sm){
		this.sm = sm;
	      this.setSize(300, 500);
	      this.setLayout(null);
	      this.add(getRadioGroup());
	      this.add(l_num_per_time = SchedulerUtil.getJLabel("����������",10, 40, 140, 18), null); 
	      this.add(t_num_per_time = SchedulerUtil.getJTextField("",150, 40, 60, 20), null); 
	      this.add(l_task_timespan_sec = SchedulerUtil.getJLabel("��������ʱ����(��)",10, 60, 140, 18), null); 
	      this.add(t_task_timespan_sec = SchedulerUtil.getJTextField("",150, 60, 60, 20), null); 
	      this.add(l_sleep_for_next = SchedulerUtil.getJLabel("�������(��)",10, 80, 140, 18), null); 
	      this.add(t_sleep_for_next = SchedulerUtil.getJTextField("",150, 80, 60, 20), null); 
	      this.add(l_datefrom = SchedulerUtil.getJLabel("��ʼʱ�� ��",10, 100, 70, 18), null); 
	      this.add(t_datefrom = SchedulerUtil.getJTextField("",96, 100, 70, 20), null); 
	      t_datefrom.setToolTipText("yyyyMMdd");
	      this.add(l_dateto = SchedulerUtil.getJLabel("��",180, 100, 30, 18), null); 
	      this.add(t_dateto = SchedulerUtil.getJTextField("",200, 100, 70, 20), null); 
	      t_dateto.setToolTipText("yyyyMMdd");
	      this.add(l_timer_before = SchedulerUtil.getJLabel("��ʱ��",10, 121, 50, 18), null); 
	      this.add(t_timer = SchedulerUtil.getJTextField("",96, 121, 50, 18), null); 
	      this.add(l_timer_end = SchedulerUtil.getJLabel("���Ӻ�ʼִ��",150, 121, 100, 18), null); 
	      
	      this.add(l_projName = SchedulerUtil.getJLabel("��Ŀ����",10, 140, 70, 18), null); 
	      this.add(t_projName = SchedulerUtil.getJTextField("",96, 140, 180, 20), null); 
	      this.add(l_nodeName = SchedulerUtil.getJLabel("��������",10, 160, 70, 18), null); 
	      this.add(t_nodeName = SchedulerUtil.getJTextField("",96, 160, 180, 20), null); 
//	      this.add(l_influence = SchedulerUtil.getJLabel("����Ӱ�쵽�Ľڵ�", 10, 190, 120, 18));
	      r_influence = new Checkbox("����Ӱ�쵽�Ľڵ�", false);
		  r_influence.setBounds(13, 185, 210, 20);
		  this.add(r_influence);
	      this.add(b_start = SchedulerUtil.getButton("��ʼ",10, 210, 70, 27), null); 
	      setStartBtnListener();
	      this.add(b_stop = SchedulerUtil.getButton("ֹͣ",90, 210, 70, 27), null);
	      setStopBtnListener();
	      l_status = SchedulerUtil.getJLabel("������",185, 210, 70, 27);
	      l_status.setForeground(Color.red);
	      this.add(l_status, null);
	      this.add(getTipPanel());
	      tip_area.setEditable(false);
	}
	
	
	private TextArea getTipPanel(){
		tip_area = new TextArea();
		tip_area.setBounds(5, 250, 280, 210);
		tip_area.setVisible(true);
		return tip_area;
	}
	
	private JPanel getRadioGroup(){
		JPanel jp = new JPanel(new FlowLayout());
		jp.setSize(300, 30);
		JLabel j = new JLabel("ִ������:");
		j.setBounds(10, 20, 20, 20);
		jp.add(j);
		r_day = new Checkbox("��", true);
		r_day.setBounds(50, 20, 20, 30);
		jp.add(r_day);
		r_week = new Checkbox("��", false);
		r_week.setBounds(100, 20, 20, 30);
		jp.add(r_week);
		r_month = new Checkbox("��", false);
		r_month.setBounds(150, 20, 20, 30);
		jp.add(r_month);
		/*bg = new ButtonGroup();
		bg.add(r_day);
		bg.add(r_week);
		bg.add(r_month);*/
		jp.setBounds(10, 5, 230, 30);
		return jp;
	}
	
	public int waitForTime(int min){
		int res = -1;
		tip_area.append("������" + min + "���Ӻ�ʼ�����Ժ�!\n");
//		int num_per_time = Integer.parseInt(t_num_per_time.getText());
		for (int i = 1; i <= min; i++) {
			
			try {
				/*if(res < num_per_time){
					tip_area.append("���ִ����������"+num_per_time+"��\n���ڵ�ǰ����������������"+res+"�������ˣ���ʼִ��^_^\n");
					break;
				}*/
				Thread.sleep(1*60*1000);
				res = sm.getRunningTaskNum();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tip_area.append("�ѵȴ�"+i+"���ӣ���ǰ����������������"+res+"\n");
			
		}
		return res;
	}
	
	Thread startThread;
	
	private void setStartBtnListener(){
		
		b_start.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				int timer = SchedulerUtil.str2int(t_timer.getText(), 0);
				
				String projName = t_projName.getText();
				String nodeName = t_nodeName.getText();
				System.out.println(getSelectedDWM());
				if(SchedulerUtil.isStrNull(nodeName)){
					if (timer > 0) {
						waitForTime(timer);
					}
					if(startThread == null){
						
						startThread = new Thread(new ProgressStartMuti(tip_area,projName));
						startThread.start();
						System.out.println(startThread.getState());
					}else if(startThread.getState() == Thread.State.TIMED_WAITING)
						startThread.resume();
					else if(Thread.State.NEW == startThread.getState())
						startThread.start();
					else{
						startThread = new Thread(new ProgressStartMuti(tip_area,projName));
						startThread.start();
					}
				}else{
					String tkname = nodeName.trim();
					String hour = "";
					if(tkname.contains("@")){
						String[] tkeles = tkname.split("@");
						tkname = tkeles[0];
						if(tkeles.length==2)
							hour = tkeles[1];
						else
							hour = "00";
					}
					Node node = sm.getDefids(projName, tkname);
					if(node == null){
						JOptionPane.showConfirmDialog(tip_area, "��Ŀ��:"+projName+"\n������:"+nodeName+"\nδ�ҵ���ؽڵ㣡","err",-1);
						return;
					}
					else{
						String content = node.getContent();
						content = content.substring(content.indexOf("/")+1);
						
						int r = -1;
						if(r_influence.getState())
							r = JOptionPane.showConfirmDialog(tip_area, "ȷ��Ҫִ������������Ӱ�쵽�Ľڵ���","��ܰ��ʾ",0);
						else
							r = JOptionPane.showConfirmDialog(tip_area, "ȷ��Ҫִ��"+content+"��","��ܰ��ʾ",0);
						if(r != 0)
							return;
					}	
					if (timer > 0) {
						waitForTime(timer);
					}
					if(startThread == null)
						startThread = new Thread(new ProgressStart(tip_area,node.getId(),hour));
					System.out.println(startThread.getState());
					if(startThread.getState() == Thread.State.TIMED_WAITING)
						startThread.resume();
					else if(Thread.State.NEW == startThread.getState())
						startThread.start();
					else{
						startThread = new Thread(new ProgressStart(tip_area,node.getId(),hour));
						startThread.start();
					}
				}
				l_status.setText("������");
				l_status.setForeground(Color.blue);
			}
		});
	}
	private void setStopBtnListener(){
		b_stop.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(startThread != null)
				startThread.suspend();
			}
		});
	}
	
	class ProgressStart implements Runnable{
		TextArea ov;
		String defids;
		String hour;
		public ProgressStart(TextArea ov,String defids,String hour){
			this.ov = ov;
			this.defids = defids;
			this.hour = hour;
		}
		@Override
		public void run() {
			ov.setText("");
			sm.setOv(ov);
			int num_per_time = Integer.parseInt(t_num_per_time.getText());
			int task_timespan_sec = Integer.parseInt(t_task_timespan_sec.getText());
			int sleep_for_next = Integer.parseInt(t_sleep_for_next.getText());
			
			String datefrom = t_datefrom.getText();
			String dateto = t_dateto.getText();
			if(!r_influence.getState())
				sm.startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, defids,getSelectedDWM(),hour);
			else
				sm.startInfluencedNodes(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, t_projName.getText(), t_nodeName.getText(), getSelectedDWM());
			l_status.setText("�����");
			l_status.setForeground(Color.GREEN);
			String projName = t_projName.getText();
			String nodeName = t_nodeName.getText();
			String content = "��Ŀ����"+projName+"<br>��������"+nodeName+"<br>"+ov.getText().replace("\n", "<br>");
			SchedulerUtil.sendMail(sm.username, "���������������", content);
		}
		
	}
	
	class ProgressStartMuti implements Runnable{
		TextArea ov;
		String projName;
		public ProgressStartMuti(TextArea ov,String projName){
			this.ov = ov;
			this.projName = projName;
		}
		@Override
		public void run() {
			ov.setText("");
			sm.setOv(ov);
			int num_per_time = Integer.parseInt(t_num_per_time.getText());
			int task_timespan_sec = Integer.parseInt(t_task_timespan_sec.getText());
			int sleep_for_next = Integer.parseInt(t_sleep_for_next.getText());
			
			String datefrom = t_datefrom.getText();
			String dateto = t_dateto.getText();
			sm.startMutiTaskBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, projName,getSelectedDWM());
			l_status.setText("�����");
			l_status.setForeground(Color.GREEN);
			SchedulerUtil.sendMail(sm.username, "���������������", ov.getText().replace("\n", "<br>"));
		}
		
	}
	
	public String getSelectedDWM(){
		String res = "";
		if(r_week.getState()){
			res += "week";
		}
		if(r_month.getState()){
			res += "month";
		}
		if(r_day.getState()){
			res += "day";
		}
		return "".equals(res) ? "day" : res;
	}
	
	public static void main(String[] args) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMdd").parse("20160301");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(SchedulerUtil.getDateWeekDay(date));
		System.out.println(SchedulerUtil.getDateDay(date));
		System.out.println(new StartTaskPanel(null).getSelectedDWM());
		System.out.println("weekmonth".indexOf("day"));
	}
	
}
