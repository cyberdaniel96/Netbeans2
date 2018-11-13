/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MQTT;

import db.AppointmentDB;
import db.CodeGeneratorDB;
import db.FavouriteDB;
import db.FeedbackDB;
import db.LeaseDB;
import db.LodgingDB;
import db.MessageDB;
import db.PrivateChatDB;
import db.TenantDB;
import db.UserDB;
import domain.Appointment;
import domain.CodeGenerator;
import domain.Favourite;
import domain.Lodging;
import domain.Message;
import domain.User;
import domain.Feedback;
import domain.Lease;
import domain.PrivateChat;
import domain.Tenant;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
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
   // String broker = "tcp://192.168.42.188:1883";
    String clientId = "serverLSSserver";
    MemoryPersistence persistence;
    Converter c = new Converter();
    String ip = "192.168.42.180";

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
            e.printStackTrace();
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
                    System.out.println(command);
                   
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

                        if (command.equals("004833")) {
                            AddPrivateMessage(mqttMessage.toString());
                        } else if (command.equals("004835")) {
                            GetAllPrivateMessage(mqttMessage.toString());
                        } else if (command.equals("004837")) {
                            DeletePrivateChat(mqttMessage.toString());
                        } else if (command.equals("004823")) {
                            CreateNewAppointment(mqttMessage.toString());
                        } else if (command.equals("004839")) {
                            GetNewAppointmentID(mqttMessage.toString());
                        } else if (command.equals("004831")) {
                            GetAllAppointments(mqttMessage.toString());
                        } else if (command.equals("004829")) {
                            CancelAppointment(mqttMessage.toString());
                        } else if (command.equals("004827")) {
                            UpdateAppointment(mqttMessage.toString());
                        }

                        //notification
                        if (command.equals("004841")) {
                            System.err.println(command);
                            CreateNotification(mqttMessage.toString());
                        } else if (command.equals("004843")) {
                            //delete notification
                        } else if (command.equals("004845")) {
                            //get all notification
                        }

                        //code generator
                        if (command.equals("004847")) {
                            CreateVerificationCode(mqttMessage.toString());
                        }

                        //chin ler
                        if (command.equals("004824")) {
                            CreateNewLease(mqttMessage.toString());
                        } else if (command.equals("004828")) {
                            GetAllLease(mqttMessage.toString());
                        } else if (command.equals("004830")) {
                            GetAllTenant(mqttMessage.toString());
                        } else if (command.equals("004832")) {
                            AddTenants(mqttMessage.toString());
                        } else if (command.equals("004836")) {
                            UpdateTenant(mqttMessage.toString());
                        }
                         
                        if(command.equals("004849")){
                            
                            GetTenant(mqttMessage.toString());
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

    public void CreateVerificationCode(String message) throws Exception {
        String[] splitDollar = message.split("\\$");
        String[] head = c.convertToString(splitDollar[0]);
        String[] body = c.convertToString(splitDollar[1]);

        String id = "";
        String issueDate = body[0];
        String issueTime = body[1];
        String randomCode = body[2];
        String user = body[3];

        CodeGeneratorDB db = new CodeGeneratorDB();
        id = db.getNewID();

        String payload = "";
        CodeGenerator cg = new CodeGenerator(id, new SimpleDateFormat("yyyy-MM-dd").parse(issueDate), new SimpleDateFormat("HH:mm:ss").parse(issueTime), Integer.parseInt(randomCode), user);
        if (db.CreateCode(cg)) {
            payload = c.convertToHex(new String[]{head[0], head[1], head[3], head[2], "Success"});
        } else {
            payload = c.convertToHex(new String[]{head[0], head[1], head[3], head[2], "Failed"});
        }

        Publish(payload);
    }
    
    public void GetTenant(String message) throws Exception {
        String splitDollar[] = message.split("\\$");
        String serverData[] = c.convertToString(splitDollar[0]);
        String data[] = c.convertToString(splitDollar[1]);

        String tenantID = data[0];
        String mycommand = data[1];

        TenantDB tdb = new TenantDB();
        LeaseDB ldb = new LeaseDB();
        LodgingDB loDB = new LodgingDB();

        ArrayList<Tenant> tList = tdb.GetTenantViaUserID(tenantID);
        ArrayList<String> item = new ArrayList<>();

        String command = serverData[0];
        String reserve = serverData[1];
        String senderClientId = serverData[3];
        String receiverClientID = serverData[2];

        if (mycommand.equals("GETLOD")) {
            if (!tList.isEmpty()) {
                for (Tenant t : tList) {
                    Lease l = ldb.GetLease(t.getLeaseID());
                    Lodging lo = loDB.GetLodging(l.getLodgingID());

                    item.add(c.convertToHex(new String[]{
                        mycommand//0
                        , l.getLeaseID()//1
                        , l.getDueDay()//2
                        , new SimpleDateFormat("dd-MM-yyyy").format(l.getIssueDate())//3
                        , l.getStatus()//4
                        , l.getLodgingID()//5
                        , lo.getImage()//6
                        , lo.getTitle()//7
                        , lo.getAddress()}));//8
                }
            }

            publishLodgingWithLease(c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, ""}), item);
        }
        if (mycommand.equals("GETTENANT")) {
            String leaseID = data[2];
            Tenant t = tdb.GetSelectedTenantWithLeaseID(tenantID,leaseID);
            System.err.println(t.getLeaseID());
            String tData = "";
            if (t != null) {
                tData = c.convertToHex(new String[]{
                    mycommand
                    , t.getLeaseID() //1
                    , t.getRoomType()//2
                    , t.getRole()//3
                    , new SimpleDateFormat("dd-MM-yyyy").format(t.getLeaseStart())//4
                    , new SimpleDateFormat("dd-MM-yyyy").format(t.getLeaseEnd())//5
                    , String.format("%f", t.getRent())//6
                    , String.format("%f", t.getDeposit())//7
                    , t.getStatus(), new SimpleDateFormat("dd-MM-yyyy").format(t.getBreakDate())//8
                    , t.getReason()//9
                    , t.getUserID()//10
                    , t.getLeaseID()//11
                });

            }
            
            Publish(c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, ""}) + "$" + tData);
        }

    }
    
    private void publishLodgingWithLease(String serverdata, ArrayList<String> list){
        String data = "";
        if(!list.isEmpty()){
            for(String temp: list){
                data += temp + "/" +c.ToHex(String.format("%d", list.size())) + "@";
            }
            System.out.println(data);
            Publish(serverdata + "$" + data.substring(0, data.length()-1));
        }
    }
 
    public void CreateNotification(String message) throws Exception {
        String splitDollar[] = message.split("\\$");
        String serverData[] = c.convertToString(splitDollar[0]);
        String notiData[] = c.convertToString(splitDollar[1]);

        String command = serverData[0];
        String reserve = serverData[1];
        String senderClientID = serverData[3];
        String receiverClientID = serverData[2];

        String head = c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, ""});
        String payload = head + "$" + splitDollar[1] + "$" + splitDollar[2];
        Publish(payload);

    }

    public void AddPrivateMessage(String message) throws Exception {
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

        Message msg = new PrivateChat(datas[8], content, sentTime, sender, receiver);
        if (db.CreateMessage((PrivateChat) msg)) {
            datas[2] = senderClientId;
            datas[3] = receiverClientId;
            String payload = c.convertToHex(datas);
            Publish(payload);

        } else {
            datas[4] = "RECEIVE FROM SERVER:/nMessage Cannot Be Sent...";
            datas[7] = datas[6];
            String payload = c.convertToHex(new String[]{});
            Publish(payload);
        }
    }

    public void GetAllPrivateMessage(String message) throws Exception {

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
        for (Message tempM : list) {
            PrivateChat chat = (PrivateChat) tempM;
            tempValue = tempValue + chat.getHexResultWithSlash() + "\\$";
        }

        String payload = "";
        if (!list.isEmpty()) {
            payload = c.ToHex(datas[0]) + "/" + c.ToHex(datas[1]) + "/" + c.ToHex(senderClientID) + "/" + c.ToHex(receiverClientID) + "/" + c.ToHex(datas[4]) + "\\$" + tempValue.substring(0, tempValue.length() - 2);
            Publish(payload);
        } else {
            payload = c.ToHex(datas[0]) + "/" + c.ToHex(datas[1]) + "/" + c.ToHex(senderClientID) + "/" + c.ToHex(receiverClientID) + "/" + c.ToHex(datas[4]) + "/" + c.ToHex("NoPrivateMessageFound");
            Publish(payload);
        }

    }

    public void DeletePrivateChat(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String senderClientID = data[3];
        String receiverClientID = data[2];
        String sender = data[4];
        String receiver = data[5];
        String role = data[6];
        String status = data[7];

        PrivateChatDB db = new PrivateChatDB();
        boolean delete = db.DeletePrivateChat(sender, receiver, role, status);
        System.out.println(delete);
        if (delete) {
            Publish(c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Success"}));
        } else {
            Publish(c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Failed"}));
        }

    }

    private void GetNewAppointmentID(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String sender = data[3];
        String receiver = data[2];

        AppointmentDB db = new AppointmentDB();
        String payload = c.convertToHex(new String[]{command, reserve, sender, receiver, db.NewAppointmentID()});
        Publish(payload);

    }

    public void CreateNewAppointment(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String senderClientId = data[3];
        String receiverClientID = data[2];

        String payload = "";
        //Appointment app = new Appointment(data[4], data[5], "NOTHINGBY" + data[11] + "ANDNOTHINGBY" + data[12], data[6], data[7], data[8], data[9], data[10], data[11], data[12]);
        Appointment app = new Appointment(data[4], data[5], "", data[6], data[7], data[8], data[9], data[10], data[11], data[12]);
        
        AppointmentDB db = new AppointmentDB();
        if (db.AddAppointment(app)) {
            payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Success"});
        } else {
            payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Fail"});
        }
        Publish(payload);

    }

    public void GetAllAppointments(String message) throws Exception {
        String[] datas = c.convertToString(message);
        String command = datas[0];
        String reserve = datas[1];
        String senderID = datas[3];
        String receiverID = datas[2];

        String tenantID = datas[4];
        AppointmentDB db = new AppointmentDB();
        List<Appointment> appList = db.GetAllAppointment(tenantID);
        String payload = c.convertToHex(new String[]{command, reserve, senderID, receiverID, Integer.toString(appList.size())}) + "";

        for (Appointment app : appList) {
            payload += "$" + c.convertToHex(new String[]{app.getAppointmentID(), app.getDateTime(), app.getReason(), app.getState(),
                app.getPriority(), app.getComment(), app.getStatus(), app.getLodgingID(), app.getTenantID(), app.getOwnerID()});
        }

        if (!appList.isEmpty()) {
            Publish(payload);
        } else {
            Publish(payload + "/" + c.ToHex("Failed"));
        }

    }

    public void CancelAppointment(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String senderClientID = data[3];
        String receiverClientID = data[2];
        String appointmentID = data[4];

        AppointmentDB db = new AppointmentDB();
        String payload = "";
        if (db.CancelAppointment(appointmentID)) {
            payload = c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Success"});
        } else {
            payload = c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Failed"});
        }
    }

    public void UpdateAppointment(String message) throws Exception {
        String data[] = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String senderClientID = data[3];
        String receiverClientID = data[2];

        Appointment app = new Appointment(data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13]);
        AppointmentDB db = new AppointmentDB();
        if (db.UpdateAppointment(app)) {
            Publish(c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Success"}));
        } else {
            Publish(c.convertToHex(new String[]{command, reserve, senderClientID, receiverClientID, "Failed"}));
        }
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

        System.out.println(c.ToString(command) + "," + c.ToString(reserve) + "," + c.ToString(senderClientId) + "," + c.ToString(receiverClientId));

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
        int temp = l.getMessage() + 1;

        if (mdb.CreateMessage(m)) {
            ldb.UpdateLodgingMessage(lodgingId, temp);
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
        ldb.UpdateLodgingMessage(lodgingId, 0);
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

    /**
     * ************
     */
    public void CreateNewLease(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String dueDay = c.ToString(datas[4]);
        String lodgingID = c.ToString(datas[5]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        Calendar cc = Calendar.getInstance();
        cc.setTime(new Date());
        String sysDate = new SimpleDateFormat().format(cc.getTime());
        Date date = new SimpleDateFormat().parse(sysDate);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        LeaseDB ldb = new LeaseDB();
        Lease l = new Lease();
        String newLeaseID = ldb.NewLeaseID();
        l.setLeaseID(newLeaseID);
        l.setDueDay(dueDay);
        l.setIssueDate(sqlDate);
        l.setStatus("Active");
        l.setLodgingID(lodgingID);

        if (ldb.CreateNewLease(l)) {
            payload += c.ToHex("0") + "/" + c.ToHex(newLeaseID);
        } else {
            payload += c.ToHex("1");
        }
        Publish(payload);
    }

    public void AddTenants(String message) throws Exception {
        String[] datas = message.split("\\$");
        String[] head = datas[0].split("/");
        String command = head[0];
        String reserve = head[1];
        String senderClientId = head[3];
        String receiverClientId = head[2];
        String leaseID = c.ToString(head[4]);
        int sizeOfTenantList = Integer.parseInt(c.ToString(head[5]));
        String payload = "";

        TenantDB tdb = new TenantDB();

        for (int i = 1; i <= sizeOfTenantList; i++) {
            String[] body = datas[i].split("/");
            Tenant t = new Tenant();
            t.setTenantID(tdb.NewTenantID());
            t.setRoomType(c.ToString(body[0]));
            t.setRole(c.ToString(body[1]));

            Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(body[2]));
            java.sql.Date sqlStrDate = new java.sql.Date(strDate.getTime());

            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(body[3]));
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

            t.setLeaseStart(sqlStrDate);
            t.setLeaseEnd(sqlEndDate);
            t.setRent(Double.parseDouble(c.ToString(body[4])));
            t.setDeposit(Double.parseDouble(c.ToString(body[5])));
            t.setUserID(c.ToString(body[6]));
            t.setLeaseID(leaseID);
            tdb.AddNewTenant(t);
        }
        payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
        Publish(payload);
    }

    public void GetAllLease(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String userId = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";
        LeaseDB ldb = new LeaseDB();
        LodgingDB lodb = new LodgingDB();
        ArrayList<Lease> leaseList = ldb.GetUserLease(userId);

        if (!leaseList.isEmpty()) {
            payload += c.ToHex(leaseList.size() + "");
            for (int i = 0; i < leaseList.size(); i++) {
                Lodging l = lodb.GetLodging(leaseList.get(i).getLodgingID());
                payload += "$" + c.ToHex(leaseList.get(i).getLeaseID()) + "/" + c.ToHex(leaseList.get(i).getDueDay()) + "/"
                        + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(leaseList.get(i).getIssueDate())) + "/" + c.ToHex(leaseList.get(i).getStatus())
                        + "/" + c.ToHex(leaseList.get(i).getLodgingID()) + "/" + c.ToHex(l.getTitle()) + "/" + c.ToHex(l.getAddress()) + "/" + c.ToHex(l.getImage());
            }
        } else {
            payload += c.ToHex("0");
        }
        Publish(payload);
    }

    public void GetAllTenant(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String leaseID = c.ToString(datas[4]);
        String payload = "";
        LeaseDB ldb = new LeaseDB();
        Lease lease = ldb.GetLease(leaseID);

        if (lease != null) {
            TenantDB tdb = new TenantDB();
            UserDB udb = new UserDB();
            ArrayList<Tenant> tenantList = tdb.GetAllTenant(leaseID);

            if (!tenantList.isEmpty()) {
                payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(tenantList.size() + "") + "/" + c.ToHex(leaseID);
                for (int i = 0; i < tenantList.size(); i++) {
                    User u = udb.GetUser(tenantList.get(i).getUserID());
                    payload += "$" + c.ToHex(tenantList.get(i).getTenantID()) + "/" + c.ToHex(tenantList.get(i).getRoomType()) + "/"
                            + c.ToHex(tenantList.get(i).getRole()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseStart())) + "/"
                            + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseEnd())) + "/" + c.ToHex(String.valueOf(tenantList.get(i).getRent())) + "/"
                            + c.ToHex(String.valueOf(tenantList.get(i).getDeposit())) + "/" + c.ToHex(tenantList.get(i).getStatus()) + "/";

                    if (tenantList.get(i).getStatus().equals("Terminated")) {
                        payload += c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getBreakDate())) + "/" + c.ToHex(tenantList.get(i).getReason());
                    }

                    payload += c.ToHex(tenantList.get(i).getUserID()) + "/" + c.ToHex(tenantList.get(i).getLeaseID()) + "/" + c.ToHex(u.getName()) + "/"
                            + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getImage());
                }
            }
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }
    }

    public void UpdateTenant(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = "";

        TenantDB tdb = new TenantDB();
        Tenant t = new Tenant();

        t.setTenantID(c.ToString(datas[4]));
        t.setRoomType(c.ToString(datas[5]));
        t.setRole(c.ToString(datas[6]));

        Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[7]));
        java.sql.Date sqlStrDate = new java.sql.Date(strDate.getTime());

        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[8]));
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        t.setLeaseStart(sqlStrDate);
        t.setLeaseEnd(sqlEndDate);
        t.setRent(Double.parseDouble(c.ToString(datas[9])));
        t.setDeposit(Double.parseDouble(c.ToString(datas[10])));
        t.setStatus(c.ToString(datas[11]));
        if (datas[11].equals("Terminated")) {
            Date breakDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[12]));
            java.sql.Date sqlBreakDate = new java.sql.Date(breakDate.getTime());
            t.setBreakDate(sqlBreakDate);
            t.setReason(c.ToString(datas[13]));
        }

        if (tdb.UpdateTenant(t)) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("Success");
            Publish(payload);
        } else {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("Failed");
            Publish(payload);
        }
    }
    /**
     * ***********
     */

}

class test{
    public static void main(String[] args) {
        
    }
}
