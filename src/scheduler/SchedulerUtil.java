package scheduler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import scheduler.entity.Node;
/***
 * Created by dongkai3 on 2016/3/7.
 */
public class SchedulerUtil {
	
	final public static String logPath = "D:/iScheduler_log/"; 
	final public static String FILE_PROJECT_SH_0 = "D:/iScheduler_log/project_sh.txt"; 
	final public static String FILE_PROJECT_SH_1 = "D:/iScheduler_log/project_sh_formal.txt"; 
	final public static String FILE_PROJECT_SH_2 = "D:/iScheduler_log/project_sh_newhpm.txt";
	final public static String PROJECT_SH_FILE_NAME = "project_sh";
	final public static String PROJECT_SH_FILE_NAME_FORMAL = "project_sh_formal";
	final public static String PROJECT_SH_FILE_NAME_NEWHPM = "project_sh_newhpm";
	//部门id
	final public static String loggroup = "17";//17:日志分析组，6:仓库小组，14:suda小组，3:线上小组
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdfsm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public static JButton getButton(String text,int x,int y,int width,int height){
		JButton jb = new JButton(text);
		jb.setBounds(x, y, width, height); 
		return jb;
	}
	
	
	public static JLabel getJLabel(String text,int x,int y,int width,int height){
		JLabel l = new JLabel(text);
		l.setBounds(x, y, width, height);
		return l;
	}
	
	public static JTextField getJTextField(String name,int x,int y,int width,int height){
		JTextField jt = new JTextField(name);
		jt.setBounds(x, y, width, height);
		return jt;
	}
	
	public static JTextField getPwdTextField(String name,int x,int y,int width,int height){
		JPasswordField jt = new JPasswordField(name);
		jt.setBounds(x, y, width, height);
		return jt;
	}
	public static JTextArea getJTextArea(String name,int x,int y,int width,int height){
		JTextArea jt = new JTextArea(name);
		jt.setBounds(x, y, width, height);
		return jt;
	}
	
	public static boolean isStrIn(String src,String str){
		if(src == null || str == null) return false;
		String cpsrc = "," + src + ",";
		String cpstr = "," + str + ",";
		return cpsrc.contains(cpstr);
	}

	public static String format2ymd(String datestr){
		if(isStrNull(datestr)){
			return datestr;
		}
		try{
			return sdf.format(new Date(Long.parseLong(datestr)));
		}catch (Exception e){
			return datestr;
		}
	}

	public static String format2ymdhms(String datestr){
		if(isStrNull(datestr)){
			return datestr;
		}
		try{
			return sdfsm.format(new Date(Long.parseLong(datestr)));
		}catch (Exception e){
			return datestr;
		}
	}


	
	public static boolean isStrNull(String str){
		return str == null || "".equals(str);
	}
		
