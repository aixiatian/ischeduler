package scheduler.entity;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import scheduler.SchedulerUtil;


public class SmallFileItem {

	String name;
	String size;
	List<SmallFileItem> data;
	String desc;
	String owner;
	String fsize;
	String code;
	String fname;
	
	public static LinkedHashMap<String,String> parseItemMap(String json){
		LinkedHashMap<String,String> res = new LinkedHashMap<String,String>();
		try {
			JSONArray js = SchedulerUtil.getJSONArray("desc",json, "data");
			//"desc": "success"
			int arrsize = js.length();
			for (int i = 0; i < arrsize; i++) {
				
				JSONObject status = js.getJSONObject(i);
				int size = status.getInt("size");
				if(size > 10)
				res.put(status.getString("name"), status.getString("size"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public static LinkedHashMap<String,String> parseItemMap(String json,String subname){
		LinkedHashMap<String,String> res = new LinkedHashMap<String,String>();
		try {
			JSONArray js = SchedulerUtil.getJSONArray("desc",json, "data");
			//"desc": "success"
			int arrsize = js.length();
			for (int i = 0; i < arrsize; i++) {
				
				JSONObject status = js.getJSONObject(i);
				int size = status.getInt("size");
				if(size > 10)
					res.put(status.getString("name"), status.getString("size"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public List<SmallFileItem> parseItemList(String json){
		try {
			JSONArray js = SchedulerUtil.getJSONArray("desc",json, "data");
			//"desc": "success"
			int arrsize = js.length();
			for (int i = 0; i < arrsize; i++) {
				
				JSONObject status = js.getJSONObject(i);
				SmallFileItem item = new SmallFileItem();
				item.setName(status.getString("name"));
				item.setName(status.getString("size"));
				data.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public SmallFileItem parse(String json){
		SmallFileItem si = new SmallFileItem();
		try {
			JSONObject jo = new JSONObject(json);
			//"desc": "success"
			si.setCode(SchedulerUtil.getJsonObjectString(jo, "code"));
			si.setData(parseItemList(json));
			si.setDesc(SchedulerUtil.getJsonObjectString(jo, "desc"));
			si.setOwner(SchedulerUtil.getJsonObjectString(jo, "owner"));
			si.setFsize(SchedulerUtil.getJsonObjectString(jo, "fsize"));
			si.setFname(SchedulerUtil.getJsonObjectString(jo, "fname"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return si;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public List<SmallFileItem> getData() {
		return data;
	}

	public void setData(List<SmallFileItem> data) {
		this.data = data;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFsize() {
		return fsize;
	}

	public void setFsize(String fsize) {
		this.fsize = fsize;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	
	
}
