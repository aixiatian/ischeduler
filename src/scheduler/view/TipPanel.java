package scheduler.view;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.SchedulerUtil;

public class TipPanel  extends JPanel{
	private static final long serialVersionUID = -3630264512989390870L;

	JLabel l_tip;
	TextArea tip_area;
	
	public TipPanel(){
		this.setSize(300, 500);
	    this.setLayout(null);
		add(l_tip = SchedulerUtil.getJLabel("使用说明",5, 5, 140, 18), null); 
		tip_area = new TextArea();
		tip_area.setBounds(5, 30, 285, 400);
		tip_area.setVisible(true);
		tip_area.setEditable(false);
		add(tip_area);
		addText();
	}
	
	private void addText(){
		if(tip_area != null && "".equals(tip_area.getText())){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/tips")));
				String str = "";
				while((str = reader.readLine()) != null){
					tip_area.append(str+"\n");
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
	
}
