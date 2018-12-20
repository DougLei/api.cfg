package test;

import java.io.File;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;


public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		File sourceFile = new File("C:\\Users\\StoneKing\\Desktop\\修改业务建模子表的字段名.txt");
		File targetFile = new File("C:\\Users\\StoneKing\\Desktop\\bbbb.txt");
		FileCopyUtils.copy(sourceFile, targetFile);
	}
}
