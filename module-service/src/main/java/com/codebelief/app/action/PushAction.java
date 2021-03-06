package com.codebelief.app.action;

import com.codebelief.app.pushupdate.PushMail;
import com.codebelief.app.scraper.Controller;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author: Wray Zheng
 * @date: 2017-12-10
 * @description: 推送当前用户的所有订阅更新内容
 */
public class PushAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private int urlID = -1;
	private boolean success = false;
	private String errorMsg;
	
	@Override
	public String execute() throws Exception {
		String userName = (String)ActionContext.getContext().getSession().get("userName");
		if(userName == null) {
			errorMsg = "用户未登录！";
			return ERROR;
		}
		try {
			Controller.execute();
			PushMail.PushUpdateMail(userName);
		} catch(Exception e) {
			e.printStackTrace(System.err);
			errorMsg = "推送失败！";
			return ERROR;
		}
		success = true;
		return SUCCESS;
	}

	public int getUrlID() {
		return urlID;
	}

	public void setUrlID(int urlID) {
		this.urlID = urlID;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
