package scheduler.view;

import java.awt.Checkbox;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scheduler.SchedulerContext;
import scheduler.SchedulerUtil;

public class SearchTaskPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	SchedulerContext sm;
	
	JLabel tip ;
	
	JTextField shellName;
	
	JButton search;
	
	TextArea tip_area;
	
	Checkbox  r_influence;

	public TextArea getTip_area() {
		return tip_area;
	}

	public SearchTaskPanel(SchedulerContext sm){
		this.sm = sm;
		this.setSize(300, 500);
		this.setLayout(null);
		this.add(tip = SchedulerUtil.getJLabel("请输入脚本名:xxx.sh",10, 80, 150, 18), null); 
		this.add(shellName = SchedulerUtil.getJTextField("",10, 100, 270, 23), null);
		r_influence = new Checkbox("生成启动调度脚本", false);
		r_influence.setBounds(10, 125, 210, 20);
		this.add(r_influence);
		this.add(search = SchedulerUtil.getButton("查找",95, 150, 70, 27),null);
		addSearchListener();
		this.add(getTipPanel());
	    tip_area.setEditable(false);
		this.sm.setOv(tip_area);
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
				search.setEnabled(false);
				try {
//						search();
						new Thread(new SearchThread(tip_area,shellName.getText(),r_influence.getState())).start();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.err.println(e2.getMessage());
				}
				search.setEnabled(true);
			}
		});
	}
	
	private void search(){
		String sh = shellName.getText();
		if(SchedulerUtil.isStrNull(sh))
			tip_area.setText("脚本名不能为空！");
		if(!sh.endsWith(".sh"))
			sh += ".sh";
		if(r_influence.getState()){
			String ss = sm.createStartTaskStr(sh);
			tip_area.setText(ss);
		}else{
			String sn = sm.getProjectNameByContent(sh);
			if(!SchedulerUtil.isStrNull(sn))
				tip_area.setText("卧槽！找到了：\n"+sn.replace("|", "\n"));
			else
				tip_area.setText("看看是不是脚本名写错了??");
		}
	}

	class SearchThread implements Runnable{
		boolean iscreate = false;
		String sh = "";
		TextArea tip_area;

		public SearchThread(TextArea tip_area,String sh,boolean iscreate){
			this.iscreate = iscreate;
			this.sh = sh;
			this.tip_area = tip_area;
		}

		@Override
		public void run() {
			sm.setOv(tip_area);
			if(SchedulerUtil.isStrNull(sh))
				tip_area.setText("脚本名不能为空！");
			if(!sh.endsWith(".sh"))
				sh += ".sh";
			if(iscreate){
				String ss = sm.createStartTaskStr(sh);
				tip_area.setText(ss);
			}else{
				String sn = sm.getProjectNameByContent(sh);
				if(!SchedulerUtil.isStrNull(sn))
					tip_area.setText("卧槽！找到了：\n"+sn.replace("|", "\n"));
				else
					tip_area.setText("看看是不是脚本名写错了??");
			}
		}
	}
	
}
