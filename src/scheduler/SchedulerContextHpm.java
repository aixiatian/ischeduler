package scheduler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import scheduler.entity.SmallFileItem;

public class SchedulerContextHpm {
	
	String token ;
	public String username;
	String pwd;
	CloseableHttpClient client = null;
	//0 :测试
	//1 :正式
//	static String[] hosts = new String[]{
//		"yz2154.hadoop.data.sina.com.cn:80"
//		,"scheduler.data.sina.com.cn:8080"};
	
	String encodestr = "/";
	
	public static void main(String[] args) {
		SchedulerContextHpm sc = new SchedulerContextHpm();
		sc.loginhpm("dongkai3", "dk@#0213");
		
//		String name = "dw/mds/mds_mbportal_client_news_column";
//		String name = "dw/mds/mds_mbportal_client_bhv_terminate";
//		String name = "dw/mds/mds_mbportal_client_bhv_event";
////		String name = "dw/mds/mds_finance_simulate_trade_users";
//		String m = URLEncoder.encode(name);
//		String bd = sc.getSmallFiles(m);
//		Map<String,String> map = SmallFileItem.parseItemMap(bd);
//		sc.getPartitionInfo(sc, name, map,"e:/work/2017/08/21","org");
		sc.startNode();
	}
	
	public void getPartitionInfo(SchedulerContextHpm sc,String name,Map<String,String> map,String outPath,String otherpath){
		String base = encodestr;
		if(!SchedulerUtil.isStrNull(otherpath))
			base = encodestr+otherpath;
		TreeMap<String,String> resmap = new TreeMap<String,String>();
		StringBuffer res = new StringBuffer();
		Map<String,String> maps = null;
		for (String key : map.keySet()) {
			if (!base.equals("/")) {
				String tem = name + encodestr + key + base;
				String bd = sc.getSmallFiles(URLEncoder.encode(tem));
				maps = SmallFileItem.parseItemMap(bd);
				for (String k : maps.keySet()) {
					resmap.put(key + "/channel=" + k,maps.get(k));
				}
			} else
				resmap.put(key ,map.get(key));
		}
		
		for (String key : resmap.keySet()) {
			res.append(key + "\n");
		}
		SchedulerUtil.writeFile(outPath+"/partitionInfo", res.toString());
	}

	public String getLt() throws IOException {
		/***
		 *
		Accept-Encoding:gzip, deflate, sdch
		Accept-Language:zh-CN,zh;q=0.8
		Connection:keep-alive
		Cookie:SINAGLOBAL=61.135.152.133_1494554422.830881; UOR=mobile.data.sina.com.cn,cj.sina.com.cn,; SGUID=1495418972644_41103942; SUB=_2AkMufscBf8NxqwJRmP8czG_kaI9wywHEieKYIjbaJRMyHRl-yD9jqkdZtRAVTGTNhn0151kFhrWbj1X-tHbEoA..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9Wh1zKlpj_ZXVfD_T2g9725w; U_TRS1=00000055.3b3c2398.59310420.e88aa75b; vjuids=72aa1aaf4.15cd2ed5afa.0.83a11837; lxlrtst=1501462726_o; ULV=1503397902933:34:12:5:61.135.152.133_1503397901.10660:1503397900913; lxlrttp=1502410412; vjlast=1498199023.1503398044.11; auth_type=erp
		Host:cas.erp.sina.com.cn
		Referer:http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/projects
		User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
		 */
		HttpGet get = new HttpGet("http://cas.erp.sina.com.cn/cas/login?ext=&service=http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/project_instances");
		get.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Host", "cas.erp.sina.com.cn");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		CloseableHttpResponse resp = client.execute(get);
		String body = EntityUtils.toString(resp.getEntity(), "utf-8");
		String lt = body.split("name=\"lt\" value=\"")[1];
		return lt;
	}
	
