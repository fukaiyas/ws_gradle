package com.bugworm.sample;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//ここの{}で囲まれた値が
@ServerEndpoint("/sample/{type}")
public class SampleWebSocketServer {

    private static Optional<Session> masterSession = Optional.empty();
    private static final Set<Session> children = new HashSet<>();

    //ここの@PathParam引数に渡される。Integerとかでもできるらしい。
    @OnOpen
    public void open(@PathParam("type") String type, Session session){
        if("master".equals(type)){
            System.out.println("master connected");
            masterSession = Optional.of(session);
        }else{
            System.out.println("children connected");
            children.add(session);
        }
    }

    @OnClose
    public void close(@PathParam("type") String type, Session session){
        if("master".equals(type)){
            System.out.println("master closed");
            masterSession = Optional.empty();
        }else{
            System.out.println("children closed : " + children.remove(session));
        }
    }

    @OnMessage
    public String action(String message)throws IOException{
        System.out.println("-----------  " + message);
        for(Session s : children){
            s.getBasicRemote().sendText(message);
        }
        return message;
    }
}
