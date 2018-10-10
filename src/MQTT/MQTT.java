/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MQTT;

import db.AppointmentDB;
import db.FavouriteDB;
import db.FeedbackDB;
import db.LodgingDB;
import db.MessageDB;
import db.PrivateChatDB;
import db.UserDB;
import domain.Favourite;
import domain.Lodging;
import domain.Message;
import domain.User;
import domain.Feedback;
import domain.PrivateChat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import service.SendEmail;

public class MQTT {

    MqttClient client;
    String topic = "MY/TARUC/LSS/000000001/PUB";
    int qos = 1;
    String broker = "tcp://test.mosquitto.org:1883";
    String clientId = "serverLSSserver";
    MemoryPersistence persistence;
    Converter c = new Converter();
    String ip = "192.168.42.129";

    public MQTT() {
        persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(1000 * 60 * 60 * 24);
            client.connect(connOpts);
            if (client.isConnected()) {
                System.out.println("Success");
                Subscribe();
            } else {
                System.out.println("Failed");
            }

        } catch (MqttException e) {
            System.out.println("Exception");
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connection Lost");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                
                try {
                    Converter c = new Converter();
                    String[] datas = mqttMessage.toString().split("/");
                    String command = c.ToString(datas[0]);
                    String receiverClientId = c.ToString(datas[3]);
                    
                    if (receiverClientId.equals(clientId)) {
                        System.out.println("Receive");
                        if (command.equals("004801")) {
                            CreateNewUser(mqttMessage.toString());
                        } else if (command.equals("004802")) {
                            ForgetPassword(mqttMessage.toString());
                        } else if (command.equals("004803")) {
                            Login(mqttMessage.toString());
                        } else if (command.equals("004804")) {
                            GetUser(mqttMessage.toString());
                        } else if (command.equals("004805")) {
                            UpdateUser(mqttMessage.toString());
                        } else if (command.equals("004806")) {
                            UpdatePassword(mqttMessage.toString());
                        } else if (command.equals("004807")) {
                            GetAllLodging(mqttMessage.toString());
                        } else if (command.equals("004808")) {
                            AddLodging(mqttMessage.toString());
                        } else if (command.equals("004809")) {
                            GetLodging(mqttMessage.toString());
                        } else if (command.equals("004810")) {
                            GetUserLodging(mqttMessage.toString());
                        } else if (command.equals("004811")) {
                            UpdateLodging(mqttMessage.toString());
                        } else if (command.equals("004812")) {
                            GetAllMessage(mqttMessage.toString());
                        } else if (command.equals("004813")) {
                            AddMessage(mqttMessage.toString());
                        } else if (command.equals("004814")) {
                            GetMessage(mqttMessage.toString());
                        } else if (command.equals("004815")) {
                            DeleteMessage(mqttMessage.toString());
                        } else if (command.equals("004816")) {
                            UpdateMessage(mqttMessage.toString());
                        } else if (command.equals("004817")) {
                            GetSomeLodging(mqttMessage.toString());
                        } else if (command.equals("004818")) {
                            AddFavourite(mqttMessage.toString());
                        } else if (command.equals("004819")) {
                            GetUserFavourite(mqttMessage.toString());
                        } else if (command.equals("004820")) {
                            RemoveFavourite(mqttMessage.toString());
                        } else if (command.equals("004821")) {
                            UpdateLodgingMessage(mqttMessage.toString());
                        } else if (command.equals("004822")) {
                            NewFeedback(mqttMessage.toString());
                        }
                        
                        if(command.equals("004833")){ 
                            AddPrivateMessage(mqttMessage.toString());
                        }else if(command.equals("004835")){
                            GetAllPrivateMessage(mqttMessage.toString());
                        }else if(command.equals("004837")){
                            DeletePrivateChat(mqttMessage.toString());
                        }else if(command.equals("004823")){
                            CreateNewAppointment(mqttMessage.toString());
                        }else if(command.equals("004839")){
                            GetNewAppointmentID(mqttMessage.toString());
                        }
                    } else {
                        System.out.println("Not belong to server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

         
            
            
        });
    }

    public void Subscribe() {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Publish(String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void AddPrivateMessage(String message) throws Exception{
        String tempDatas[] = c.convertToString(message);//source
        String datas[] = new String[9];//destination
        System.arraycopy(tempDatas, 0, datas, 0, tempDatas.length);
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        PrivateChatDB db = new PrivateChatDB();
        datas[8] = db.getNewID();
        String content = datas[4];
        String sentTime = datas[5];
        String sender = datas[6];
        String receiver = datas[7];

        Message msg = new PrivateChat(datas[8] , content, sentTime, sender, receiver);
        if(db.CreateMessage((PrivateChat)msg)){
            datas[2] = senderClientId;
            datas[3] = receiverClientId;
            String payload = c.convertToHex(datas);
            Publish(payload);
          
        }else{
            datas[4] = "RECEIVE FROM SERVER:/nMessage Cannot Be Sent...";
            datas[7] = datas[6];
            String payload = c.convertToHex(new String[]{});
            Publish(payload);
        }
    }

    public  void GetAllPrivateMessage(String message) throws Exception{
        
        String datas[] = c.convertToString(message.toString());
        String senderId = datas[4];
        String receiverId = datas[5];
        
        String senderClientID = datas[2];
        String receiverClientID = datas[3];
        senderClientID = datas[3];
        receiverClientID = datas[2];
        PrivateChatDB db = new PrivateChatDB();
        List<Message> list = db.GetAllMessage(senderId, receiverId);
        String tempValue = "";
        for(Message tempM: list){
            PrivateChat chat = (PrivateChat)tempM;
            tempValue = tempValue + chat.getHexResultWithSlash()+ "\\$";
        }
        
        String payload = "";
        if(!list.isEmpty()){
             payload = c.ToHex(datas[0])+"/"+c.ToHex(datas[1])+"/"+c.ToHex(senderClientID)+"/"+c.ToHex(receiverClientID)+"/"+c.ToHex(datas[4])+"\\$"+ tempValue.substring(0, tempValue.length()-2);
            Publish(payload);
        } else {
            payload = c.ToHex(datas[0]) + "/" + c.ToHex(datas[1]) + "/" + c.ToHex(senderClientID) + "/" + c.ToHex(receiverClientID) + "/" + c.ToHex(datas[4]) + "/" + c.ToHex("NoPrivateMessageFound");
            Publish(payload);
        }
        
    }
    
    public void DeletePrivateChat(String message) throws Exception{
            String datas[] = c.convertToString(message);
            String status = datas[4];
            List<String> list = new ArrayList<>();
            for(int index = 5; index < datas.length; index++){
                list.add(datas[index]);
            }
            String[] id = list.toArray(new String[list.size()]);
           String payload = "";
           PrivateChatDB db = new PrivateChatDB();
           if(db.UpdateMessage(status, id)){
               payload = c.convertToHex(new String[]{datas[0], datas[1], datas[3],datas[2], c.ToHex("Deleted")});
           }else{
               payload = c.convertToHex(new String[]{datas[0], datas[1], datas[3],datas[2], c.ToHex("DeleteFailed")});
           }
           
           Publish(payload);
           
    }
    
    private void GetNewAppointmentID(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String sender = data[3];
        String receiver = data[2];
        
        AppointmentDB db = new AppointmentDB();
        String payload = c.convertToHex(new String[]{command, reserve, sender, receiver,db.NewAppointmentID()});
        Publish(payload);
        
    }
    
    public void CreateNewAppointment(String message){
        String[] tempDatas = c.convertToString(message);
    }
    
    public void CreateNewUser(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String name = c.ToString(datas[5]);
        String icNo = c.ToString(datas[6]);
        String contactNo = c.ToString(datas[7]);
        String email = c.ToString(datas[8]);
        String password = c.ToString(datas[9]);
        String userType = c.ToString(datas[10]);
        String image = c.ToString(datas[11]);
        byte[] decoded = Base64.getDecoder().decode(image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decoded));
        String payload = "";
        UserDB udb = new UserDB();
        User u = new User();

        if (udb.UserIDValidation(userId)) {
            u.setUserId(userId);
            u.setName(name);
            u.setIcNo(icNo);
            u.setContactNo(contactNo);
            u.setEmail(email);
            u.setPassword(password);
            u.setUserType(userType);
            u.setImage("http://" + ip + "/img/User/" + userId + ".jpg");
            if (udb.CreateNewUser(u)) {
                File path = new File("C:\\xampp\\htdocs\\img\\User\\" + userId + ".jpg");
                ImageIO.write(img, "jpg", path);
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
                Publish(payload);
            } else {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
                Publish(payload);
            }
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("2");
            Publish(payload);
        }
    }

    public void ForgetPassword(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String payload = "";
        UserDB udb = new UserDB();
        User u = new User();
        SendEmail se = new SendEmail();
        String newPass = random();

        if (!udb.UserIDValidation(userId)) {
            u = udb.GetUser(userId);
            u.setPassword(newPass);
            if (udb.UpdateUser(u)) {
                if (se.SendNewPassword(u.getEmail(), newPass)) {
                    payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
                    Publish(payload);
                }
            } else {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
                Publish(payload);
            }
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("2");
            Publish(payload);
        }
    }

    public String random() {
        String temp = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(temp.charAt(rnd.nextInt(temp.length())));
        }
        return sb.toString();
    }

    public void Login(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String password = c.ToString(datas[5]);
        String payload = "";
        UserDB udb = new UserDB();
        User u = new User();

        if (!udb.UserIDValidation(userId)) {
            u = udb.GetUser(userId);
            if (u.getPassword().equals(password)) {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0") + "/"
                        + c.ToHex(userId) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getImage());
                Publish(payload);
            } else {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
                Publish(payload);
            }
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("2");
            Publish(payload);
        }

    }

