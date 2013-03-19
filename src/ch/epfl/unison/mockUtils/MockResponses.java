package ch.epfl.unison.mockUtils;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockResponses {

	public final HttpResponse loginGETSuccess;
	public final HttpResponse responseForLibentriesPUT;
	public final HttpResponse groupsGETSuccess;

	public MockResponses() throws JSONException, UnsupportedEncodingException {
	
		
		// Phone sends email and password, server's answer is:
		JSONObject loginGETResponseContentSuccess = new JSONObject().put("gid", JSONObject.NULL)
				.put("nickname", "user0").put("uid", "0");

		loginGETSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		loginGETSuccess
				.setEntity(new StringEntity(loginGETResponseContentSuccess.toString()));
		
		
		
		// Phone sends its library, server's answer is:
		JSONObject libraryPUTResponseContentSuccess = new JSONObject().put("success", true);

		responseForLibentriesPUT = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		responseForLibentriesPUT
		.setEntity(new StringEntity(libraryPUTResponseContentSuccess.toString()));
		
		
		
		// Phone asks for groups, server's answer is:
		JSONObject groupsGETResponseContentTest1Test2NoDist = new JSONObject().put(
				"groups",
				new JSONArray().put(
						new JSONObject().put("nb_users", 0).put("gid", 0)
								.put("name", "test1")
								.put("distance", JSONObject.NULL)).put(
						new JSONObject().put("nb_users", 1).put("gid", 1)
								.put("name", "test2")
								.put("distance", JSONObject.NULL)));
		
		groupsGETSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		groupsGETSuccess.setEntity(new StringEntity(groupsGETResponseContentTest1Test2NoDist
				.toString()));
	}
}
