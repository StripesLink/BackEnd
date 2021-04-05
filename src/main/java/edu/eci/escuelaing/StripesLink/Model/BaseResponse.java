package edu.eci.escuelaing.StripesLink.Model;

public class BaseResponse {

	private String res;

	public BaseResponse() {
	}

	public BaseResponse(String res) {
		this.res = res;
	}

	public String getResponse() {
		return res;
	}

	public void setResponse(String res) {
		this.res = res;
	}
}
