package adri.suys.un_mutescan.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import adri.suys.un_mutescan.model.User;
import adri.suys.un_mutescan.viewinterfaces.LoginViewInterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock private LoginViewInterface viewInterface;
    private Gson gson;
    private LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        presenter = new LoginPresenter(viewInterface);
    }

    @Test
    public void testConnectionSuccess(){
        presenter.setPassword("testtest");
        try {
            JSONObject jsonObject = new JSONObject(jsonConnectionOK());
            presenter.handleJSONObject(jsonObject, gson);
            verify(viewInterface).showHelloToast("Ro Bot");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnectionFailWrongPwd(){
        presenter.setPassword("testtests");
        try {
            JSONObject jsonObject = new JSONObject(jsonConnectionOK());
            presenter.handleJSONObject(jsonObject, gson);
            verify(viewInterface).showBadCredentialsToast();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnectionFailUnknown(){
        presenter.setPassword("testtests");
        try {
            JSONObject jsonObject = new JSONObject(jsonConnectionKO_Unknown());
            presenter.handleJSONObject(jsonObject, gson);
            verify(viewInterface).showUnvalidUsernameToast();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnectionFailNoRight(){
        presenter.setPassword("testtests");
        try {
            JSONObject jsonObject = new JSONObject(jsonConnectionKO_NoRight());
            presenter.handleJSONObject(jsonObject, gson);
            verify(viewInterface).showCantUseAppToast();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String jsonConnectionOK(){
        return "{\n" +
                "    \"id\": 417,\n" +
                "    \"username\": \"1234@test.be\",\n" +
                "    \"name\": \"Ro Bot\",\n" +
                "    \"password\": \"$2y$13$Ck2Btgv1mMXe7VlqLyCJ6.w2XDWg.TCfWZXEAWlaq/kww2RNDbWOi\",\n" +
                "    \"error\": \"\"\n" +
                "}";
    }

    private String jsonConnectionKO_NoRight(){
        return "{\n" +
                "    \"id\": \"\",\n" +
                "    \"username\": \"logan.chale@gmail.com\",\n" +
                "    \"name\": \"\",\n" +
                "    \"password\": \"\",\n" +
                "    \"error\": \"Vous ne pouvez pas utiliser l'application. Vous devez être gestionnaire de campagnes.\"\n" +
                "}";
    }

    private String jsonConnectionKO_Unknown(){
        return "{\n" +
                "    \"id\": \"\",\n" +
                "    \"username\": \"\",\n" +
                "    \"name\": \"\",\n" +
                "    \"password\": \"\",\n" +
                "    \"error\": \"Cet utilisateur n'existe pas.\"\n" +
                "}";
    }
}