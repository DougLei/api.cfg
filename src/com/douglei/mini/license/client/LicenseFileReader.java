package com.douglei.mini.license.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.tools.instances.scanner.FileScanner;

/**
 * 
 * @author DougLei
 */
class LicenseFileReader {
	private static final Logger logger = LoggerFactory.getLogger(LicenseFileReader.class);
	
	/**
	 * 读取并获得授权文件实例
	 * @param resource 授权文件的资源路径(基于java项目的路径)
	 * @return
	 */
	public LicenseFile read() {
		LicenseFile licenseFile = new LicenseFile();
		String path = new FileScanner(".license").scan("").get(0);
		logger.info("加载授权文件: {}", path);
		try(InputStream input = FileScanner.readByScanPath(path)){
			ByteArrayOutputStream out = new ByteArrayOutputStream(300);
			int privateKey = input.read();
			int separator = input.read()^privateKey;
			int b;
			while((b=input.read()) > -1) {
				b = b^privateKey;
				if(b == separator) {
					licenseFile.setContent(new String(out.toByteArray()));
					out.reset();
				}else {
					out.write(b);
				}
			}
		}catch(Exception e) {
		}
		return licenseFile;
	}
}
