package ch.epfl.unison.mockUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockResponses {

	public final JSONObject loginGETResponseContent;
	public final JSONObject libraryPUTResponseContent;
	public final JSONObject groupsGETResponseContentTest1Test2NoDist;
	public final HashMap<String, HttpResponse> responseMap;
	public final String LIBENTRIES_KEY = "libentries";
	public final String LOGIN_KEY = "loginKEY";
	public final String GROUPS_SUCC_KEY = "groupsSucc";


	public MockResponses() throws JSONException, UnsupportedEncodingException {
		responseMap = new HashMap<String, HttpResponse>();
		
		
		// Phone sends email and password, server's answer is:
		loginGETResponseContent = new JSONObject().put("gid", JSONObject.NULL)
				.put("nickname", "user0").put("uid", "0");

		HttpResponse loginSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		loginSuccess
				.setEntity(new StringEntity(loginGETResponseContent.toString()));
		responseMap.put(LOGIN_KEY, loginSuccess);
		
		
		
		// Phone sends its library, server's answer is:
		libraryPUTResponseContent = new JSONObject().put("success", true);

		HttpResponse responseForLibentriesPUT = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		responseForLibentriesPUT
		.setEntity(new StringEntity(libraryPUTResponseContent.toString()));
		responseMap.put(LIBENTRIES_KEY, responseForLibentriesPUT);
		
		
		
		// Phone asks for groups, server's answer is:
		groupsGETResponseContentTest1Test2NoDist = new JSONObject().put(
				"groups",
				new JSONArray().put(
						new JSONObject().put("nb_users", 0).put("gid", 0)
								.put("name", "test1")
								.put("distance", JSONObject.NULL)).put(
						new JSONObject().put("nb_users", 1).put("gid", 1)
								.put("name", "test2")
								.put("distance", JSONObject.NULL)));
		
		HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		groupsSuccess.setEntity(new StringEntity(groupsGETResponseContentTest1Test2NoDist
				.toString()));
		responseMap.put(GROUPS_SUCC_KEY, groupsSuccess);
	}
}
