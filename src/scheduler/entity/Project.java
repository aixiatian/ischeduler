package scheduler.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scheduler.SchedulerUtil;

public class Project {

	/***
	 * "id": "94c6ad84-9a42-11e4-bfec-14feb5d379aa",
		"name": "loganalysis_video_pcclient_day",
		"desc": "\u89c6\u9891PC\u5ba2\u6237\u7aef\u76f8\u5173\u6307\u6807_hive",
		"parentId": "",
		"deptId": "17",
		"nodeCount": "7"
		"graphInsId":"2AD979AD-C29A-4D98-DD61-50EF3D48FD16",
		"createTime":"1473215964565",
		"compTime":"0",
		"startUser":"dongkai3"
		"status":"RUNNING",
		"hours":"24",
		"startTime":"1473215964736",
		"endTime":"0"
	 */
	
	String id;
	String name;
	String desc;
	String parentId;
	String deptId;
	String nodeCount;
	String graphInsId;
	String createTime;
	String compTime;
	String startUser;
	String status;
	String hours;
	String startTime;
	String endTime;
	
	public static Map<String,Project> getProjectsFromJsonStr(String projStrs){
		Map<String,Project> projs = new HashMap<String,Project>();
		try {
			JSONArray ja = SchedulerUtil.getJSONArray(projStrs, "data");
			if(ja == null)
				return null;
			for (int i = 0; i < ja.length(); i++) {
				JSONObject o = ja.getJSONObject(i);
				Project p = new Project();
				p.setId(o.getString("id"));
				p.setName(o.getString("name"));
				p.setDesc(o.getString("desc"));
				p.setParentId(o.getString("parentId"));
				p.setDeptId(o.getString("deptId"));
				p.setNodeCount(o.getString("nodeCount"));
				p.setGraphInsId(SchedulerUtil.getJsonObjectString(o, "graphInsId"));
				p.setCreateTime(SchedulerUtil.getJsonObjectString(o, "createTime"));
				p.setCompTime(SchedulerUtil.getJsonObjectString(o, "compTime"));
				p.setStartUser(SchedulerUtil.getJsonObjectString(o, "startUser"));
				p.setStatus(SchedulerUtil.getJsonObjectString(o, "status"));
				p.setHours(SchedulerUtil.getJsonObjectString(o, "hours"));
				p.setStartTime(SchedulerUtil.getJsonObjectString(o, "startTime"));
				p.setEndTime(SchedulerUtil.getJsonObjectString(o, "endTime"));
				projs.put(o.getString("name")+"`"+SchedulerUtil.getJsonObjectString(o, "startTime"), p);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return projs;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(String nodeCount) {
		this.nodeCount = nodeCount;
	}
	
	public String getGraphInsId() {
		return graphInsId;
	}

	public void setGraphInsId(String graphInsId) {
		this.graphInsId = graphInsId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCompTime() {
		return compTime;
	}

	public void setCompTime(String compTime) {
		this.compTime = compTime;
	}

	public String getStartUser() {
		return startUser;
	}

	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		
		return this.name;
	}
	
}