	public static void writeLog(String str,String fileName){
		String path = logPath+fileName+".txt";
		File f = new File("d:/iScheduler_log");
		if(!f.exists())
			f.mkdir();
		FileWriter writer = null;
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(path, true);
			writer.write(str+"\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			if(writer != null)
				try {
					writer.write(e.getMessage());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	}
	public static void writeFile(String path,String str){
		

			File file = new File(path);
			OutputStreamWriter out = null;
			try {

				out = new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8");
				out.write(str.toCharArray());
				out.flush();
				System.out.println("success!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (Exception e2) {
				}
			}

	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 15; i++) {
			writeLog(i+"\n", "aabbcc");
		}
	}
	private static final Calendar staticCal = newGregorianCalendar();
	
	static Calendar newGregorianCalendar()
	  {
	    int hOffset = TimeZone.getDefault().getRawOffset() / 3600000;

	    return Calendar.getInstance(TimeZone.getTimeZone("GMT" + (hOffset > 0 ? "+" + hOffset : new StringBuilder().append("").append(hOffset).toString()) + ":00"));
	  }
	
	public static Calendar getStaticCalendars(java.util.Date date)
	  {
	    if (date != null)
	      staticCal.setTime(date);
	    else
	      staticCal.setTimeInMillis(System.currentTimeMillis());
	    return staticCal;
	  }
	
	public static int getDateWeekDay(Date date) {
		
		if (date == null)
			return 0;
		synchronized (staticCal) {
			int res = getStaticCalendars(date).get(7) - 1;
			return res == 0 ? 7 : res;
		}
	}
	
	public static int getDateDay(java.util.Date date){
		if (date == null)
			return 0;
		synchronized (staticCal) {
			return getStaticCalendars(date).get(5);
		}
	}
	
	public static int getDateHour(Date date) {
		synchronized (staticCal) {
			return getStaticCalendars(date).get(Calendar.HOUR_OF_DAY);
		}
	}
	
	public static int str2int(String str,int defaultval){
		if(isStrNull(str)){
			return defaultval;
		}
		return Integer.parseInt(str);
	}
	
	public static String getDateStr(Date date) {
		synchronized (staticCal) {
			return sdf.format(getStaticCalendars(date).getTime());
		}
		
	}
	
	public static void sendMail(String to,String subject,String content){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://api.pso.data.sina.com.cn/monitor/index.php/interface/sendMail");
		try {
			post.addHeader("Expect","");
			Map<String,String> parameterMap = new HashMap<String,String>();
		    parameterMap.put("subject", subject);
		    if(!to.contains("@"))
		    	to = to + "@staff.sina.com.cn";
		    parameterMap.put("receivers", to);
		    parameterMap.put("content", content);
		    
		    UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
		    post.setEntity(postEntity);
			client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static List<BasicNameValuePair> getParam(Map<String,String> parameterMap) {
	    List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
	    Iterator<Map.Entry<String,String>> it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String,String> parmEntry = it.next();
	      param.add(new BasicNameValuePair((String) parmEntry.getKey(),
	          (String) parmEntry.getValue()));
	    }
	    return param;
	}
	
	public static JSONArray getJSONArray(String str,String arrname){
		return getJSONArray("status", str, arrname);
	}
	public static JSONArray getJSONArray(String statusnm,String str,String arrname){
		if(isStrNull(str))
			return null;
		try {
			JSONObject jo = new JSONObject(str);
			if(!isJsonReturnSuccess(jo))
				return null;
			System.out.println(jo.getString(arrname));
			return jo.getJSONArray(arrname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isJsonReturnSuccess(JSONObject jo){
		return "success".equals(getJsonObjectString(jo,"status"))
				|| "000".equals(getJsonObjectString(jo,"code"))
				|| "success".equals(getJsonObjectString(jo,"desc"));
	}

	public static String getJsonObjectString(JSONObject o ,String name){
		String res = "";
		try {
			res = o.getString(name);
		} catch (Exception e) {
			res = "";
		}
		return res;
	}
	
	public static boolean deleteLogFile(String filename){
		boolean r = true;
		try {
			File f = new File(filename);
			if(!f.exists())
				return true;
			return f.delete();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			r = false;
		}
		return r;
	}
	
	public static boolean isLoggroup(String name) {
		return loggroup.equals(name);
	}

	public static boolean isDayWeekMonthExe(String src,Date date){
		if(src == null || "".equals(src) || src.indexOf("day") > -1){
			return true;
		}else if (src.indexOf("week") > -1 && SchedulerUtil.getDateWeekDay(date) == 1){
			return true;
		}else if (src.indexOf("month") > -1 && SchedulerUtil.getDateDay(date) == 1){
			return true;
		}
		return false;
	}

	public static boolean isTest(Node node){
		if(node == null){
			return false;
		}
		return !node.getOnline();
	}

	public static String getCurrentScheduler(int idx){
		if(idx == 0){
			return "测试";
		}else if (idx == 1){
			return "正式";
		}else if (idx == 2){
			return "newhpm";
		}else{
			return "unknown";
		}
	}

	public static String getLogPath(int idx){
		if(idx == 0){
			return FILE_PROJECT_SH_0;
		}else if (idx == 1){
			return FILE_PROJECT_SH_1;
		}else if (idx == 2){
			return FILE_PROJECT_SH_2;
		}else{
			return "unknown";
		}
	}

	public static String getFileName(int idx){
		if(idx == 0){
			return PROJECT_SH_FILE_NAME;
		}else if (idx == 1){
			return PROJECT_SH_FILE_NAME_FORMAL;
		}else if (idx == 2){
			return PROJECT_SH_FILE_NAME_NEWHPM;
		}else{
			return "unknown";
		}
	}
}
