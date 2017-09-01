package scheduler;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scheduler.entity.Node;
import scheduler.entity.Project;

public class SchedulerContext {
	
	public SchedulerContext(int hostIdx){
		this.hostIdx = hostIdx;
	}

	public SchedulerContext(){}
	
	String token ;
	public String username;
	String pwd;
	String ssn;
	CloseableHttpClient client = null;
	//0 :测试
	//1 :正式
	static String[] hosts = new String[]{
		"yz2154.hadoop.data.sina.com.cn:80"
		,"scheduler.data.sina.com.cn:8080"};
	
	int hostIdx = 0;

	boolean isStartTest = false;

	public void setStartTest(boolean startTest) {
		isStartTest = startTest;
	}
	TextArea ov;
	
	public TextArea getOv() {
		return ov;
	}

	public void setOv(TextArea ov) {
		this.ov = ov;
	}
	
	public int getHostIdx() {
		return hostIdx;
	}

	public void setHostIdx(int hostIdx) {
		this.hostIdx = hostIdx;
	}

	/*private boolean isInMaxTaskNum(int num_per_time){
		boolean res = true;
		String projs = getProjectsForStatus("RUNNING");
		if(SchedulerUtil.isStrNull(projs)){
			res = false;
		}else{
			try {
				JSONObject jo = new JSONObject(projs);
				String success = jo.getString("status");
				if(!"success".equals(success))
					res = false;
				JSONArray jarr = jo.getJSONArray("data");
				if(jarr.length()<=0){
					res = false;
				}else{
					
					int nodeCount = 0;
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject o = (JSONObject)jarr.get(i);
						nodeCount += o.getInt("nodeCount");
					}
					if(nodeCount >= num_per_time){
						ov.append("当前运行任务数"+nodeCount+"大于等于最大每批任务数：\n"+num_per_time+"\n");
						return true;
					}else{
						double rate = 1 - (nodeCount*1.00/num_per_time);
						if(rate <= 0.1){
							return false;
						}else{
							return true;
						}
						
					}
				}
			} catch (Exception e) {
				res = true;
				ov.append("查询当前执行节点个数出错：\n"+e.getMessage()+"\n");
			}
		}
		return res;
	}*/
	
