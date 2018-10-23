package test;

import java.util.List;

import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;




public class TestMain {
	private int a;
	
	public static void main(String[] args) {
		List<ResourceMetadataInfo> resourceMetadataInfos = (List<ResourceMetadataInfo>) null;
		System.out.println(resourceMetadataInfos);
		
		TestMain tm = new TestMain();
		tm.a++;
		tm.a++;
		System.out.println(tm.a);
	}
}