	public boolean loginhpm(String username,String pwd) {
		client = HttpClients.createDefault();
		if(SchedulerUtil.isStrNull(this.username))
			this.username = username;
		if(SchedulerUtil.isStrNull(this.pwd))
			this.pwd = pwd;
		boolean success = false;
//		HttpGet get = new HttpGet("http://cas.erp.sina.com.cn/cas/login?ext=&service=http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/project_instances");
//		get.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
//		get.addHeader("Accept-Encoding", "gzip, deflate");
//		get.addHeader("Accept-Language", "zh-CN");
//		get.addHeader("Connection", "Keep-Alive");
//		get.addHeader("Host", "cas.erp.sina.com.cn");
//		get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
//
		try {
//			CloseableHttpResponse resp = client.execute(get);
//			String body = EntityUtils.toString(resp.getEntity(), "utf-8");
//			String lt = body.split("name=\"lt\" value=\"")[1];
//			lt = lt.substring(0,lt.indexOf("\""));
			String body = "";
//			System.out.println("lt->"+lt);
			String lt = getLt();

			HttpPost post = new HttpPost("https://cas.erp.sina.com.cn/cas/login?ext=&service=http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/project_instances");
			post.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			post.addHeader("Accept-Encoding", "gzip, deflate");
			post.addHeader("Accept-Language", "zh-CN");
			post.addHeader("Cache-Control", "no-cache");
			post.addHeader("Connection", "Keep-Alive");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Host", "cas.erp.sina.com.cn");
			post.addHeader("Referer", "https://cas.erp.sina.com.cn/cas/login?ext=&service=http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/project_instances");
			post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
			
			Map<String,String> parameterMap = new HashMap<String,String>();
			parameterMap.put("auth_type", "ldap");
			parameterMap.put("ext", "");
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
			
//			System.out.println("body->"+body);
			
			if(code == 200){
				String loginUrl = body.substring(body.indexOf("\"")+1, body.lastIndexOf("\""));
				presp.close();
				HttpGet login = new HttpGet(loginUrl);
				
				login.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				login.addHeader("Accept-Encoding", "gzip,deflate,sdch");
				login.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
				login.addHeader("Connection", "keep-alive");
				login.addHeader("User-Agent", "Mozilla/5.0(Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/31.0.1650.63 Safari/537.36");
				
				HttpResponse presp1 = client.execute(login);
				
				body = EntityUtils.toString(presp1.getEntity(), "utf-8");
				
//				System.out.println("login_body->"+body);
//				String[] secondUrlPara = body.split("'");
//				String secondLoginurl = secondUrlPara[1];
//				HttpGet getsec = new HttpGet(secondLoginurl);
//				getsec.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
//				getsec.addHeader("Accept-Encoding", "gzip, deflate");
//				getsec.addHeader("Accept-Language", "zh-CN");
//				getsec.addHeader("Connection", "Keep-Alive");
//				getsec.addHeader("Host", "dp.hadoop.data.sina.com.cn");
//				getsec.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");
//
//				CloseableHttpResponse secresp = client.execute(getsec);
//				String bodysec = EntityUtils.toString(secresp.getEntity(), "utf-8");

				getTokenStr(loginUrl);
				
//				System.out.println(token);
				success = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	String getSmallFiles(String name){
		String url = "http://dp.hadoop.data.sina.com.cn/hpm/common/js/AdminLTE/dirtee/data/data.php?";
		url += "name="+name;
		url += "&count=1000";
		url += "&owner=loganalysis";
		url += "&method="+Constants.method_smallFileNum;
		
		HttpGet get = new HttpGet(url);
		
		get.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		get.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		get.addHeader("Connection", "keep-alive");
		get.addHeader("Host", "dp.hadoop.data.sina.com.cn");
		get.addHeader("Referer", "http://dp.hadoop.data.sina.com.cn/hpm/index.php/statistic/personal_small/detail/loganalysis/50");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
		get.addHeader("X-Requested-With", "XMLHttpRequest");
		
		
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

	public void startNode(){
		try {
			HttpPost post = new HttpPost("http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/rest");
			post.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			post.addHeader("Accept-Encoding", "gzip, deflate");
			post.addHeader("Accept-Language", "zh-CN");
			post.addHeader("Cache-Control", "no-cache");
			post.addHeader("Connection", "Keep-Alive");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Host", "newdp.hadoop.data.sina.com.cn");
			post.addHeader("Referer", "http://newdp.hadoop.data.sina.com.cn/hpm/index.php/scheduler/projects");
			post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3; .NET4.0E)");

//			post = new HttpPost("http://10.39.2.131:8111/interface/ins_graph/start_by_defnode");
			Map<String,String> parameterMap = new HashMap<String,String>();
			parameterMap.put("defIDs", "03E7FDF9-E76A-44C7-B6B1-8132EF228D8A");
			parameterMap.put("paramMap", "{\"YYYYMMDD\":\"20170611\",\"test\":\"1\"}");
			parameterMap.put("rdurl", "/interface/ins_graph/start_by_defnode");
//			parameterMap.put("ssn","200720");
//			parameterMap.put("userName","dongkai3");
//			parameterMap.put("token",token);

			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
			post.setEntity(postEntity);

			CloseableHttpResponse presp = client.execute(post);
			int code = presp.getStatusLine().getStatusCode();
			String body = EntityUtils.toString(presp.getEntity(), "utf-8");
			System.out.println(code + "-->" + body);
		}catch (Exception e){

		}


	}
	
}
