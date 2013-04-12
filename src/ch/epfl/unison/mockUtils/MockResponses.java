package ch.epfl.unison.mockUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MockResponses {

	public final HttpResponse loginGETSuccess;
	public final HttpResponse responseForLibentriesPUT;

	public final HttpResponse signupPOSTSuccess;
	public final HttpResponse signupPOSTAlreadyExist;
	public final HttpResponse signupPOSTInvalidEmail;

	public final HttpResponse groupsGETSuccessAfterCreation;
	public final HttpResponse groupsGETSuccess;
	public final HttpResponse groupsGETSuggestion;
	
	public final HttpResponse mainGETGroupsSuccess;
	
	public final HttpResponse PUTNicknameSuccess;


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
		
		// Phone asks for groups after having created a group named "newGroup", server's answer is:
        JSONObject groupsGETResponseContent_Test1_Test2_NewGroup_NoDist = new JSONObject().put(
                "groups",
                    new JSONArray()
                            .put(new JSONObject().put("nb_users", 0)
                                    .put("gid", 0).put("name", "test1")
                                    .put("distance", JSONObject.NULL))
                            .put(new JSONObject().put("nb_users", 1)
                                    .put("gid", 1).put("name", "test2")
                                    .put("distance", JSONObject.NULL))
                            .put(new JSONObject().put("nb_users", 0)
                                    .put("gid", 2).put("name", "newGroup")
                                    .put("distance", JSONObject.NULL)));

        groupsGETSuccessAfterCreation = new BasicHttpResponse(
            new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        groupsGETSuccessAfterCreation.setEntity(new StringEntity(groupsGETResponseContent_Test1_Test2_NewGroup_NoDist
                .toString()));
        
        JSONObject groupsGETResponseContent_Test1_Test2_NoDist = new JSONObject().put(
                "groups",
                    new JSONArray()
                            .put(new JSONObject().put("nb_users", 0)
                                    .put("gid", 0).put("name", "test1")
                                    .put("distance", JSONObject.NULL))
                            .put(new JSONObject().put("nb_users", 1)
                                    .put("gid", 1).put("name", "test2")
                                    .put("distance", JSONObject.NULL)));

        groupsGETSuccess = new BasicHttpResponse(
            new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        groupsGETSuccess.setEntity(new StringEntity(groupsGETResponseContent_Test1_Test2_NoDist
                .toString()));
			

		
		JSONObject signupSuccessContent = new JSONObject().put("uid", 0);
		signupPOSTSuccess = new BasicHttpResponse(new ProtocolVersion(
                "HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		signupPOSTSuccess.setEntity(new StringEntity(signupSuccessContent.toString()));
		
		
		JSONObject signupAlreadyExistContent = new JSONObject().put("message", "user already exists").put("error", 2);
		signupPOSTAlreadyExist = new BasicHttpResponse(new ProtocolVersion(
                "HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "BAD REQUEST");
		signupPOSTAlreadyExist.setEntity(new StringEntity(signupAlreadyExistContent.toString()));
		
		JSONObject signupInvalidEmailContent = new JSONObject().put("message", "e-mail is not valid").put("error", 4);
        signupPOSTInvalidEmail = new BasicHttpResponse(new ProtocolVersion(
                "HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "BAD REQUEST");
        signupPOSTInvalidEmail.setEntity(new StringEntity(signupInvalidEmailContent.toString()));

		JSONObject mainGETGroupsContent = new JSONObject()
				.put("track", JSONObject.NULL)
				.put("master", JSONObject.NULL)
				.put("name", "cool group")
				.put("users",
						new JSONArray().put(new JSONObject()
								.put("score", JSONObject.NULL)
								.put("nickname", "nick").put("uid", 0)
								.put("predicted", true)));
        mainGETGroupsSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        mainGETGroupsSuccess.setEntity(new StringEntity(mainGETGroupsContent.toString()));
        
        
        JSONObject PUTNicknameSuccessContent = new JSONObject().put("success", true);
        PUTNicknameSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        PUTNicknameSuccess.setEntity(new StringEntity(PUTNicknameSuccessContent.toString()));
        
        String[] fakeUsers = {
                "user1", "user2", "user3", "user4",
                "user5", "user6", "user7", "user8", "user9", "user10", "user11", "user12"
        };
        JSONObject groupsGETSuggestionContent = new JSONObject()
                .put("users", new JSONArray(Arrays.asList(fakeUsers)))
                .put("group", new JSONObject().put("nb_users", 0)
                                .put("gid", 0).put("name", "test1"));
        groupsGETSuggestion = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        groupsGETSuggestion.setEntity(new StringEntity(groupsGETSuggestionContent.toString()));
	}
}
