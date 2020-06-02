package com.douglei.tools.instances.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.douglei.tools.utils.CloseUtil;

/**
 * 
 * @author DougLei
 */
public abstract class Scanner {
	protected PathDeDuplicationFilter filter = new PathDeDuplicationFilter(this);
	protected List<String> list = new LinkedList<String>();
	private ClassLoader classLoader;
	protected String[] targetFileSuffix;
	
	private ClassLoader getClassLoader() {
		if(classLoader == null) {
			classLoader = Scanner.class.getClassLoader();
		}
		return classLoader;
	}
	
	/**
	 * 
	 * @param basePath
	 * @return
	 */
	protected URL getResource(String basePath){
		return getClassLoader().getResource(basePath);
	}
	
	/**
	 * 
	 * @param basePath
	 * @return
	 */
	protected Enumeration<URL> getResources(String basePath){
		try {
			return getClassLoader().getResources(basePath);
		} catch (IOException e) {
			throw new ScannerException("在扫描["+basePath+"]路径, getResources()时, 出现异常:", e);
		}
	}
	
	/**
	 * 扫描文件，并加入到list集合中
	 * @param filePath
	 * @param param
	 */
	protected void scanFromFile(String filePath, String param) {
		File firstFile = new File(filePath);
		if(firstFile.isFile()) {
			if(isTargetFile(filePath)) {
				addFileToList(firstFile, param);
			}
			return;
		}
		
		File[] files = listFiles(firstFile);
		if(files != null && files.length > 0){
			for (File file : files) {
				if(file.isDirectory()) {
					scanFromFile(file.getAbsolutePath(), processParamsOnDirectory(file, param));
				}else {
					addFileToList(file, param);
				}
			}
		}
	}
	
