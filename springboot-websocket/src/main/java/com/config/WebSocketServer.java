package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.SocketMsg;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author Kevin
 * @date 2023/7/29 16:14
 * @Description: websocket的具体实现类
 * 但在springboot中连容器都是spring管理的。
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，
 * 所以可以用一个静态set保存起来。
 */
@ServerEndpoint(value = "/websocket/{userName}")
@Component
public class WebSocketServer {

    /**
     *  依赖保存每个客户端对应的WebSocket
     */

    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String,Session> mapSocket = new ConcurrentHashMap<>();
    /**
     *  与客户端的连接会话，需要通过它来给客户发送信息
     */
    private Session session;

    /**
     *  客户端连接的用户名
     */
    private String userName;

    /**
     * 连接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName){
        this.session = session;
        this.userName = userName;
        /**
         * 具体来说，session对象的getId()方法返回一个唯一标识符，
         * 用于标识服务器上当前会话。当客户端发送请求时，服务器将检查请求中是否包含一个与现有会话ID匹配的会话ID。
         * 如果找到匹配的ID，则将该请求与现有会话相关联；如果没有找到，则将创建新的会话，并将该ID作为会话ID。
         * 因此，session对象的getId()方法是获取会话ID的一种方式，它在实现会话跟踪机制时非常重要。
         * 通过会话ID，服务器可以识别客户端请求所属的会话，并从会话对象中获取相关的数据。
         */
        webSocketServers.add(this);
        mapSocket.put(session.getId(),session);
        System.out.println(userName+"加入！当前在线人数为" + webSocketServers.size());
        this.session.getAsyncRemote().sendText("恭喜您成功连接上WebSocket-->当前在线人数为："+
                webSocketServers.size()+"您的sessionId:"+session.getId());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userName")String userName){
        //关闭链接时，从webSocketServers中删除
        webSocketServers.remove(this);
        System.out.println(userName+"连接关闭！当前在线人数为" + webSocketServers.size());
    }

    /**
     * 发生错误时调用
     *
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发送错误");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     * @param userName
     */
    @OnMessage
    public void onMessage(String message,Session session,@PathParam("userName")String userName){
        System.out.println("来自"+userName+"的："+message);
        /**
         *  从客户端传过来的数据是json数据，所以这里使用jackson进行转换为SocketMsg对象，
         *  然后通过socketMsg的type进行判断是单聊还是群聊，进行相应的处理
         *  ObjectMapper类是Jackson库的主要类。它称为ObjectMapper的原因是因为
         *  它将JSON映射到Java对象（反序列化），或将Java对象映射到JSON（序列化）
         */
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsg socketMsg;
        try{
            socketMsg = objectMapper.readValue(message,SocketMsg.class);
            if (socketMsg.getMsgType()==1){
                //单聊  需要发送者、接受者信息
                //fromUser存在于当前Session
                socketMsg.setFromUser(session.getId());
                Session fromUser = mapSocket.get(socketMsg.getFromUser());
                Session toUser = mapSocket.get(socketMsg.getToUser());
                //发送给接收者
                if (toUser!=null){
                    toUser.getAsyncRemote().sendText(userName+": "+socketMsg.getMsg());
                }
                else{
                    //该接收者不存在，反馈信息给发送者
                    fromUser.getAsyncRemote().sendText("系统信息：您输入的频道号不存在！");
                }
            }
            else{
                //群发消息
                broadcast(userName+":"+socketMsg.getMsg());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    /**
     * 群发自定义消息
     */
    public  void broadcast(String message){
        for (WebSocketServer item : webSocketServers) {
            //同步异步说明参考：http://blog.csdn.net/who_is_xiaoming/article/details/53287691
            //this.session.getBasicRemote().sendText(message);
            //异步发送消息.
            item.session.getAsyncRemote().sendText(message);
        }
    }

}
