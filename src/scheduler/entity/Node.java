package scheduler.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scheduler.SchedulerUtil;

public class Node {

	/***
	 * 
	 * "status": "success",
	"data": {
		"nodes": [{
			"createTime": 1416456247000,
			"outputPath": [],
			"desc": "",
			"runUser": "loganalysis",
			"runIP": "",
			"updateTime": 1420703066465,
			"svnUrl": "https://svn.intra.sina.com.cn/data/myprog/home_yz/loganalysis/mds/mbportal/mds_mbportal_apache/job/",
			"params": "YYYYMMDD",
			"depth": 1,
			"nameCN": "",
			"creator": "libo7",
			"startTime": "04:00",
			"id": "CE2501C9-7C74-4E87-BD45-493E9FBFDE18",
			"content": "job_shell/mds_mbportal_apache_user_data.sh",
			"projectID": "3460f2d0-706a-11e4-bfec-14feb5d379aa",
			"inputPath": [],
			"hadoopVersion": "v2",
			"maxRetry": 0,
			"priority": 0,
			"name": "hive_mds_mbportal_apache_user_data",
			"owner": "libo7",
			"deptID": "17",
			"warningCond": "fail(Y,Y);start(,);length(s,s)"
		}]
	},
	"why": ""
	 */
	String createTime;
	String outputPath;
	String desc;
	String runUser;
	String runIP;
	String updateTime;
	String svnUrl;
	String params;
	String depth;
	String nameCN;
	String creator;
	String startTime;
	String id;
	String content;
	String projectID;
	String inputPath;
	String hadoopVersion;
	String maxRetry;
	String priority;
	String name;
	String owner;
	String deptID;
	String warningCond;
	String status;
	boolean online;

	public static Map<String,Node> getNodeMapFromJson(String nodes){
		Map<String,Node> nodeMap = new HashMap<String, Node>();
		try {
			JSONArray ja = new JSONArray(nodes);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject o = ja.getJSONObject(i);
				Node n = new Node();
				n.setCreateTime(o.getString("createTime"));
				n.setOutputPath(o.getString("outputPath"));
				n.setDesc(o.getString("desc"));
				n.setRunUser(o.getString("runUser"));
				n.setRunIP(o.getString("runIP"));
				n.setUpdateTime(o.getString("updateTime"));
				n.setSvnUrl(o.getString("svnUrl"));
				n.setParams(o.getString("params"));
				n.setDepth(o.getString("depth"));
				n.setNameCN(o.getString("nameCN"));
				n.setCreator(o.getString("creator"));
				n.setStartTime(o.getString("startTime"));
				n.setId(o.getString("id"));
				n.setContent(o.getString("content"));
				n.setProjectID(o.getString("projectID"));
				n.setInputPath(o.getString("inputPath"));
				n.setHadoopVersion(o.getString("hadoopVersion"));
				n.setMaxRetry(o.getString("maxRetry"));
				n.setPriority(o.getString("priority"));
				n.setName(o.getString("name"));
				n.setOwner(SchedulerUtil.getJsonObjectString(o, "owner"));
				n.setDeptID(o.getString("deptID"));
				n.setWarningCond(o.getString("warningCond"));
				n.setStatus(SchedulerUtil.getJsonObjectString(o, "status"));
				String online = SchedulerUtil.getJsonObjectString(o, "online");
				n.setOnline("".equals(online)?false:Boolean.valueOf(online));
				nodeMap.put(n.getName(), n);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nodeMap;
	}

	public boolean getOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getRunUser() {
		return runUser;
	}
	public void setRunUser(String runUser) {
		this.runUser = runUser;
	}
	public String getRunIP() {
		return runIP;
	}
	public void setRunIP(String runIP) {
		this.runIP = runIP;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getNameCN() {
		return nameCN;
	}
	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getHadoopVersion() {
		return hadoopVersion;
	}
	public void setHadoopVersion(String hadoopVersion) {
		this.hadoopVersion = hadoopVersion;
	}
	public String getMaxRetry() {
		return maxRetry;
	}
	public void setMaxRetry(String maxRetry) {
		this.maxRetry = maxRetry;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getWarningCond() {
		return warningCond;
	}
	public void setWarningCond(String warningCond) {
		this.warningCond = warningCond;
	}
	
	@Override
	public String toString() {
		
		return this.name+":"+this.nameCN;
	}
}
