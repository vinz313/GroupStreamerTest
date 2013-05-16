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


	public final HttpResponse mainGETGroupsNotDJSuccess;
	public final HttpResponse mainGETGroupsDJSuccess;
	public final HttpResponse mainGETGroupsSuccess;
	

	public final HttpResponse PUTNicknameSuccess;

	public final HttpResponse PUTMasterSuccess;
	
	public final HttpResponse GETPlaylistSuccess;
	public final HttpResponse GET1st2ndTrack;
	public final HttpResponse GET2nd3rdTrack;
	public final HttpResponse GET3rdTrack;
	public final HttpResponse GETNoMoreTracks;
	
	
	public final HttpResponse PUTCurrentSuccess;
	
	public final HttpResponse PUTPasswordSuccess;
	
	public final HttpResponse historyGroupNotFound;
	public final HttpResponse singleGroupAfterCreation;

	public MockResponses() throws JSONException, UnsupportedEncodingException {

		// Phone sends email and password, server's answer is:
		JSONObject loginGETResponseContentSuccess = new JSONObject()
				.put("gid", JSONObject.NULL).put("nickname", "user0")
				.put("uid", "0");

		loginGETSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1,
				1), HttpStatus.SC_OK, "OK");
		loginGETSuccess.setEntity(new StringEntity(
				loginGETResponseContentSuccess.toString()));

		// Phone sends its library, server's answer is:
		JSONObject libraryPUTResponseContentSuccess = new JSONObject().put(
				"success", true);

		responseForLibentriesPUT = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		responseForLibentriesPUT.setEntity(new StringEntity(
				libraryPUTResponseContentSuccess.toString()));

		// Phone asks for groups after having created a group named "newGroup",
		// server's answer is:
		JSONObject groupsGETResponseContent_Test1_Test2_NewGroup_NoDist = new JSONObject()
				.put("groups",
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
		groupsGETSuccessAfterCreation
				.setEntity(new StringEntity(
						groupsGETResponseContent_Test1_Test2_NewGroup_NoDist
								.toString()));

		JSONObject groupsGETResponseContent_Test1_Test2_NoDist = new JSONObject()
				.put("groups",
                        new JSONArray().put(
                                new JSONObject().put("nb_users", 0)
                                        .put("gid", 0).put("name", "test1")
                                        .put("distance", JSONObject.NULL)).put(
                                new JSONObject().put("nb_users", 1)
                                        .put("gid", 1).put("name", "test2")
                                        .put("distance", JSONObject.NULL)).put(
                                new JSONObject().put("nb_users", 1)
                                        .put("gid", 2).put("name", "test3")
                                        .put("distance", JSONObject.NULL)
                                        .put("password", true)));

		groupsGETSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1,
				1), HttpStatus.SC_OK, "OK");
		groupsGETSuccess.setEntity(new StringEntity(
				groupsGETResponseContent_Test1_Test2_NoDist.toString()));

		JSONObject signupSuccessContent = new JSONObject().put("uid", 0);
		signupPOSTSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		signupPOSTSuccess.setEntity(new StringEntity(signupSuccessContent
				.toString()));

		JSONObject signupAlreadyExistContent = new JSONObject().put("message",
				"user already exists").put("error", 2);
		signupPOSTAlreadyExist = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "BAD REQUEST");
		signupPOSTAlreadyExist.setEntity(new StringEntity(
				signupAlreadyExistContent.toString()));

		JSONObject signupInvalidEmailContent = new JSONObject().put("message",
				"e-mail is not valid").put("error", 4);
		signupPOSTInvalidEmail = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "BAD REQUEST");
		signupPOSTInvalidEmail.setEntity(new StringEntity(
				signupInvalidEmailContent.toString()));

		JSONObject mainGETGroupsNotDJContent = new JSONObject()
				.put("track", JSONObject.NULL)
				.put("master", JSONObject.NULL)
				.put("name", "cool group")
				.put("users",
						new JSONArray().put(new JSONObject()
								.put("score", JSONObject.NULL)
								.put("nickname", "nick").put("uid", 0)
								.put("predicted", true)));

        mainGETGroupsSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        mainGETGroupsSuccess.setEntity(new StringEntity(mainGETGroupsNotDJContent.toString()));
        
        
        JSONObject PUTNicknameSuccessContent = new JSONObject().put("success", true);
        PUTNicknameSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        PUTNicknameSuccess.setEntity(new StringEntity(PUTNicknameSuccessContent.toString()));
        
        String[] fakeUsers = {
                "user1", "user2", "user3", "user4",
                "user5", "user6", "user7", "user8", "user9", "user10", "user11", "user12"
        };
        JSONObject groupsGETSuggestionContent = new JSONObject()
                .put("suggestion", true)
                .put("cluster", new JSONObject().put("cid", 0)
                                   .put("lat", 0.0)
                                   .put("lon", 0.0)
                                   .put("gid", 0))
                .put("group", new JSONObject().put("nb_users", 0)
                                .put("gid", 0).put("name", "test1"))                 
                .put("users", new JSONArray(Arrays.asList(fakeUsers)));
        groupsGETSuggestion = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");
        groupsGETSuggestion.setEntity(new StringEntity(groupsGETSuggestionContent.toString()));
		mainGETGroupsNotDJSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		mainGETGroupsNotDJSuccess.setEntity(new StringEntity(mainGETGroupsNotDJContent
				.toString()));
		
		JSONObject mainGETGroupsDJContent = new JSONObject()
		.put("track", JSONObject.NULL)
		.put("master", new JSONObject().put("nickname", "nick").put("uid", 0))
		.put("name", "cool group")
		.put("users",
				new JSONArray().put(new JSONObject()
						.put("score", JSONObject.NULL)
						.put("nickname", "nick").put("uid", 0)
						.put("predicted", true)));
