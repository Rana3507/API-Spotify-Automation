package com.spotify.oauth2.tests;
import com.spotify.oauth2.api.application.Api.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    public Playlist playlistBuilder(String name, String description, boolean _public){
        return  new Playlist().
                setName(name).setDescription(description).setPublic(_public);
    }
     public void assertPlaylistEqual(Playlist requestPlaylist, Playlist responsePlaylist){

         assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
         assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
         assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
     }
     public void assertStatusCode(int actualStatusCode, int expectedStatusCode){
         assertThat(actualStatusCode, equalTo(expectedStatusCode));

     }

     public void  assertError(Error responseErr,int expectedStatusCode, String expectedMsg){
         assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
         assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));

     }



    @Description("This test attempts to log into the website using a login and a password. Fails if any error happens.\n\nNote that this test does not test 2-Factor Authentication.")
    @Severity(CRITICAL)
    @Owner("John Doe")
    @Link(name = "Website", url = "https://dev.example.com/")
    @Issue("AUTH-123")
    @TmsLink("TMS-456")
     @Test
    public void CreateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder("Playlist created using API Test", "Description for the Playlist created using API", false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), 201);
        assertPlaylistEqual(response.as(Playlist.class), requestPlaylist);

    }
    @Test
    public void GetAPlaylist(){
        Playlist requestPlaylist =playlistBuilder("Playlist created using API Test", "Description for the Playlist created using API", true);

        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.statusCode(), 200);
        assertPlaylistEqual(response.as(Playlist.class), requestPlaylist);
    }

    @Test
    public void UpdateAPlaylist(){

        Playlist requestPlaylist = playlistBuilder("New Playlist", "New Playlist Description", false);
        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), 200);
    }

    @Test
    public void NotableToCreateAPlaylistWithoutName(){
        Playlist requestPlaylist = playlistBuilder("","Description for the Playlist created using API", false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), 400);
        assertError(response.as(Error.class),400, "Missing required field: name");

    }


    @Test
    public void NotableToCreateAPlaylistWithoutExpiredToken(){
        String invalid_token = "123456";

        Playlist requestPlaylist = playlistBuilder("Playlist created using API Test", "Description for the Playlist created using API", false);
        Response response = PlaylistApi.post(invalid_token,requestPlaylist);
        assertStatusCode(response.statusCode(),401);
        assertError(response.as(Error.class),401, "Invalid access token");


    }

}