	private File[] listFiles(File file) {
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if(file.isDirectory() || isTargetFile(file.getName())) {
					return true;
				}
				return false;
			}
		});
		return files;
	}
	
	/**
	 * 当file时文件夹时, 处理params
	 * @param file
	 * @param param
	 * @return
	 */
	protected abstract String processParamsOnDirectory(File file, String param);

	/**
	 * 添加file到集合中
	 * @param file
	 * @param param
	 */
	protected abstract void addFileToList(File file, String param);

	/**
	 * 从jar包中扫描文件，并加入到list集合中
	 * @param fileUrl
	 * @param basePath
	 */
	protected void scanFromJar(URL fileUrl, String basePath) {
		JarFile jarFile = null;
		JarEntry entry = null;
		try {
			URLConnection urlConnection = fileUrl.openConnection();
			if(urlConnection instanceof JarURLConnection) {
				jarFile = ((JarURLConnection) urlConnection).getJarFile();
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				while(jarEntries.hasMoreElements()) {
					entry = jarEntries.nextElement();
					if(entry.getName().startsWith(basePath) && isTargetFile(entry.getName())) {
						addJarEntryToList(entry);
					}
				}
			}else {
				// TODO 后续可能需要实现其他类型
				throw new UnsupportUrlConnectionException(urlConnection);
			}
		} catch (Exception e) {
			throw new ScannerException("从jar扫描文件时出现异常", e);
		} finally {
			CloseUtil.closeIO(jarFile);
		}
	}
	
	/**
	 * 添加jar实体到集合中
	 * @param entry
	 */
	protected abstract void addJarEntryToList(JarEntry entry);
	
	/**
	 * 是否是要扫描的目标文件
	 * @param fileName
	 * @return
	 */
	protected boolean isTargetFile(String fileName) {
		if(targetFileSuffix == null || targetFileSuffix.length == 0) {
			return true;
		}
		for (String fs : targetFileSuffix) {
			if(fileName.endsWith(fs)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isFile(URL fileUrl) {
		return fileUrl.getProtocol().equals("file");
	}
	protected boolean isJarFile(URL fileUrl) {
		return fileUrl.getProtocol().equals("jar");
	}
	
	/**
	 * 验证要扫描的目标文件后缀, 如果没有.开始, 则加上
	 * @param targetFileSuffix
	 * @return 
	 */
	protected String[] validateTargetFileSuffix(String... targetFileSuffix) {
		if(targetFileSuffix != null && targetFileSuffix.length > 0) {
			for(int i=0;i<targetFileSuffix.length;i++) {
				if(!targetFileSuffix[i].startsWith(".")) {
					targetFileSuffix[i] = "."+targetFileSuffix[i];
				}
			}
		}
		return targetFileSuffix;
	}
	
	/**
	 * 重置要扫描的目标文件后缀
	 * @param targetFileSuffix
	 */
	public void resetTargetFileSuffix(String... targetFileSuffix) {
		this.targetFileSuffix = validateTargetFileSuffix(targetFileSuffix);
	}
	
	public List<String> getResult(){
		return list;
	}
	
	/**
	 * 设置类加载器
	 * @param classLoader
	 * @return
	 */
	public Scanner setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}
	
	/**
	 * 销毁对象
	 */
	public void destroy() {
		list.clear();
		list = null;
	}
	
	/**
	 * 根据指定路径扫描
	 * @param basePath
	 * @return 
	 */
	public abstract List<String> scan(String basePath);
	/**
	 * 根据指定路径，扫描其下所有的文件，获取它们的全名集合
	 * 
	 * 是否搜索相同的路径
	 * 
	 * searchAll = false
	 * 扫描指定的某个路径时, 程序会先在当前项目中搜索该路径, 如果找到了, 则就在该路径下开始扫描目标, 同时, 如果jar中也存在相同的路径, 那么是不会被扫描的
	 * 											             如果没找到, 则就去jar中搜索, 如果搜索到了, 就在该jar的路径下开始扫描, 同时, 如果其他jar中也存在相同路径, 那么是不会被扫描的
	 * 即搜索到指定路径时, 就停止搜索其他相同的路径, 只扫描一个路径
	 * 
	 * searchAll = true
	 * 即搜索所有指定的路径, 对所有满足条件的路径进行扫描
	 * 
	 * @param searchAll
	 * @param basePath
	 * @return 
	 */
	public abstract List<String> scan(boolean searchAll, String basePath);
	
	/**
	 * 根据指定路径，重新扫描其下所有的文件
	 * <p>会清空上一次扫描的结果集</p>
	 * @param basePath
	 * @return
	 */
	public abstract List<String> rescan(String basePath);
	/**
	 * 根据指定路径，重新扫描其下所有的文件
	 * <p>会清空上一次扫描的结果集</p>
	 * @param searchAll @see scan(boolean searchAll, String basePath)
	 * @param basePath
	 * @return
	 */
	public abstract List<String> rescan(boolean searchAll, String basePath);
	
	/**
	 * 指定多个路径，多路径扫描，将最终的结果一次返回
	 * @param basePaths
	 * @return
	 */
	public abstract List<String> multiScan(String... basePaths);
	/**
	 * 指定多个路径，多路径扫描，将最终的结果一次返回
	 * @param searchAll @see scan(boolean searchAll, String basePath)
	 * @param basePaths
	 * @return
	 */
	public abstract List<String> multiScan(boolean searchAll, String... basePaths);
	
	/**
	 * 根据指定路径，重新循环扫描其下所有的文件
	 * <p>会清空上一次扫描的结果集</p>
	 * @param basePaths
	 * @return
	 */
	public abstract List<String> reMultiScan(String... basePaths);
	/**
	 * 根据指定路径，重新循环扫描其下所有的文件
	 * <p>会清空上一次扫描的结果集</p>
	 * @param searchAll @see scan(boolean searchAll, String basePath)
	 * @param basePaths
	 * @return
	 */
	public abstract List<String> reMultiScan(boolean searchAll, String... basePaths);
	
	/**
	 * 替换路径的分隔符
	 * @param path
	 * @return
	 */
	protected abstract String replacePathDelimiter(String path);
	
	/**
	 * 获取路径split后的长度
	 * @param path
	 * @return
	 */
	protected abstract byte pathSplitLength(String path);
}
