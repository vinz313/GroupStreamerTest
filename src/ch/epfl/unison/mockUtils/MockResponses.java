package ch.epfl.unison.mockUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockResponses {

	public final JSONObject loginGETResponseContent;
	public final JSONObject libraryPUTResponseContent;
	public final JSONObject groupsGETResponseContent;

	public MockResponses() throws JSONException {
		loginGETResponseContent = new JSONObject().put("gid", JSONObject.NULL)
				.put("nickname", "user0").put("uid", "0");

		// Phone sends email and password, server's answer is:
		libraryPUTResponseContent = new JSONObject().put("success", true);

		// Phone asks for groups, server's answer is:
		groupsGETResponseContent = new JSONObject().put(
				"groups",
				new JSONArray().put(
						new JSONObject().put("nb_users", 0).put("gid", 0)
								.put("name", "test1")
								.put("distance", JSONObject.NULL)).put(
						new JSONObject().put("nb_users", 1).put("gid", 1)
								.put("name", "test2")
								.put("distance", JSONObject.NULL)));
	}
}
