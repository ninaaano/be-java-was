package Controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserController{
    public String addUser(HttpRequest httpRequest) {
        User user = new User(httpRequest.getBody("userId"), httpRequest.getBody("password"), httpRequest.getBody("name"), httpRequest.getBody("email"));
        return "/index.html";
    }
}
