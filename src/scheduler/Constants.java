package scheduler;


public class Constants {
	
	public static final String REST_URL = 					"/schedulerManager/index.php/interface/rest";
	
	public static final String getNodesForProject = 		"/interface/ins_graph/by_proj_ids";
	
	public static final String searchNodesForProjectIDs = 	"/interface/def_graph/by_project_ids";
	
	public static final String setStatus = 					"/interface/ins_node/set_status";
	
	public static final String startByDefnode = 			"/interface/ins_graph/start_by_defnode";
	
	public static final String getInfluncedNodes = 			"/interface/def_graph/by_following_node_ids";
	
	public static final String getProjectsForStatus = 		"/schedulerManager/index.php/interface/project_instances?";
	
	public static final String getAllProjects = 			"/schedulerManager/index.php/interface/projects?";
	
	public static final String hpm = "http://dp.hadoop.data.sina.com.cn/hpm/index.php/scheduler";
	
	public static final String method_smallFileNum = "smallFileNum";
}
