package com.douglei.mini.license.client;

/**
 * 授权文件验证器
 * @author DougLei
 */
class LicenseValidator {
	private String publicKey; // 公钥
	private LicenseFile licenseFile; // 授权文件实例
	
	public LicenseValidator(String publicKey) {
		this.publicKey = publicKey;
		this.licenseFile = new LicenseFileReader().read();
	}
	
	/**
	 * 初次验证所有的信息, 包括签名, 用在系统启动时
	 * @return
	 */
	protected ValidationResult verifyFirst() {
		ValidationResult result = licenseFile.signature.verify(publicKey, licenseFile.getContentDigest());
		if(result == null)
			result = verify();
		return result;
	}
	
	/**
	 * 验证所有的信息, 不包括签名, 用在系统运行时
	 * @return
	 */
	protected ValidationResult verify() {
		ValidationResult result = licenseFile.expired.verify();
		if(result == null && licenseFile.ip != null)
			result = licenseFile.ip.verify();
		if(result == null && licenseFile.mac != null)
			result = licenseFile.mac.verify();
		return result;
	}
	
	/**
	 * 获取剩余有效天数
	 * @return
	 */
	public int getLeftDays() {
		return licenseFile.expired.getLeftDays();
	}
}
