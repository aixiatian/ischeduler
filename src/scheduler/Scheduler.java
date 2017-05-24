package scheduler;

import scheduler.view.LoginView;

public class Scheduler {
	
	public static void main(String[] args) {
		LoginView lv = new LoginView();
		/*OperateView ov = new OperateView();
		lv.setOv(ov);*/
		System.out.println("start...");
		lv.setVisible(true);
	}
	
	
}
