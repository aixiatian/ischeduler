package scheduler.entity;

public class ErrProject {
	
	/***
	 * 
	 *{"status":"success",
	 *"why":"",
	 *"data":[
	 *{"id":"ae003556-334d-11e4-bfec-14feb5d379aa29509C4B-CF17-D1F6-3EAE-D388D2F8354C@24",
	 *"name":"test_video_playlog",
	 *"desc":"\u89c6\u9891\u65e5\u5fd7\u5904\u7406\u6d4b\u8bd5",
	 *"parentId":"",
	 *"projectId":"ae003556-334d-11e4-bfec-14feb5d379aa",
	 *"deptId":"17",
	 *"graphInsId":"29509C4B-CF17-D1F6-3EAE-D388D2F8354C",
	 *"createTime":"1453284363922","compTime":"0",
	 *"startUser":"dongkai3","nodeCount":"1","status":"FAILED",
	 *"hours":"24","startTime":"1453358307000","endTime":"0"
	 *},
	 *
	 *{"id":"ae003556-334d-11e4-bfec-14feb5d379aaAFCBA452-9153-8E8E-A60B-E203C22330A2@24",
	 *"name":"test_video_playlog","desc":"\u89c6\u9891\u65e5\u5fd7\u5904\u7406\u6d4b\u8bd5",
	 *"parentId":"",
	 *"projectId":"ae003556-334d-11e4-bfec-14feb5d379aa",
	 *"deptId":"17",
	 *"graphInsId":"AFCBA452-9153-8E8E-A60B-E203C22330A2",
	 *"createTime":"1453344448787","compTime":"0","startUser":"dongkai3",
	 *"nodeCount":"1","status":"FAILED","hours":"24","startTime":"1453344446351","endTime":"0"
	 *}
	 *]
	 *}
	 */
	//projectId+graphInsId+²ÎÊý
	String id;
	String name;
	String desc;
	String parentId;
	String projectId;
	String deptId;
	String graphInsId;
	String createTime;
	String compTime;
	String startUser;
	String nodeCount;
	String status;
	String hours;
	String startTime;
	String endTime;
	
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
	public String getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(String nodeCount) {
		this.nodeCount = nodeCount;
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
	
	
	
}