    public void GetUser(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String payload = "";
        UserDB udb = new UserDB();
        User u = udb.GetUser(userId);

        if (u != null) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0") + "/"
                    + c.ToHex(u.getUserId()) + "/" + c.ToHex(u.getName()) + "/" + c.ToHex(u.getIcNo()) + "/"
                    + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getPassword()) + "/"
                    + c.ToHex(u.getUserType()) + "/" + c.ToHex(u.getImage());
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }
    }

    public void UpdateUser(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String name = c.ToString(datas[5]);
        String icNo = c.ToString(datas[6]);
        String contactNo = c.ToString(datas[7]);
        String email = c.ToString(datas[8]);
        String userType = c.ToString(datas[9]);
        String image = c.ToString(datas[10]);
        byte[] decoded = Base64.getDecoder().decode(image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decoded));
        String payload = "";
        UserDB udb = new UserDB();
        User u = udb.GetUser(userId);
        u.setUserId(userId);
        u.setName(name);
        u.setIcNo(icNo);
        u.setContactNo(contactNo);
        u.setEmail(email);
        u.setUserType(userType);

        if (udb.UpdateUser(u)) {
            File file = new File("C:\\xampp\\htdocs\\img\\User\\" + userId + ".jpg");
            file.delete();
            File path = new File("C:\\xampp\\htdocs\\img\\User\\" + userId + ".jpg");
            ImageIO.write(img, "jpg", path);
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }
    }

    public void UpdatePassword(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String oldPassword = c.ToString(datas[5]);
        String newPassword = c.ToString(datas[6]);
        String payload = "";
        UserDB udb = new UserDB();
        User u = udb.GetUser(userId);

        if (u.getPassword().equals(oldPassword)) {
            u.setPassword(newPassword);
            if (udb.UpdateUser(u)) {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
                Publish(payload);
            } else {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
                Publish(payload);
            }
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("2");
            Publish(payload);
        }

    }

    public void GetAllLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = "";
        LodgingDB ldb = new LodgingDB();
        ArrayList<Lodging> la = ldb.GetAllLodging();

        if (!la.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(la.size() + "");
            for (int i = 0; i < la.size(); i++) {
                payload += "$" + c.ToHex(la.get(i).getLodgingId()) + "/" + c.ToHex(la.get(i).getTitle()) + "/"
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(la.get(i).getExpireDate()) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage());

            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }
    }

    public void AddLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String title = c.ToString(datas[4]);
        String address = c.ToString(datas[5]);
        String price = c.ToString(datas[6]);
        String facility = c.ToString(datas[7]);
        String lodgingType = c.ToString(datas[8]);
        String description = c.ToString(datas[9]);
        String expireDate = c.ToString(datas[10]);
        String userId = c.ToString(datas[11]);
        String image = c.ToString(datas[12]);
        byte[] decoded = Base64.getDecoder().decode(image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decoded));
        String payload = "";

        LodgingDB ldb = new LodgingDB();
        Lodging l = new Lodging();
        l.setLodgingId(ldb.NewLodgingId());
        l.setTitle(title);
        l.setAddress(address);
        l.setPrice(Double.parseDouble(price));
        l.setFacility(facility);
        l.setLodgingType(lodgingType);
        l.setDescription(description);
        l.setExpireDate(expireDate);
        l.setUserId(userId);
        l.setImage("http://" + ip + "/img/Lodging/" + l.getLodgingId() + ".jpg");

        if (ldb.CreateLodging(l)) {
            File path = new File("C:\\xampp\\htdocs\\img\\Lodging\\" + l.getLodgingId() + ".jpg");
            ImageIO.write(img, "jpg", path);
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void GetLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String payload = "";

        LodgingDB ldb = new LodgingDB();
        Lodging l = ldb.GetLodging(lodgingId);
        UserDB udb = new UserDB();
        User u = udb.GetUser(l.getUserId());
        if (l != null) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0") + "/"
                    + c.ToHex(l.getTitle()) + "/" + c.ToHex(l.getAddress()) + "/" + c.ToHex(l.getPrice() + "") + "/"
                    + c.ToHex(l.getFacility()) + "/" + c.ToHex(l.getLodgingType()) + "/"
                    + c.ToHex(l.getDescription()) + "/" + c.ToHex(l.getExpireDate()) + "/" + c.ToHex(u.getName()) + "/"
                    + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(l.getLodgingId()) + "/"
                    + c.ToHex(l.getImage()) + "/" + c.ToHex(l.getUserId());
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void GetUserLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String payload = "";
        LodgingDB ldb = new LodgingDB();
        ArrayList<Lodging> la = ldb.GetUserLodging(userId);

        if (!la.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(la.size() + "");
            for (int i = 0; i < la.size(); i++) {
                payload += "$" + c.ToHex(la.get(i).getLodgingId()) + "/" + c.ToHex(la.get(i).getTitle()) + "/"
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(la.get(i).getExpireDate()) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage()) + "/"
                        + c.ToHex(la.get(i).getMessage() + "");
            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }
    }

    public void UpdateLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String title = c.ToString(datas[5]);
        String address = c.ToString(datas[6]);
        String price = c.ToString(datas[7]);
        String facility = c.ToString(datas[8]);
        String lodgingType = c.ToString(datas[9]);
        String description = c.ToString(datas[10]);
        String expireDate = c.ToString(datas[11]);
        String image = c.ToString(datas[12]);
        byte[] decoded = Base64.getDecoder().decode(image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decoded));
        String payload = "";
        LodgingDB ldb = new LodgingDB();
        Lodging l = new Lodging();
        l.setLodgingId(lodgingId);
        l.setTitle(title);
        l.setAddress(address);
        l.setPrice(Double.parseDouble(price));
        l.setFacility(facility);
        l.setLodgingType(lodgingType);
        l.setDescription(description);
        l.setExpireDate(expireDate);

        if (ldb.UpdateLodging(l)) {
            File file = new File("C:\\xampp\\htdocs\\img\\Lodging\\" + l.getLodgingId() + ".jpg");
            file.delete();
            File path = new File("C:\\xampp\\htdocs\\img\\Lodging\\" + l.getLodgingId() + ".jpg");
            ImageIO.write(img, "jpg", path);
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void GetAllMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String payload = "";
        MessageDB mdb = new MessageDB();
        ArrayList<Message> ma = mdb.GetAllMessage(lodgingId);
        UserDB udb = new UserDB();
        LodgingDB ldb = new LodgingDB();
        Lodging l = ldb.GetLodging(lodgingId);

        if (!ma.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(ma.size() + "");
            for (int i = 0; i < ma.size(); i++) {
                User u = udb.GetUser(ma.get(i).getUserId());
                payload += "$" + c.ToHex(ma.get(i).getMessageId()) + "/" + c.ToHex(ma.get(i).getContent()) + "/"
                        + c.ToHex(ma.get(i).getSentTime()) + "/" + c.ToHex(ma.get(i).getUserId()) + "/"
                        + c.ToHex(u.getName()) + "/" + c.ToHex(u.getImage()) + "/" + c.ToHex(l.getUserId());
            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }
    }

    public void AddMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String content = c.ToString(datas[5]);
        String sentTime = c.ToString(datas[6]);
        String userId = c.ToString(datas[7]);
        String payload = "";

        System.out.println(c.ToString(command) +","+ c.ToString(reserve) +","+ c.ToString(senderClientId) +","+ c.ToString(receiverClientId));
        
        MessageDB mdb = new MessageDB();
        Message m = new Message();
        m.setMessageId(mdb.NewMessageId());
        m.setLodgingId(lodgingId);
        m.setContent(content);
        m.setSentTime(sentTime);
        m.setUserId(userId);
        UserDB udb = new UserDB();
        User u = udb.GetUser(userId);
        LodgingDB ldb = new LodgingDB();
        Lodging l = ldb.GetLodging(lodgingId);
        int temp = l.getMessage()+1;
        
        if (mdb.CreateMessage(m)) {
            ldb.UpdateLodgingMessage(lodgingId,temp);
            payload = payload = command + "/" + reserve + "/" + senderClientId + "/" + c.ToHex(lodgingId) + "/"
                    + c.ToHex(m.getMessageId()) + "/" + c.ToHex(content) + "/"
                    + c.ToHex(m.getSentTime()) + "/" + c.ToHex(m.getUserId()) + "/"
                    + c.ToHex(u.getName()) + "/" + c.ToHex(u.getImage()) + "/" + c.ToHex(l.getUserId());
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void GetMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String messageId = c.ToString(datas[4]);
        String payload = "";

        MessageDB mdb = new MessageDB();
        Message m = mdb.GetMessage(messageId);

        if (m != null) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0") + "/"
                    + c.ToHex(m.getContent());
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void DeleteMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String messageId = c.ToString(datas[4]);
        String payload = "";

        MessageDB mdb = new MessageDB();

        if (mdb.DeleteMessage(messageId)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void UpdateMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String messageId = c.ToString(datas[4]);
        String content = c.ToString(datas[5]);
        String payload = "";

        MessageDB mdb = new MessageDB();

        if (mdb.UpdateMessage(messageId, content)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }

    }

    public void GetSomeLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String address = c.ToString(datas[4]);
        String payload = "";

        LodgingDB ldb = new LodgingDB();
        ArrayList<Lodging> la = ldb.GetSomeLodging(address);

        if (!la.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(la.size() + "");
            for (int i = 0; i < la.size(); i++) {
                payload += "$" + c.ToHex(la.get(i).getLodgingId()) + "/" + c.ToHex(la.get(i).getTitle()) + "/"
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(la.get(i).getExpireDate()) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage()) + "/"
                        + c.ToHex(la.get(i).getMessage() + "");

            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }
    }

    public void AddFavourite(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String userId = c.ToString(datas[5]);
        String payload = "";

        FavouriteDB fdb = new FavouriteDB();

        if (fdb.IsExist(lodgingId, userId)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            Favourite f = new Favourite();
            f.setFavouriteId(fdb.NewFavouriteId());
            f.setLodgingId(lodgingId);
            f.setUserId(userId);
            if (fdb.AddFavourite(f)) {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
                Publish(payload);
            } else {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("2");
                Publish(payload);
            }

        }
    }

    public void GetUserFavourite(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String payload = "";

        FavouriteDB fdb = new FavouriteDB();
        LodgingDB ldb = new LodgingDB();
        ArrayList<Favourite> fa = fdb.GetUserFavourite(userId);

        if (!fa.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(fa.size() + "");
            for (int i = 0; i < fa.size(); i++) {
                Lodging l = ldb.GetLodging(fa.get(i).getLodgingId());
                payload += "$" + c.ToHex(l.getLodgingId()) + "/" + c.ToHex(l.getTitle()) + "/"
                        + c.ToHex(l.getPrice() + "") + "/" + c.ToHex(l.getExpireDate()) + "/"
                        + c.ToHex(l.getLodgingType()) + "/" + c.ToHex(l.getImage());

            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }

    }

    public void RemoveFavourite(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        String userId = c.ToString(datas[5]);
        String payload = "";

        FavouriteDB fdb = new FavouriteDB();

        if (fdb.DeleteFavourite(lodgingId, userId)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }
    }

    public void UpdateLodgingMessage(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingId = c.ToString(datas[4]);
        
        LodgingDB ldb = new LodgingDB();
        ldb.UpdateLodgingMessage(lodgingId,0);
    }
    
    public void NewFeedback(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String email = c.ToString(datas[4]);
        String feedbackType = c.ToString(datas[5]);
        String feedback = c.ToString(datas[6]);
        String payload = "";
        
        FeedbackDB fdb = new FeedbackDB();
        Feedback f = new Feedback();
        
        f.setFeedbackId(fdb.NewFeedbackId());
        f.setEmail(email);
        f.setFeedbackType(feedbackType);
        f.setFeedback(feedback);
        
        if (fdb.AddFeedback(f)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("1");
            Publish(payload);
        }
        
    }

}