mainGETGroupsDJSuccess = new BasicHttpResponse(new ProtocolVersion(
		"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
mainGETGroupsDJSuccess.setEntity(new StringEntity(mainGETGroupsDJContent
		.toString()));


		//This playlist ID should be the same for "next tracks" requests.
		JSONObject GETPlaylistSuccessContent = new JSONObject().put(
				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c");
		GETPlaylistSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		GETPlaylistSuccess.setEntity(new StringEntity(GETPlaylistSuccessContent
				.toString()));
		
		JSONObject GET1st2ndTrackContent = new JSONObject().put(
				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c").put("tracks",
						new JSONArray().put(new JSONObject()
						.put("title", "Stay (Explicit Edit)")
						.put("local_id", 22)
						.put("artist", "Rihanna Feat Mikky Ekko")
						).put(new JSONObject()
						.put("title", "When I Was Your Man")
						.put("local_id", 21)
						.put("artist", "Bruno Mars")
						));
		GET1st2ndTrack = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		GET1st2ndTrack.setEntity(new StringEntity(GET1st2ndTrackContent
				.toString()));
		
		JSONObject GET2nd3rdTrackContent = new JSONObject().put(
				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c").put("tracks",
						new JSONArray().put(new JSONObject()
						.put("title", "When I Was Your Man")
						.put("local_id", 21)
						.put("artist", "Bruno Mars")
						).put(new JSONObject()
						.put("title", "Skyfall")
						.put("local_id", 19)
						.put("artist", "Adele")
						));
		GET2nd3rdTrack = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		GET2nd3rdTrack.setEntity(new StringEntity(GET2nd3rdTrackContent
				.toString()));
		
		JSONObject GET3rdTrackContent = new JSONObject().put(
				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c").put("tracks",
						new JSONArray().put(new JSONObject()
						.put("title", "Skyfall")
						.put("local_id", 19)
						.put("artist", "Adele")
						));
		GET3rdTrack = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		GET3rdTrack.setEntity(new StringEntity(GET3rdTrackContent
				.toString()));
		
		/* NOT NEEDED FOR NOW
//		JSONObject GET4thTrackContent = new JSONObject().put(
//				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c").put("tracks",
//						new JSONArray().put(new JSONObject()
//						.put("title", "Harlem Shake")
//						.put("local_id", 20)
//						.put("artist", "Baauer")
//								));
//		GET4thTrack = new BasicHttpResponse(new ProtocolVersion("HTTP",
//				1, 1), HttpStatus.SC_OK, "OK");
//		GET4thTrack.setEntity(new StringEntity(GET4thTrackContent
//				.toString()));
		*/
		JSONObject GETNoMoreTracksContent = new JSONObject().put(
				"playlist_id", "3d706c8776a2ebd7c26a6082938a56c223fd505c").put("tracks",
						new JSONArray());
		GETNoMoreTracks = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		GETNoMoreTracks.setEntity(new StringEntity(GETNoMoreTracksContent
				.toString()));
		
		
		JSONObject PUTMasterSuccessContent = new JSONObject().put("success",
				true);
		PUTMasterSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		PUTMasterSuccess.setEntity(new StringEntity(PUTMasterSuccessContent
				.toString()));
		
		
		JSONObject PUTCurrentSuccessContent = new JSONObject().put("success",
				true);
		PUTCurrentSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP",
				1, 1), HttpStatus.SC_OK, "OK");
		PUTCurrentSuccess.setEntity(new StringEntity(PUTCurrentSuccessContent
				.toString()));
		
		JSONObject PUTPasswordSuccessContent = new JSONObject().put("success",
                true);
        PUTPasswordSuccess = new BasicHttpResponse(new ProtocolVersion("HTTP",
                1, 1), HttpStatus.SC_OK, "OK");
        PUTPasswordSuccess.setEntity(new StringEntity(PUTPasswordSuccessContent
                .toString()));
        
//        JSONObject historyGroupNotFoundContent = new JSONObject().put("error", 
//                new JSONArray().put(new JSONObject().put("error", 6).put("message", "group not found")));
//        historyGroupNotFound = new BasicHttpResponse(new ProtocolVersion("HTTP",
//                1, 1), HttpStatus.SC_NOT_FOUND, "Not Found");
        
        JSONObject historyGroupNotFoundContent = new JSONObject().put("error", 6).put("message", "group not found");
        historyGroupNotFound = new BasicHttpResponse(new ProtocolVersion("HTTP",
                1, 1), HttpStatus.SC_NOT_FOUND, "Not Found");
        historyGroupNotFound.setEntity(new StringEntity(historyGroupNotFoundContent.toString()));
        
        JSONObject singleGroupAfterCreationContent = new JSONObject().put("nb_users", 0)
                .put("gid", 5).put("name", "newGroup")
                .put("distance", JSONObject.NULL);
        singleGroupAfterCreation = new BasicHttpResponse(new ProtocolVersion("HTTP",
                1, 1), HttpStatus.SC_OK, "OK");
        singleGroupAfterCreation.setEntity(new StringEntity(singleGroupAfterCreationContent.toString()));
        
	}
}
