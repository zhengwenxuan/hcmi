package com.hjw.webService.client.erfuyuan.bean;

import com.google.gson.Gson;

public class Response_clientRegister {
	
	private String status="";//	返回状态码	正常：10000，体检者ID不正确或不存在：12001，返回失败： 12002
	private Result_clientRegister result;
	private String message="";
	
	public static void main(String[] args) {
		String json = "{\"message\":\"<DataContent content=\"client_add_ok\" counter=\"\" number_3party=\"\"><err msg=\"\">0</err><client id=\"3635\" number=\"001\" categoryid=\"319妇科\" time_enter=\"2018-12-14 19:32:14\" name=\"nan\" card=\"201812140003\" wp=\"TD_妇科;\"><pos float=\"1.000000e+004\">-1</pos><score wnd=\"\" staffer=\"\"></score><reservation>2018-12-14 19:32:14</reservation></client></DataContent>\",\"result\":{\"clientId\":\"201812140003\",\"firstQueue\":\319妇科\",\"msg\":\"0\",\"name\":\"nan\"},\"status\":10000}";
		Response_clientRegister response = new Gson().fromJson(json, Response_clientRegister.class);
		System.out.println(response.getStatus());
	}
	
	public String getStatus() {
		return status;
	}
	public Result_clientRegister getResult() {
		return result;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setResult(Result_clientRegister result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