	public int getRunningTaskNum(){
		String projs = getProjectsForStatus("RUNNING");
		if(SchedulerUtil.isStrNull(projs)){
			return -1;
		}else{
			try {
				JSONObject jo = new JSONObject(projs);
				String success = jo.getString("status");
				if (!"success".equals(success))
					return -1;
				JSONArray jarr = jo.getJSONArray("data");

				int nodeCount = 0;
				/*for (int i = 0; i < jarr.length(); i++) {
					JSONObject o = (JSONObject) jarr.get(i);
					nodeCount += o.getInt("nodeCount");
				}*/
				if(jarr == null)
					nodeCount = 0;
				else
					nodeCount = jarr.length();
				return nodeCount;
			} catch (Exception e) {
				if(ov != null)
				ov.append("查询当前执行节点个数出错：\n"+e.getMessage()+"\n");
			}
		}
		return -1;
	}
	public int getRunningShellNum(){
		String projs = getProjectsForStatus("RUNNING");
		if(SchedulerUtil.isStrNull(projs)){
			return -1;
		}else{
			try {
				JSONObject jo = new JSONObject(projs);
				if (!SchedulerUtil.isJsonReturnSuccess(jo))
					return -1;
				JSONArray jarr = jo.getJSONArray("data");
				int nodeCount = 0;
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject item = jarr.getJSONObject(i);
					String projid = item.getString("projectId");
					String insgraid = item.getString("graphInsId");
					String nodesStr = getNodesForProject(projid, insgraid);
					JSONObject nodesJs = new JSONObject(nodesStr);
					JSONObject data = nodesJs.getJSONObject("data");
					JSONArray nodes = data.getJSONArray("nodes");
					for (int j = 0; j < nodes.length(); j++) {
						
						JSONObject node = nodes.getJSONObject(j);
						String status = node.getString("status");
						if (!"RUNNING".equals(status))
							continue;
						nodeCount++;
					}
					Thread.sleep(1000);
				}
				return nodeCount;
			} catch (Exception e) {
				if(ov != null)
				ov.append("查询当前执行节点个数出错：\n"+e.getMessage()+"\n");
				e.printStackTrace();
			}
		}
		return -1;
	}
	public List<JSONObject> getResponseMap(String json){
		try {
			JSONObject jo = new JSONObject(json);
			if(!SchedulerUtil.isJsonReturnSuccess(jo))
				throw new RuntimeException("返回值状态有误！");
			JSONArray jarr = jo.getJSONArray("data");
			if(jarr.length()>0){
				List<JSONObject> items = new ArrayList<JSONObject>();
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject o = (JSONObject)jarr.get(i);
					items.add(o);
				}
				return items;
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,Project> getAllProjects(){
		String url = hosts[hostIdx]+Constants.getAllProjects;
		url += "ssn="+ssn+"&";
		url += "userName="+username+"&";
		url += "token="+token;
		HttpGet get = new HttpGet("http://"+url);
		get.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		get.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		get.addHeader("Connection", "keep-alive");
		get.addHeader("Host", hosts[hostIdx]);
		get.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		get.addHeader("X-Requested-With", "XMLHttpRequest");
		
		String body = "";
		Map<String,Project> proj = null;
		try {
			CloseableHttpResponse resp = client.execute(get);
			body = EntityUtils.toString(resp.getEntity(),"utf-8");
			if(resp.getStatusLine().getStatusCode() == 200){
//				String dataStr = new JSONObject(body).getString("data");
				proj = Project.getProjectsFromJsonStr(body);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return proj;
	}
	
	public Map<String,Node> searchNodesForProjectIDs(Project p){
		String interface_url = "http://"+hosts[hostIdx]+Constants.REST_URL;
		String res = "";
		HttpPost post = new HttpPost(interface_url);

		post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Origin", "http://"+hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		String projid = p.getId().replace(",", "\",\"");
		System.out.println("项目id："+projid);
		String data = "{";
		data +="\"projectIDs\":[\""+projid+"\"],";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\",";
		data += getNodeExecample();
		data += "}";
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "POST");
	    parameterMap.put("rdurl", Constants.searchNodesForProjectIDs);
	    parameterMap.put("rdm", Math.random()+"");
	    
	    Map<String,Node> nodeMap = null;
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			CloseableHttpResponse resp = client.execute(post);
			if(resp.getStatusLine().getStatusCode() == 200){
				res = EntityUtils.toString(resp.getEntity(),"utf-8");
				JSONObject dataJson = null;
				try {
					dataJson = new JSONObject(res).getJSONObject("data");
					
				} catch (Exception e) {
					System.err.println("getData失败!");
					return new HashMap<String,Node>();
				}
//				System.out.println(dataJson.getString("nodes"));
				nodeMap = Node.getNodeMapFromJson(dataJson.getString("nodes"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeMap;
	}
	
	public boolean login(String username,String pwd) {
		client = HttpClients.createDefault();
		if(SchedulerUtil.isStrNull(this.username))
			this.username = username;
		if(SchedulerUtil.isStrNull(this.pwd))
			this.pwd = pwd;
		boolean success = false;
		HttpGet get = new HttpGet("http://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
		get.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Host", "cas.erp.sina.com.cn");
		get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
		
		try {
			CloseableHttpResponse resp = client.execute(get);
			String body = EntityUtils.toString(resp.getEntity(), "utf-8");
			String lt = body.split("name=\"lt\" value=\"")[1];
			lt = lt.substring(0,lt.indexOf("\""));
			HttpPost post = new HttpPost("https://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
			post.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			post.addHeader("Accept-Encoding", "gzip, deflate");
			post.addHeader("Accept-Language", "zh-CN");
			post.addHeader("Cache-Control", "no-cache");
			post.addHeader("Connection", "Keep-Alive");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Host", "cas.erp.sina.com.cn");
			post.addHeader("Referer", "https://cas.erp.sina.com.cn/cas/login?ext=http://"+hosts[hostIdx]+"/schedulerManager/&service=http://"+hosts[hostIdx]+"/schedulerManager/index.php/login");
			post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
			
			Map<String,String> parameterMap = new HashMap<String,String>();
		    parameterMap.put("auth_type", "ldap");
		    parameterMap.put("ext", "http://"+hosts[hostIdx]+"/schedulerManager/");
		    parameterMap.put("lt", lt);
		    parameterMap.put("password", this.pwd);
		    parameterMap.put("qrc_email", "");
		    parameterMap.put("qrc_status", "");
		    parameterMap.put("username", this.username);
		    
		    UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
		    post.setEntity(postEntity);
		    
		    CloseableHttpResponse presp = client.execute(post);
		    int code = presp.getStatusLine().getStatusCode();
		    body = EntityUtils.toString(presp.getEntity(), "utf-8");
		    if(code == 200){
		    	String loginUrl = body.substring(body.indexOf("\"")+1, body.lastIndexOf("\""));
		    	presp.close();
		    	HttpGet login = new HttpGet(loginUrl);
		    	login.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		    	login.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		    	login.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		    	login.addHeader("Connection", "keep-alive");
		    	login.addHeader("Host", hosts[hostIdx]);
		    	login.addHeader("User-Agent", "Mozilla/5.0(Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/31.0.1650.63 Safari/537.36");

		    	HttpResponse presp1 = client.execute(login);
		    	
		    	body = EntityUtils.toString(presp1.getEntity(), "utf-8");
		    	getSsn(body);
		    	getTokenStr(loginUrl);
		    	success = true;
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	
	String getSsn(String loginBody){
		
		if("".equals(ssn) || loginBody != null){
			String ssnStr = loginBody.substring(loginBody.indexOf("\"sinaSchedulerSsn\",\"")+20);
			ssn = ssnStr.substring(0,ssnStr.indexOf("\""));
		}
		
		return ssn;
	}
	
	public List<BasicNameValuePair> getParam(Map<String,String> parameterMap) {
	    List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
	    Iterator<Map.Entry<String,String>> it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String,String> parmEntry = it.next();
	      param.add(new BasicNameValuePair((String) parmEntry.getKey(),
	          (String) parmEntry.getValue()));
	    }
	    return param;
	}
	
	String getTokenStr(String loginurl){
		if(token == null || "".equals(token)){
			token = loginurl.substring(loginurl.indexOf("ticket=")+7,loginurl.indexOf("&"));
		}
		return token;
	}
	
	//RUNNING ：运行中 INITED ：等待中 FAILED ：错误 COMPELETED ：已完成
	String getProjectsForStatus(String status){
		String url = "http://"+hosts[hostIdx]+Constants.getProjectsForStatus;
		url += "ssn="+ssn;
		url += "&token="+token;
		url += "&userName="+username;
		url += "&nodestatus="+status;
		
		HttpGet get = new HttpGet(url);
		String body = "";
		try {
			CloseableHttpResponse resp = client.execute(get);
			body = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	String getNodesForProject(String projid,String insgraid){
		String interface_url = "http://"+hosts[hostIdx]+Constants.REST_URL;
		String res = "";
		HttpPost post = new HttpPost(interface_url);
		
		post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Origin", "http://"+hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		String data = "{";
		data +="\"projIDs\":[\""+projid+"\"],";
		data +="\"insGraphID\":\""+insgraid+"\",";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\",";
		data += getNodeExecample();
		data += "}";
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "POST");
	    parameterMap.put("rdurl", Constants.getNodesForProject);
	    parameterMap.put("rdm", Math.random()+"");
	    
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			CloseableHttpResponse resp = client.execute(post);
			res = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	String getLogUrlFromMsg(String outPath,String outMsg){
		String errUrl = "";
		if(SchedulerUtil.isStrNull(outMsg))
			return "";
		errUrl += outMsg.substring(outMsg.indexOf("[INFO] 日志文件:")+12);
		errUrl += errUrl.substring(0,errUrl.indexOf(".log"));
		return errUrl;
	}
	
	String getNodeExecample(){
		String res = "\"nodeExample\":{\"id\":\"p_001\",\"name\":\"test1\",\"nameCN\":\"测试1\",\"desc\":\"111\",\"runUser\":\"xiaofei5\",\"svnUrl\":\"t\",\"content\":\"123\",\"params\":\"\",\"runIP\":\"10.39.0.100\",\"priority\":3,\"creator\":\"xiaofei5\",\"createTime\":\"20130128\",\"updateTime\":\"20130128\",\"owner\":\"xiaofei5\",\"group\":\"\",\"deptID\":\"3\",\"projectID\":\"1\",\"maxRetry\":2,\"startTime\":\"03  00\",\"warningCond\":\"test\",\"inputPath\":[\"/file/tblog/behavior/scribe\"],\"outputPath\":[\"/file/tblog/behavior/14000003\",\"/file/tblog/behavior/14000004\"],\"hadoopVersion\":\"v1\",\"paramMap\":{\"key1\":\"val1\",\"key2\":\"val2\"},\"defID\":\"n_002\",\"insGraphID\":\"ins001\",\"actualStartTime\":330088888,\"actualEndTime\":330088889,\"progress\":50,\"status\":\"FAILED\",\"exitCode\":3,\"errMsg\":\"some error\",\"outMsg\":\"\",\"errPath\":\":8080/logView?logpath=/temp/test2.err\",\"outPath\":\":8080/logView?logpath=/temp/test1.out\",\"depth\":3}";
		return res;
	}
	/***
	 * 重试节点
	 * @param nodeInsID
	 * @return
	 */
	String retryNode(String insGraphID,String nodeInsID){
		return setStatus(insGraphID, nodeInsID, "READY");
	}
	/***
	 * 跳过节点
	 * @param insGraphID
	 * @param nodeInsID
	 * @return
	 */
	String passNode(String insGraphID,String nodeInsID){
		return setStatus(insGraphID, nodeInsID, "COMPELETED");
	}
	/***
	 * 终止节点
	 * @param insGraphID
	 * @param nodeInsID
	 * @return
	 */
	String interruptNode(String insGraphID,String nodeInsID){
		return setStatus(insGraphID, nodeInsID, "FAILED");
	}
	
	/***
	 * COMPELETED 跳过
	 * READY	      重试
	 * FAILED	      终止
	 * @return
	 */
	String setStatus(String insGraphID,String nodeInsID,String status){

		String interface_url = "http://"+hosts[hostIdx]+Constants.REST_URL;
		String res = "";
		HttpPost post = new HttpPost(interface_url);
		post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		post.addHeader("Host", hosts[hostIdx]);
		post.addHeader("Origin", "http://"+hosts[hostIdx]);
		post.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		
		String data = "{";
		data +="\"insGraphID\":\""+insGraphID+"\",";
		data +="\"nodeInsID\":\""+nodeInsID+"\",";
		data +="\"status\":\""+status+"\",";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\"";
		data += "}";
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "PUT");
	    parameterMap.put("rdurl", Constants.setStatus);
	    parameterMap.put("rdm", Math.random()+"");
	    
	    UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);
			CloseableHttpResponse resp = client.execute(post);
			res = EntityUtils.toString(resp.getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	void startByDefnodeFromParamMap(Map<String,Object> param,String defids){
		
	}
	
	/**
	 * 根据节点id，按照指定日期启动节点
	 * @param dateStr 日期字符串
	 * @param defids 节点ID
	 */
	public String startByDefnode(String dateStr,String defids){	
		
		HttpPost method = new HttpPost("http://"+hosts[hostIdx]+Constants.REST_URL);
		method.addHeader("Accept", "gzip,deflate,sdch");
		method.addHeader("Accept-Encoding", "application/json, text/javascript, */*; q=0.01");
		method.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		method.addHeader("Connection", "keep-alive");
		method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		method.addHeader("Host", hosts[hostIdx]);
		method.addHeader("Origin", "http://"+hosts[hostIdx]+"");
		method.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/");
		method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		method.addHeader("X-Requested-With", "XMLHttpRequest");
		if(defids.contains(","))
			defids = defids.replaceAll(",", "\",\"");
		String data = "{";
		data +="\"defIDs\":[\""+defids+"\"],";
		String hour = "";
		if(dateStr.contains("@"))
		{
			String[] dtele = dateStr.split("@");
			dateStr = dtele[0].trim();
			hour = dtele[1].trim();
		}	
		data +="\"paramMap\":{\"YYYYMMDD\":\""+dateStr+"\",\"HH\":\""+hour+"\"},";
		data +="\"ssn\":\""+ssn+"\",";
		data +="\"userName\":\""+username+"\",";
		data +="\"token\":\""+token+"\"";
		data += "}";
		
		Map<String,String> parameterMap = new HashMap<String,String>();
	    parameterMap.put("data", data);
	    parameterMap.put("sendtype", "PUT");
	    parameterMap.put("rdurl", Constants.startByDefnode);
	    parameterMap.put("rdm", Math.random()+"");
	    
	    UrlEncodedFormEntity postEntity;
	    String err = "";
	    try {
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			method.setEntity(postEntity);
			CloseableHttpResponse resp = client.execute(method);
			String body = EntityUtils.toString(resp.getEntity());
			body = body.replace("\"", "");
			body = body.replace("{", "");
			body = body.replace("}", "");
			body = body.replace(",", "\n");
			return body;
		} catch (Exception e) {
			err += e.getMessage();
			e.printStackTrace();
		}
		return dateStr+"->任务启动失败!"+err;
	}
	/***
	 * loganalysis_mbportal_client_sports:hive_mobileportal_722078:rpt_kpi_mobileportal_722078.sh:20150101:20151231;
	 */
	public void startMutiTaskBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String infos,String dwm){
		if(SchedulerUtil.isStrNull(infos))
			return;
		String[] projs = infos.split(";");
		if(projs.length < 1)
			return;
		for (int i = 0; i < projs.length; i++) {
			String proj = projs[i].trim();
			if(SchedulerUtil.isStrNull(proj))
				continue;
			String[] info = proj.split(":");
			if(info.length==5 || info.length==3){
				String[] tasknames = info[1].trim().split(",");
				String[] shells = info[2].trim().split(",");
				if(tasknames.length != shells.length){
					ov.append("配置信息有误：任务名和脚本数量不匹配！\n任务名："+info[1]+"\n脚本："+info[2]+"\n");
					continue;
				}
				
				String datefromStr = datefrom;
				String datetoStr = dateto;
				if(info.length==5){
					datefromStr = info[3].trim();
					datetoStr = info[4].trim();
				}
				
				String defidsStr = "";
				String hour = "";
				boolean findAllDefids = true;
				for (int j = 0; j < tasknames.length; j++) {
					String tkname = tasknames[j].trim();
					if(tkname.contains("@")){
						String[] tkeles = tkname.split("@");
						tkname = tkeles[0];
						if(tkeles.length==2)
							hour = tkeles[1];
						else
							hour = "00";
					}
						
					Node node = getDefids(info[0].trim(), tkname);
					if(node == null){
						ov.append("未找到节点信息：\n项目名："+info[0]+"，任务名："+tkname+"\n");
						findAllDefids = false;
						break;
					}
					String content = node.getContent();
					content = content.substring(content.lastIndexOf("/")+1);
					if(content.trim().equals(shells[j].trim())){
						ov.append("节点信息匹配成功：\n项目名："+info[0]+"\n任务名："+tkname+"\n脚本名："+shells[j]+"\n");
						String defids = node.getId();
						if(!"".equals(defidsStr)){
							defidsStr += ","+defids;
						}
						else{
							defidsStr += defids;
						}
							
					}
				}
				if(!findAllDefids){
					ov.append("信息未完全匹配："+proj+"执行下一个了 -_-!!!\n");
					continue;
				}
				startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefromStr, datetoStr, defidsStr,dwm,hour);
			}
		}
	}
	
	/***
	 * 
	 * @param num_per_time 每批任务个数
	 * @param task_timespan_sec 两个任务之间间隔
	 * @param sleep_for_next 两批任务间隔
	 * @param datefrom
	 * @param dateto
	 * @param defids 任务ID
	 */
	public void startNodeBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String defids,String dwm,String hour){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			long timestart = sdf.parse(datefrom).getTime();
			long timeend = sdf.parse(dateto).getTime();
			int count = 0;
			int canrunNum = num_per_time;
			for (long i = timestart; i <= timeend; i=i+86400000) {
				if(!SchedulerUtil.isDayWeekMonthExe(dwm, new Date(i)))
					continue;
				if(count == 0){
					
					int nodecount = getRunningTaskNum();
					if(nodecount >= num_per_time){
						i = i - 86400000;
						Thread.sleep(sleep_for_next*60*1000);
						continue;
					}else{
						canrunNum = num_per_time - nodecount;
					}
				}
				if(canrunNum <= 0 || canrunNum > num_per_time){
					i = i - 86400000;
					Thread.sleep(sleep_for_next*60*1000);
					int nodecount = getRunningTaskNum();
					canrunNum  = num_per_time - nodecount;
					continue;
				}
				String dateStr = sdf.format(i);
				if(!SchedulerUtil.isStrNull(hour))
					dateStr += "@"+hour;
				String res = startByDefnode(dateStr, defids);
				ov.append(dateStr+"成功：\n"+res+"\n");
				Thread.sleep(task_timespan_sec*1000);
				count ++;
				canrunNum --;
				/*if(canrunNum == 0 && count % num_per_time == 0){
					Thread.sleep(sleep_for_next*60*1000);
					int nodecount = getRunningTaskNum();
					canrunNum  = num_per_time - nodecount;
				}*/
					
			}
			ov.append("----------------任务完成-------------------"+count);
			String tt = ov.getText();
			SchedulerUtil.writeLog(tt, defids);
			
//			ov.setText("");
		} catch (Exception e) {
			ov.append(e.getMessage());
		}
	}
	
	
	
	public void startNodeBetweenDates(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String defids,String dwm){
		startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, defids, dwm, null);
	}
	
	/***
	 * 根据项目名和任务名获取任务ID
	 * @param projName
	 * @param nodeName
	 * @return 节点ID
	 */
	public Node getDefids(String projName,String nodeName){
		Map<String,Project> projMap = getAllProjects();
		Node id = null;
		projName = projName + "`";
		if(projMap != null && projMap.containsKey(projName)){
			Project p = projMap.get(projName);
			Map<String,Node> nodeMap = searchNodesForProjectIDs(p);
			if(nodeName.contains("@")){
				nodeName = nodeName.substring(0,nodeName.indexOf("@"));
			}
			if(nodeMap != null && nodeMap.containsKey(nodeName)){
				id = nodeMap.get(nodeName);
			}
		}
		return id;
	}
	
	public Map<String,Integer> getNodeCount4ProjectByStatus(Map<String,Integer> data,Project proj){
		if(data == null)
			data = new HashMap<String,Integer>();
		String str = getNodesForProject(proj.getId(), proj.getGraphInsId());
		try {
			JSONObject jo = new JSONObject(str);
			if(!"success".equals(jo.getString("status"))){
				return data;
			}
			Map<String, Node> nodes = Node.getNodeMapFromJson(jo.getJSONObject("data").getString("nodes"));
			for (String key : nodes.keySet()) {
				Node node = nodes.get(key);
				String status = node.getStatus();
				if(data.containsKey(status)){
					data.put(status,data.get(status).intValue() + 1);
				}else{
					data.put(status, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	public static void main(String[] args){
		int hostIdx = 0;
		SchedulerContext sc = new SchedulerContext(hostIdx);
//		sc.login("dongkai3", "0905dk@#");
//		String str = sc.createStartTaskStr("rpt_kpi_videoportal_601030.sh,rpt_kpi_videoportal_601031.sh");
//		System.out.println(str);
		//重试、跳过
		/*String resStr = sc.getProjectsForStatus("INITED");
		System.out.println(resStr);
		String projname = "loganalysis_video_play_basic_day";
		Map<String,Project> projs = Project.getProjectsFromJsonStr(resStr);
		System.out.println(projs);
		List<JSONObject> items = sc.getResponseMap(resStr);
		if(items != null && items.size()>0){
			for (JSONObject item : items) {
				try {
					System.out.println(item.get("id")+"-->"+item.get("name"));
					String name = item.getString("name");
					if(!SchedulerUtil.isStrIn(projname, name))
						continue;
					
					String projid = item.getString("projectId");
					String insgraid = item.getString("graphInsId");
					
					String nodesStr = sc.getNodesForProject(projid, insgraid);
					JSONObject nodesJs = new JSONObject(nodesStr);
					JSONObject data = nodesJs.getJSONObject("data");
					JSONArray nodes = data.getJSONArray("nodes");
					for (int i = 0; i < nodes.length(); i++) {
						
						JSONObject node = nodes.getJSONObject(i);
						
//						String viewlog = sc.getLogUrlFromMsg(node.getString("outPath"), node.getString("outMsg"));
//						System.out.println("日志信息："+viewlog);
						
						String nodeinsid = node.getString("id");
						String status = node.getString("status");
						if(!"READY".equals(status))
							continue;
						//重试节点
//						String str = sc.retryNode(insgraid, nodeinsid);
						//跳过节点
						String str = sc.passNode(insgraid, nodeinsid);
						System.out.println(node.getString("nameCN")+"-->重试节点返回："+str);
//						System.out.println(node.getString("nameCN"));
					}
					System.out.println("--------------------------");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}*/
//		String ss = sc.getProjectNameByContent("mr_mds_media_cp_cost.sh");
//		sc.getInfluncedNodes("loganalysis_ods_mbportal_apache_data", "ods_mbportal_apache_data");
//		System.out.println(ss);
//		int num = sc.getRunningShellNum();
//		System.out.println(num);
	}
	
	public String getProjectNameByContent(String sh){
		String res = "";
		String pfname = SchedulerUtil.getLogPath(hostIdx);
		res = getProjectNameFromFile(sh);
		if(!SchedulerUtil.isStrNull(res)){
			return res;
		}
		SchedulerUtil.deleteLogFile(pfname);
		Map<String,Project> ps = getAllProjects();
//		ov.append("正在查找\n");
		int per = 1;
		int size = ps.size();
		for (String pn : ps.keySet()) {
			Project p = ps.get(pn);
			Map<String,Node> nods = searchNodesForProjectIDs(p);
			for (String nn : nods.keySet()) {
				Node n = nods.get(nn);
				String content = n.getContent();
				String record = "项目名："+p.getName()+"|任务名："+n.getName();
				if(hostIdx == 2){
					record = record + "|状态：" + (n.getOnline()?"上线":"下线");
				}
				if(content.contains(sh)){
					res += record + "\n";
				}
				String shn = content.substring(content.lastIndexOf("/")+1);
				SchedulerUtil.writeLog(shn+";"+record, SchedulerUtil.getFileName(hostIdx));
			}
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			per ++;
			ov.setText(Math.round((per*1.00/size)*100)+"%");
		}
		ov.setText("100%");
		return res;
	}
	
	public String getProjectNameFromFile(String sh){
		String path = SchedulerUtil.getLogPath(hostIdx);
//		String path = hostIdx == 0 ? SchedulerUtil.FILE_PROJECT_SH_0 : SchedulerUtil.FILE_PROJECT_SH_1;
		BufferedReader br = null;
		InputStreamReader isr = null;
		String res = "";
		try {
			String charset = System.getProperty("sun.jnu.encoding");
			isr = new InputStreamReader(new FileInputStream(path), charset);
			br = new BufferedReader(isr);
			String line = null;
			while((line = br.readLine()) != null){
				if(line.startsWith(sh+";"))
				res += line.substring(line.indexOf(";")+1)+"\n";
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
				try {
					if(isr != null)
						isr.close();
					if(br != null)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		return res;
	}
	
	public Map<String,Node> getInfluncedNodes(String projName,String nodeName){
		try {
			Node node = getDefids(projName, nodeName);
			String defid = node.getId();
			
			HttpPost method = new HttpPost("http://"+hosts[hostIdx]+Constants.REST_URL);
			method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			method.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			method.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			method.addHeader("Connection", "keep-alive");
			method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			method.addHeader("Host", hosts[hostIdx]);
			method.addHeader("Origin", "http://"+hosts[hostIdx]+"");
			method.addHeader("Referer", "http://"+hosts[hostIdx]+"/schedulerManager/index.php");
			method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
			method.addHeader("X-Requested-With", "XMLHttpRequest");
			
			String data = "{";
			data +="\"nodeDefIDs\":[\""+defid+"\"],";
			data +="\"maxDepth\":\""+65535+"\",";
			data +=getNodeExecample()+",";
			data +="\"ssn\":\""+ssn+"\",";
			data +="\"userName\":\""+username+"\",";
			data +="\"token\":\""+token+"\"";
			data += "}";
			
			Map<String,String> parameterMap = new HashMap<String,String>();
		    parameterMap.put("data", data);
		    parameterMap.put("sendtype", "POST");
		    parameterMap.put("rdurl", Constants.getInfluncedNodes);
		    parameterMap.put("rdm", Math.random()+"");
		    
		    UrlEncodedFormEntity postEntity;
		    
			postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			method.setEntity(postEntity);
			CloseableHttpResponse resp = client.execute(method);
			String body = EntityUtils.toString(resp.getEntity());
			JSONObject jo = new JSONObject(body);
			Map<String,Node> nm = Node.getNodeMapFromJson(jo.getJSONObject("data").getString("nodes"));
//				for (String nn : nm.keySet()) {
//					System.out.println(nn + "-->" + nm.get(nn).getName());
//				}
			return nm;
		
		} catch (Exception e) {
			if(ov != null)
			ov.setText(e.getMessage());
		}
		return null;
	}
	
	public void startInfluencedNodes(int num_per_time,int task_timespan_sec,int sleep_for_next,String datefrom,String dateto,String projName,String nodeName,String dwm){
		ov.append("正在查找影响到的节点信息...\n");
		Map<String,Node> nodes = getInfluncedNodes(projName, nodeName);
		ov.append("查找完成，共找到"+nodes.size()+"个节点\n");
		String defids = "";
		for (String nn : nodes.keySet()) {
			Node n = nodes.get(nn);
			String dept = n.getDeptID();
			if(!SchedulerUtil.isLoggroup(dept)) {
				System.out.println("非"+SchedulerUtil.loggroup+"-->"+dept);
				continue;
			}
			if(!defids.equals("")) {
				defids += ",";
			}
			defids += n.getId();
		}
		if(!SchedulerUtil.isStrNull(defids)) {
			ov.append("开始启动节点\n");
			startNodeBetweenDates(num_per_time, task_timespan_sec, sleep_for_next, datefrom, dateto, defids, dwm);
		}
	}

	boolean isStop = false;
	
	
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public void retryFailedNodes(String projname,boolean isRetry) {
		isStop = false;
		ov.append("正在查找错误项目："+projname+"\n");
		String resStr = getProjectsForStatus("FAILED");
		List<JSONObject> items = getResponseMap(resStr);
		
		String op = (isRetry?"重试":"跳过");
		
		if (items != null && items.size() > 0) {
			ov.append("查找完成，开始执行"+op+"操作。\n");
			for (JSONObject item : items) {
				if(isStop) {
					ov.append("正在停止查询错误项目...\n");
					break;
				}
				try {
					String name = item.getString("name");
					if (!SchedulerUtil.isStrIn(projname, name))
						continue;

					String projid = item.getString("projectId");
					String insgraid = item.getString("graphInsId");

					String nodesStr = getNodesForProject(projid, insgraid);
					JSONObject nodesJs = new JSONObject(nodesStr);
					JSONObject data = nodesJs.getJSONObject("data");
					JSONArray nodes = data.getJSONArray("nodes");
					for (int i = 0; i < nodes.length(); i++) {
						if(isStop) {
							ov.append("正在停止重试节点...\n");
							break;
						}
						int run = getRunningShellNum();
						if(run > 27){
							try {
								ov.append("当前运行脚本数"+run+",等待1分钟。");
								Thread.sleep(60000);
							} catch (Exception e) {
								
							}
						}
						JSONObject node = nodes.getJSONObject(i);

						passOrRetry(isRetry, op, insgraid, node);
						Thread.sleep(2000);
						
					}
					System.out.println("--------------------------");
				} catch (Exception e) {
					e.printStackTrace();
					ov.append(e.getMessage());
				}
			}
			ov.append("------------------"+(isStop?"停止":op)+"成功-----------------\n");
		}else{
			ov.append("查找失败！");
		}
	}
	
	public void autoRetry(String projname,String fromhour,String tohour,int retrytimes) {
		isStop = false;
		if(SchedulerUtil.isStrNull(projname))
			return;
		if(SchedulerUtil.isStrNull(fromhour))
			fromhour = "2";
		if(SchedulerUtil.isStrNull(tohour))
			tohour = "10";
//		if(retrytimes < 3)
//			retrytimes = 3;
		boolean istodaystart = false;
		Map<String,Integer> taskmap = new HashMap<String, Integer>();
		Map<String,Boolean> errmap = new HashMap<String,Boolean>();
		//taskname + actualStartTime
		//actualStartTime
		ov.append("开始自动重试\n");
		String datestr = SchedulerUtil.getDateStr(null);
		while (true) {
			if(isStop) {
				ov.append("停止自动重试\n");
				checkAndSendMail("【"+datestr+"自动重试节点停止】", errmap);
				break;
			}
			if(checkBreak(fromhour, tohour) != 0) {
				if(istodaystart) {
					ov.append(datestr+"自动重试结束，等待下一天^_^\n");
					checkAndSendMail("【"+datestr+"自动重试节点结束】", errmap);
					istodaystart = false;
					continue;
				}else {
					try {
						Thread.sleep(60000);
						getRunningTaskNum();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
			istodaystart = true;
			String resStr = getProjectsForStatus("FAILED");
			List<JSONObject> items = getResponseMap(resStr);
			if (items != null && items.size() > 0) {
//				ov.append("查找完成，开始执行重试操作。\n");
				for (JSONObject item : items) {
					if(isStop) {
						ov.append("正在停止查询错误项目...\n");
						break;
					}
					try {
						String name = item.getString("name");
						if (!SchedulerUtil.isStrIn(projname, name))
							continue;

						String projid = item.getString("projectId");
						String insgraid = item.getString("graphInsId");

						String nodesStr = getNodesForProject(projid, insgraid);
						JSONObject nodesJs = new JSONObject(nodesStr);
						JSONObject data = nodesJs.getJSONObject("data");
						JSONArray nodes = data.getJSONArray("nodes");
						for (int i = 0; i < nodes.length(); i++) {
							if(isStop) {
								ov.append("正在停止重试节点...\n");
								break;
							}
							JSONObject node = nodes.getJSONObject(i);
							String nodename = node.getString("name");
							String ymd = getHHMMParamsFromNode(node);
							if(SchedulerUtil.isStrNull(ymd)) {
								ov.append("获取"+nodename+"运行参数失败，重试其他节点。\n");
								continue;
							}
							if(taskmap.containsKey(nodename+"-"+ymd)) {
								int t = taskmap.get(nodename+"-"+ymd);
								if(t == retrytimes && !errmap.containsKey("项目："+name+",节点："+nodename+"，运行参数："+ymd)) {
									errmap.put("项目："+name+",节点："+nodename+"，运行参数："+ymd,false);
								}else if(t < retrytimes){
									passOrRetry(true, "重试", insgraid, node,false);
									taskmap.put(nodename+"-"+ymd, taskmap.get(nodename+"-"+ymd)+1);
									ov.append("第"+(t+1)+"次重试->"+"项目："+name+",节点："+nodename+"，运行参数："+ymd+"\n");
								}
							}else {
								passOrRetry(true, "重试", insgraid, node,false);
								taskmap.put(nodename+"-"+ymd, 1);
								ov.append("第1次重试->"+"项目："+name+",节点："+nodename+"，运行参数："+ymd+"\n");
							}
							Thread.sleep(2000);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ov.append(e.getMessage()+"\n");
					}
				}
				//TODO 重试3次仍然不通过的发邮件通知
				checkAndSendMail("【重试"+retrytimes+"次仍失败节点】",errmap);
			}else{
				ov.append("没有符合条件的错误项目!"+"\n");
			}
		}
	}
	
	public int checkBreak(String starth,String endh) {
		int nowh = SchedulerUtil.getDateHour(null);
		int start = Integer.parseInt(starth);
		int end = Integer.parseInt(endh);
		int ret = -1;
		if(nowh > end) {
			ret = 1;
		}else if(nowh < start) {
			ret = -1;
		}else {
			ret = 0;
		}
		return ret;
	}
	
	public void checkAndSendMail(String sub,Map<String,Boolean> errmap) {
		if(errmap.size()>0) {
			String content = "";
			for (String c : errmap.keySet()) {
				if(!errmap.get(c)) {
					content += c + "<br>";
					errmap.put(c, true);
				}
			}
			if(!SchedulerUtil.isStrNull(content))
			SchedulerUtil.sendMail(username, sub, "请查看：<br>" + content);
		}
	}
	
	public String getHHMMParamsFromNode(JSONObject node) throws JSONException {
		JSONObject paramap = node.getJSONObject("paramMap");
		if(paramap != null) {
			String yyyy = paramap.getString("YYYYMMDD");
			String mm = paramap.getString("HH");
			return yyyy + ":" + mm;
		}
		return "";
	}
	
	public boolean passOrRetry(boolean isRetry,String op,String insgraid,JSONObject node,boolean isprint) throws JSONException {
		String nodeinsid = node.getString("id");
		String status = node.getString("status");
		if (!"FAILED".equals(status))
			return false;
		// 重试节点
		String str = "";
		if(isRetry)
			str = retryNode(insgraid, nodeinsid);
		else
			str = passNode(insgraid, nodeinsid);
		// 跳过节点
		// String str = sc.passNode(insgraid, nodeinsid);
		if(isprint)
		ov.append("正在"+op+"：" + node.getString("nameCN")
				+ "\n返回结果：" + str + "\n\n");
		return true;
	}
	
	public boolean passOrRetry(boolean isRetry,String op,String insgraid,JSONObject node) throws JSONException {
		return passOrRetry(isRetry, op, insgraid, node, true);
	}
	
	public String createStartTaskStr(String shs){
		String[] shArr = shs.split(" ");
		String err = "";
		Map<String,List<String>> res = new HashMap<String, List<String>>();
		for (int i = 0; i < shArr.length; i++) {
			String proj = getProjectNameFromFile(shArr[i]);
			if(SchedulerUtil.isStrNull(proj)){
				err += shArr[i]+"\n";
				continue;
			}
			String[] pros = proj.split("\n");
			if(pros.length != 1){
				err += shArr[i]+":"+proj+"\n";
				continue;
			}
			//rpt_kpi_mobileportal_722081.sh;项目名：loganalysis_mbportal_client_sports|任务名：hive_mobileportal_722081
			String projname = proj.substring(proj.indexOf("：")+1,proj.indexOf("|"));
			String tskname = proj.substring(proj.indexOf("|任务名：")+5);
			if(res.containsKey(projname)){
				res.get(projname).add(tskname + ":" + shArr[i]);
			}else{
				List<String> l = new ArrayList<String>();
				l.add(tskname + ":" + shArr[i]);
				res.put(projname, l);
			}
		}
		String resstr = "";
		for (String proj : res.keySet()) {
			List<String> tsk = res.get(proj);
			String tskn = "";
			String shn = "";
			for (String s : tsk) {
				String[] tsks = s.split(":");
				if(!"".equals(tskn)){
					tskn += "," + tsks[0].trim();
					shn += "," + tsks[1].trim();
				}else{
					shn += tsks[1].trim();
					tskn += tsks[0].trim();
				}
			}
			if(!"".equals(resstr)){
				resstr += ";" + proj + ":" + tskn + ":" + shn + "\n";
			}else{
				resstr += proj + ":" + tskn + ":" + shn + "\n";
			}
			
		}
		return resstr + "\n" +err;
	}
}
