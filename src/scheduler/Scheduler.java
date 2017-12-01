package scheduler;

import scheduler.view.LoginView;
/***
 * Created by dongkai3 on 2016/3/7.
 */
public class Scheduler {
	
	public static void main(String[] args) {
		LoginView lv = new LoginView();
		/*OperateView ov = new OperateView();
		lv.setOv(ov);*/
		System.out.println("start...");
		lv.setVisible(true);
	}
	
	
}
