package com.douglei.mini.license.client;

/**
 * 自动的授权文件验证器
 * @author DougLei
 */
public class AutoLicenseValidator extends LicenseValidator{
	private boolean start;
	private ValidationResult result; // 验证结果
	
	public AutoLicenseValidator(String publicKey) {
		super(publicKey);
	}
	
	/**
	 * 自动验证
	 * @return 并返回验证结果
	 */
	ValidationResult autoVerify() {
		if(result == null)
			result = verify();
		return result;
	}
	
	/**
	 * 由外部更新验证结果
	 * @param result
	 */
	void updateResult(ValidationResult result) {
		this.result = result;
	}
	
	/**
	 * 启动验证
	 */
	public void start() {
		if(!start) {
			start = true;
			result = verifyFirst();
			if(result == null) 
				new AutoLicenseValidatorThread("auto.license.validator", this).start();
		}
	}
	
	/**
	 * 获取验证结果, 验证结果只要为null, 则证明验证通过
	 * @return
	 */
	public ValidationResult getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("授权文件验证结果: %s, 剩余有效天数: %d", result==null?"正常":result, getLeftDays());
	}
}