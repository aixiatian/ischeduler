package scheduler;


interface Constants {
	
	public static final String REST_URL = 					"/schedulerManager/index.php/interface/rest";
	public static final String HPM_REST_URL = 					"/hpm/index.php/scheduler/rest";

	public static final String getNodesForProject = 		"/interface/ins_graph/by_proj_ids";

	public static final String searchNodesForProjectIDs = 	"/interface/def_graph/by_project_ids";
	
	public static final String setStatus = 					"/interface/ins_node/set_status";
	
	public static final String startByDefnode = 			"/interface/ins_graph/start_by_defnode";

	public static final String getInfluncedNodes = 			"/interface/def_graph/by_following_node_ids";
	
	public static final String getProjectsForStatus = 		"/schedulerManager/index.php/interface/project_instances?";
	
	public static final String getAllProjects = 			"/schedulerManager/index.php/interface/projects?";
	
	public static final String hpm = "http://dp.hadoop.data.sina.com.cn/hpm/index.php/scheduler";
	
	public static final String method_smallFileNum = "smallFileNum";

	public static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

	public static final String HPM_NEW = "newdp.hadoop.data.sina.com.cn";

	public static final String GET_PROJECT_FROM_STATUS = "/hpm/index.php/scheduler/project_instances/project_pages";

	//状态
	public static final String STATUS_FAIL = "FAILED";
	public static final String STATUS_SUCCESS = "COMPELETED";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String STATUS_WAIT = "INITED";
	public static final String STATUS_ALL = "ALL";
	public static final String STATUS_RETRY = "READY";

	public static final String HPM_INFLUNCE_URL = "/interface/def_graph/by_following_nodes_ids_proj_pages";
	public static final String PAGE_AFFACTED_PROJECTS_URL = "/hpm/index.php/scheduler/project_affected";
	public static final String BY_FOLLOWING_PROJECTS_IDS_URL = "/interface/def_graph/by_following_proj_ids";


}
