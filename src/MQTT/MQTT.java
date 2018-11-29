package MQTT;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import service.SendEmail;
import domain.*;
import db.*;

public class MQTT {
    MqttClient client;
    String topic = "MY/TARUC/LSS/000000001/PUB";
    int qos = 1;
    String broker = "tcp://test.mosquitto.org:1883";
    String clientId = "serverLSSserver";
    MemoryPersistence persistence;
    Converter c = new Converter();
    String ip = "172.18.23.5";

    String serverData = c.convertToHex(new String[]{
        "004841",
        "000000000000000000000000",
        "serverLSSserver",
        "android",
        " "
    });

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
                        } else if (command.equals("004823")) {
                            CreateNewAppointment(mqttMessage.toString());
                        } else if (command.equals("004827")) {
                            UpdateAppointment(mqttMessage.toString());
                        } else if (command.equals("004829")) {
                            CancelAppointment(mqttMessage.toString());
                        } else if (command.equals("004831")) {
                            GetAllAppointments(mqttMessage.toString());
                        } else if (command.equals("004833")) {
                            AddPrivateMessage(mqttMessage.toString());
                        } else if (command.equals("004835")) {
                            GetAllPrivateMessage(mqttMessage.toString());
                        } else if (command.equals("004837")) {
                            DeletePrivateChat(mqttMessage.toString());
                        }  else if (command.equals("004839")) {
                            GetNewAppointmentID(mqttMessage.toString());
                        } else if (command.equals("004841")) {
                            CreateNotification(mqttMessage.toString());  //notification
                        } else if (command.equals("004847")) {
                            CreateVerificationCode(mqttMessage.toString());
                        } else if (command.equals("004849")) {
                            GetTenant(mqttMessage.toString());
                        } else if (command.equals("004851")) {
                            UpdateTenantStatus(mqttMessage.toString());
                        } else if(command.equals("004853")){
                            GetRentalDetails(mqttMessage.toString());
                        } else if(command.equals("004855")){
                            UpdateTenantReceipt(mqttMessage.toString());
                        } else if (command.equals("004824")) {
                            CreateNewLease(mqttMessage.toString());
                        } else if (command.equals("004826")) {
                            VerifyTenant(mqttMessage.toString());
                        } else if (command.equals("004828")) {
                            GetAllLease(mqttMessage.toString());
                        } else if (command.equals("004830")) {
                            GetAllTenant(mqttMessage.toString());
                        } else if (command.equals("004832")) {
                            AddTenants(mqttMessage.toString());
                        } else if (command.equals("004834")) {
                            UpdateLease(mqttMessage.toString());
                        } else if (command.equals("004836")) {
                            UpdateTenant(mqttMessage.toString());
                        } else if (command.equals("004838")) {
                            CreateNewRental(mqttMessage.toString());
                        } else if (command.equals("004840")) {
                            GetAllRental(mqttMessage.toString());
                        } else if (command.equals("004842")) {
                            GetRental(mqttMessage.toString());
                        } else if (command.equals("004844")) {
                            GetAllReceipt(mqttMessage.toString());
                        } else if (command.equals("004846")) {
                            UpdateExpense(mqttMessage.toString());
                        } else if (command.equals("004848")) {
                            UpdateReceipt(mqttMessage.toString());
                        } else if (command.equals("004850")) {
                            AddVisitTime(mqttMessage.toString());
                        } else if (command.equals("004852")) {
                            GetAllVisitTime(mqttMessage.toString());
                        } else if (command.equals("004854")) {
                            GetVisitTime(mqttMessage.toString());
                        } else if (command.equals("004856")) {
                            UpdateVisitTime(mqttMessage.toString());
                        } else if (command.equals("004858")) {
                            RenewLodging(mqttMessage.toString());
                        } else if (command.equals("004860")) {
                            RemoveLodging(mqttMessage.toString());
                        } else if (command.equals("004862")) {
                            AddExpense(mqttMessage.toString());
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
        
        ldb.IsExpiryCheck();
        
        if (!la.isEmpty()) {
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex(la.size() + "");
            for (int i = 0; i < la.size(); i++) {
                payload += "$" + c.ToHex(la.get(i).getLodgingId()) + "/" + c.ToHex(la.get(i).getTitle()) + "/"
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(la.get(i).getExpireDate())) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage()) + "/"
                        + c.ToHex(la.get(i).getStatus());

            }
            Publish(payload);
        } else {
            
            payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
            Publish(payload);
        }

        String notifyData = c.convertToHex(new String[]{"Lodging Service System", "Rental is going to reach the due date. Please make payment.", "RENTAL EXPIRED", ""});
        String resource = c.ToHex("leaseID") + "@" + c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
        
        notifyData = c.convertToHex(new String[] {"Lodging Service System", "Lease is going to end.", "LEASE EXPIRED", ""});
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
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
        Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse(expireDate);
        java.sql.Date sqlStrDate = new java.sql.Date(strDate.getTime());
        l.setExpireDate(sqlStrDate);
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
                    + c.ToHex(l.getDescription()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(l.getExpireDate())) + "/" + c.ToHex(u.getName()) + "/"
                    + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(l.getLodgingId()) + "/"
                    + c.ToHex(l.getImage()) + "/" + c.ToHex(l.getStatus()) + "/" + c.ToHex(l.getUserId());

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
                LeaseDB db = new LeaseDB();
                int leaseCount = db.GetLeaseCount(la.get(i).getLodgingId());
                payload += "$" + c.ToHex(la.get(i).getLodgingId()) + "/" + c.ToHex(la.get(i).getTitle()) + "/"
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(la.get(i).getExpireDate())) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage()) + "/"
                        + c.ToHex(la.get(i).getMessage() + "") + "/" + c.ToHex(la.get(i).getAddress()) + "/"
                        + c.ToHex(la.get(i).getStatus()) + "/" + c.ToHex(leaseCount + "");
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
        String status = c.ToString(datas[13]);
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
        Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse(expireDate);
        java.sql.Date sqlStrDate = new java.sql.Date(strDate.getTime());
        l.setExpireDate(sqlStrDate);
        l.setStatus(status);

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
                        + c.ToHex(la.get(i).getPrice() + "") + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(la.get(i).getExpireDate())) + "/"
                        + c.ToHex(la.get(i).getLodgingType()) + "/" + c.ToHex(la.get(i).getImage()) + "/"
                        + c.ToHex(la.get(i).getMessage() + "") + "/" + c.ToHex(la.get(i).getStatus());

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
                        + c.ToHex(l.getPrice() + "") + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(l.getExpireDate())) + "/"
                        + c.ToHex(l.getLodgingType()) + "/" + c.ToHex(l.getImage()) + "/" + c.ToHex(l.getStatus());
              
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

    public void CreateNewAppointment(String message) throws Exception {
        String[] data = c.convertToString(message);
        String command = data[0];
        String reserve = data[1];
        String senderClientId = data[3];
        String receiverClientID = data[2];

        String payload = "";
        Appointment app = new Appointment(data[4], data[5], "", data[6], data[7], data[8], data[9], data[10], data[11], data[12]);

        AppointmentDB db = new AppointmentDB();
        if (db.AddAppointment(app)) {
            payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Success"});
        } else {
            payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Fail"});
        }
        Publish(payload);

        String notificationData = c.convertToHex(new String[]{"Lodging Service System",
            app.getTenantID() + " make an appointment with you.",
            "APPOINTMENT CREATED",
            app.getOwnerID()});

        String resourcesData = c.ToHex("Hello") + "@" + c.ToHex("World");

        String sendNotification = serverData + "$" + notificationData + "$" + resourcesData;
        CreateNotification(sendNotification);

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
        
        if (app.getStatus().equals("accept")) {
            String notificationData = c.convertToHex(new String[]{"Lodging Service Sytem",
                app.getOwnerID() + " accept your appointment.",
                "APPOINTMENT ACCEPTED",
                app.getTenantID()});
           
            String resourcesData = c.ToHex("Appointment") + "@" + c.ToHex("Resouces");

            String servicePayload = serverData + "$" + notificationData + "$" + resourcesData;

            CreateNotification(servicePayload);
        } else if (app.getStatus().equals("reject")) {
            String notificationData = c.convertToHex(new String[]{"Lodging Service Sytem",
                app.getOwnerID() + " reject your appointment.",
                "APPOINTMENT REJECTED",
                app.getTenantID()});

            String resourcesData = c.ToHex("Appointment") + "@" + c.ToHex("Resouces");

            String servicePayload = serverData + "$" + notificationData + "$" + resourcesData;

            CreateNotification(servicePayload);

        } else {
            String notificationData = c.convertToHex(new String[]{"Lodging Service Sytem",
                data[2].substring(0, data[2].length() - 1) + " updated the appointment",
                "APPOINTMENT UPDATED",
                app.getOwnerID()});

            String resourcesData = c.ToHex("Appointment") + "@" + c.ToHex("Resouces");

            String servicePayload = serverData + "$" + notificationData + "$" + resourcesData;

            CreateNotification(servicePayload);

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

        String notificationData = c.convertToHex(new String[]{"Lodging Service Sytem",
            data[2].substring(0, data[2].length() - 1) + " cancelled the appointment",
            "APPOINTMENT CANCEL",
            data[5]});

        String resourcesData = c.ToHex("Appointment") + "@" + c.ToHex("Resouces");

        String servicePayload = serverData + "$" + notificationData + "$" + resourcesData;

        CreateNotification(servicePayload);
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

        String notificationData = c.convertToHex(new String[]{"Lodging Service System",
            "You got a new message from " + ((PrivateChat) msg).getSenderID(),
            "PRIVATECHAT RECEIVED",
            ((PrivateChat) msg).getReceiverID()});

        String resourcesData = c.ToHex("Hello") + "@" + c.ToHex("world");

        String sendNotification = serverData + "$" + notificationData + "$" + resourcesData;
        CreateNotification(sendNotification);
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
    
    public void CreateNotification(String message) throws Exception {
        String splitDollar[] = message.split("\\$");
        String notiData[] = c.convertToString(splitDollar[1]);

        if (notiData[2].equals("LEASE ACCEPTED")) {  //tenant,owner receive
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(notiData[3]);
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());

            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], lo.getUserId()});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("LEASE REJECTED")) {  //tenant,owner receive
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(notiData[3]);
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());

            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], lo.getUserId()});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("LEASE TERMINATED")) { //owner,tenant receive
            TenantDB tdb = new TenantDB();
            Tenant t = tdb.GetSelectedTenant(notiData[3]);
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(t.getLeaseID());
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());

            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1] + lo.getTitle() + "]", notiData[2], t.getUserID()});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if(notiData[2].equals("LEASE EXPIRED")){
            LeaseDB ldb = new LeaseDB();
            ArrayList<Tenant> tenantList = ldb.isLeaseExpiry();
            
            if(tenantList.size()>0){
                for(Tenant t:tenantList){
                    Lease l = ldb.GetLease(t.getLeaseID());
                    LodgingDB lodb = new LodgingDB();
                    Lodging lo = lodb.GetLodging(l.getLodgingID());
                    UserDB udb = new UserDB();
                    User u = udb.GetUser(t.getUserID());
                    String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1]+"["+lo.getTitle()+"]", notiData[2], t.getUserID()}); //tenant
                    Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
                    notifyData = c.convertToHex(new String[]{notiData[0],
                        u.getName()+"'s lease is going to end on "+new SimpleDateFormat("dd-MM-yyyy").format(t.getLeaseEnd())+". ["+lo.getTitle()+"]",
                        notiData[2], lo.getUserId()}); //owner
                    Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
                }               
            }                
            return;
        }

        if (notiData[2].equals("RENTAL UPLOADED")) { //tenant receive
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(notiData[3]);
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());
            TenantDB tdb = new TenantDB();
            List<Tenant> tenantList = tdb.GetActiveTenant(notiData[3]);

            for (int i = 0; i < tenantList.size(); i++) {
                String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1] + lo.getTitle() + "]", notiData[2], tenantList.get(i).getUserID()});
                Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            }
            return;
        }

        if (notiData[2].equals("RENTAL ACCEPTED") || notiData[2].equals("RENTAL REJECTED")) { //tenant receive          
            TenantDB tdb = new TenantDB();
            Tenant t = tdb.GetSelectedTenant(notiData[3]);
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(t.getLeaseID());
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());

            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1] + lo.getTitle() + "]", notiData[2], t.getUserID()});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("APPOINTMENT ACCEPTED")) { //tenant receie              
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("APPOINTMENT REJECTED")) { //owner receive              
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }
        if (notiData[2].equals("APPOINTMENT CREATED")) { //owner receive              
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("APPOINTMENT UPDATED")) { //owner receive              
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }

        if (notiData[2].equals("APPOINTMENT CANCEL")) { //owner receive              
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }
        if (notiData[2].equals("RENTAL EXPIRED")) { //tenant receive 
            RentalDB rdb = new RentalDB();
            ArrayList<Receipt> receiptList = rdb.isRentalExipy();

            if (receiptList.size() > 0) {
                for (Receipt r : receiptList) {
                    TenantDB tdb = new TenantDB();
                    Tenant t = tdb.GetSelectedTenant(r.getTenantID());
                    String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], t.getUserID()});
                    Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
                }
            }
            return;
        }

        if (notiData[2].equals("PRIVATECHAT RECEIVED")) { //owner/tenant receive
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }
        
        if (notiData[2].equals("VERIFICATION RECEIVED")) { //owner/tenant receive
            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1], notiData[2], notiData[3]});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);
            return;
        }    
        
       
        if(notiData[2].equals("RENTAL EDITED")){ //send to owner
            RentalDB rdb = new RentalDB();
            Rental r = rdb.GetRental(notiData[3]);
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(r.getLeaseID());
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());
            TenantDB tdb = new TenantDB();
           
            List<Tenant> tenantList = tdb.GetActiveTenant(r.getLeaseID());
          
            if(tenantList.size()>0){
                for(int i = 0; i < tenantList.size(); i++){
                    String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1]+lo.getTitle(), notiData[2], tenantList.get(i).getUserID()});
                    Publish(serverData + "$" + notifyData + "$" + splitDollar[2]); 
                }     
            }     
            return;
        } 
        
        if(notiData[2].equals("RENTAL RECEIVED")){ //send to owner
            ReceiptDB rcdb = new ReceiptDB();
            Receipt rc = rcdb.GetSelectedReceipt(notiData[3]);
            TenantDB tdb = new TenantDB();
            Tenant t = tdb.GetSelectedTenant(rc.getTenantID());
            LeaseDB ldb = new LeaseDB();
            Lease l = ldb.GetLease(t.getLeaseID());
            UserDB udb = new UserDB();
            User tenant = udb.GetUser(t.getUserID());
            LodgingDB lodb = new LodgingDB();
            Lodging lo = lodb.GetLodging(l.getLodgingID());

            String notifyData = c.convertToHex(new String[]{notiData[0], notiData[1]+tenant.getName()+". ["+lo.getTitle()+"]", notiData[2], lo.getUserId()});
            Publish(serverData + "$" + notifyData + "$" + splitDollar[2]);     
            return;
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

        String notificationData = c.convertToHex(new String[]{"Lodging Service System",
            "Your verification code is " + randomCode,
            "VERIFICATION RECEIVED",
            body[4]});

        String resourcesData = c.ToHex("Hello") + "@" + c.ToHex("world");

        String sendNotification = serverData + "$" + notificationData + "$" + resourcesData;
        
        CreateNotification(sendNotification);

    }
    
    private void publishLodgingWithLease(String serverdata, ArrayList<String> list) {
        String data = "";
        if (!list.isEmpty()) {
            for (String temp : list) {
                data += temp + "/" + c.ToHex(String.format("%d", list.size())) + "@";
            }
            System.out.println(data);
            Publish(serverdata + "$" + data.substring(0, data.length() - 1));
            return;
        }
        
 
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
                        mycommand
                        , l.getLeaseID()
                        , l.getDueDay()
                        , new SimpleDateFormat("dd-MM-yyyy").format(l.getIssueDate())
                        , l.getStatus()
                        , l.getLodgingID()
                        , lo.getImage()
                        , lo.getTitle()
                        , lo.getAddress()}));
                }
            }

            publishLodgingWithLease(c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, ""}), item);
        }
        if (mycommand.equals("GETTENANT")) {
            String leaseID = data[2];
            Tenant t = tdb.GetSelectedTenantWithLeaseID(tenantID, leaseID);

            String tData = "";
            if (t != null) {
                tData = c.convertToHex(new String[]{
                    mycommand, t.getTenantID()
                    , t.getRoomType()
                    , t.getRole()
                    , new SimpleDateFormat("dd-MM-yyyy").format(t.getLeaseStart())
                    , new SimpleDateFormat("dd-MM-yyyy").format(t.getLeaseEnd())
                    , String.format("%f", t.getRent())
                    , String.format("%f", t.getDeposit())
                    , t.getStatus(), new SimpleDateFormat("dd-MM-yyyy").format(t.getBreakDate())
                    , ""
                    , t.getUserID()
                    , t.getLeaseID()
                });

            }
            Publish(c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, ""}) + "$" + tData);
        }
    }

    private void UpdateTenantStatus(String message) throws Exception {
        String[] data = message.split("\\$");
        String[] head = c.convertToString(data[0]);
        String[] tail = c.convertToString(data[1]);

        TenantDB db = new TenantDB();
        Tenant t = new Tenant();
        t.setStatus(tail[0]);
        t.setReason(tail[1]);
        t.setTenantID(tail[2]);

        db.UpdateTenantStatus(t);

    }
    
    private void GetRentalDetails(String message) throws Exception{
        String data[] = message.split("\\$");
        String server[] = c.convertToString(data[0]);
        
        String command = server[0];
        String reserve = server[1];
        String senderClientId = server[3];
        String receiverClientID = server[2];
        
       String tail[] = c.convertToString(data[1]);
       String id = tail[0];
       String mycommand = tail[1];
        
        TenantDB tdb = new TenantDB();
        LeaseDB ldb = new LeaseDB();
        LodgingDB lodb = new LodgingDB();
        RentalDB rdb = new RentalDB();
        ReceiptDB redb = new ReceiptDB();
        ExpenseDB edb = new ExpenseDB();
        
        if(mycommand.equals("LODGING")){
            ArrayList<Tenant> tList = tdb.GetAllTenants(id);
            ArrayList<Lodging> loList = new ArrayList<>();
            ArrayList<Lease> lList = new ArrayList<>();
            ArrayList<String> strLeaseID = new ArrayList<>();
            
            
            
            for(Tenant t: tList){
                Lease lease = ldb.GetLease(t.getLeaseID());
                lList.add(lease);
                strLeaseID.add(t.getLeaseID());
                
            }
            
            for(Lease l: lList){
                Lodging lodging = lodb.GetLodging(l.getLodgingID());
                loList.add(lodging);
            }

            String payload = "";
            payload = c.convertToHex(new String[]{command,reserve, senderClientId, receiverClientID, loList.size()+"",mycommand, ""});
            int count = 0;
            for(Lodging l: loList){
                payload += "$" + c.convertToHex(new String[]{l.getTitle(), l.getAddress(), l.getImage(), strLeaseID.get(count++)});
            }
            Publish(payload);
            return;
        }
        
        if(mycommand.equals("RENTAL")){
            ArrayList<Rental> rList = rdb.GetRentals(id);
            String payload = "";
            
            payload = c.convertToHex(new String[]{command,reserve, senderClientId, receiverClientID, rList.size()+"",mycommand, ""});
            
            for(Rental rental: rList){
                payload += "$" + c.convertToHex(new String[]{rental.getRentalID(),
                    rental.getIssueDate().toString(),
                    rental.getDueDate().toString(),
                    rental.getTotalAmount() + "",
                    rental.getStatus(),
                    rental.getLeaseID()});
            }
            Publish(payload);
        }
        
        if(mycommand.equals("RECEIPT")){
            String myID[] = id.split("\\|");
            String leaseID = myID[0];
            String rentalID = myID[1];
          
            Tenant t = tdb.GetOneTenant(leaseID);
            Receipt r = redb.GetReceipt(t.getTenantID(), rentalID);
            
            String payload = c.convertToHex(new String[]{command,reserve, senderClientId, receiverClientID, 0+"",mycommand, ""});
            payload += "$"+c.convertToHex(new String[]{
                r.getReceiptID(),
                r.getAmount()+"",
                " ",
                r.getPayStatus(),
                " "});
            System.err.println( r.getReceiptID());
            Publish(payload);
        }
        
        if(mycommand.equals("EXPENDSES")){
            ArrayList<Expense> exlist = edb.GetAllExpenses(id);
            int size = exlist.size();
             String payload = c.convertToHex(new String[]{command,reserve, senderClientId, receiverClientID, size+"",mycommand, ""});
            
             for(Expense e: exlist){
                 payload += "$"+c.convertToHex(new String[]{e.getCategory(),e.getAmount()+""});
             }
             
             Publish(payload);
        }
    }

    private void UpdateTenantReceipt(String message) throws Exception{
        String data[] = message.split("\\$");
        String server[] = c.convertToString(data[0]);
        
        String command = server[0];
        String reserve = server[1];
        String senderClientId = server[3];
        String receiverClientID = server[2];
        
        String[] body = data[1].split("\\@");
        
        String receiptID = body[1];
        String paidStatus = "Paid";
        System.err.println(receiptID);
        Receipt receipt = new Receipt();
        receipt.setPayStatus(paidStatus);
        receipt.setImage("http://"+ip+"/img/Receipt/"+receiptID+"_receipt.jpg");
        receipt.setReceiptID(receiptID);
       
        ReceiptDB db = new ReceiptDB();
        if(db.UpdateTenantReceipt(receipt)){
             System.err.println("HERE " + receiptID);
            //Start:: Convert String back to Image
            String strImg = body[0];
            byte[] decoded = Base64.getDecoder().decode(strImg);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(decoded));
            File path = new File("C:\\xampp\\htdocs\\img\\Receipt\\" +receiptID+"_receipt.jpg");
            ImageIO.write(img, "jpg", path);
            //End:; Convert String back to Image*/
            
            String payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Success"});
            Publish(payload);
        }else{
             System.err.println("HERE " + receiptID);
              String payload = c.convertToHex(new String[]{command, reserve, senderClientId, receiverClientID, "Failed"});
              Publish(payload);
        }
        
        String notifyData = c.convertToHex(new String[] {"Lodging Service System", "Rental was received from ", "RENTAL RECEIVED", receiptID});
        String resource = c.ToHex("leaseID") +"@"+c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);        
    }
      
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

    public void VerifyTenant(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        double verifyCode = Double.parseDouble(c.ToString(datas[4]));
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";
        VerificationDB vdb = new VerificationDB();

        String userID = vdb.VerifyCode(verifyCode);
        if (!userID.equals("")) {
            UserDB udb = new UserDB();
            User u = udb.GetUser(userID);
            if (u != null) {
                payload += c.ToHex("0") + "/" + c.ToHex(u.getUserId()) + "/" + c.ToHex(u.getName()) + "/"
                        + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getImage());
            } else {
                payload += c.ToHex("1");
            }
        } else {
            payload += c.ToHex("2");
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
        ArrayList<Tenant> tlist = new ArrayList<>();
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
            tlist.add(t);
        }
        payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
        Publish(payload);

        for (Tenant temp : tlist) {
            String serverData = c.convertToHex(new String[]{"004841", "000000000000000000000000", "serverLSSserver", "android", ""});
            String notifyData = c.convertToHex(new String[]{"Lodging Service System", "New lease was related to you.", "LEASE RECEIVED", temp.getUserID()});
            String resource = c.ToHex("leaseID") + "@" + c.ToHex("tenantID");
            Publish(serverData + "$" + notifyData + "$" + resource);
        }
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
        TenantDB tdb = new TenantDB();
        ArrayList<Lease> leaseList = ldb.GetUserLease(userId);

        if (!leaseList.isEmpty()) {
            payload += c.ToHex(leaseList.size() + "");
            for (int i = 0; i < leaseList.size(); i++) {
                Lodging l = lodb.GetLodging(leaseList.get(i).getLodgingID());
                int tenantCount = tdb.GetTenantCount(leaseList.get(i).getLeaseID());
                payload += "$" + c.ToHex(leaseList.get(i).getLeaseID()) + "/" + c.ToHex(leaseList.get(i).getDueDay()) + "/"
                        + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(leaseList.get(i).getIssueDate())) + "/" + c.ToHex(leaseList.get(i).getStatus())
                        + "/" + c.ToHex(leaseList.get(i).getLodgingID()) + "/" + c.ToHex(l.getTitle()) + "/" + c.ToHex(l.getAddress()) + "/" + c.ToHex(l.getImage()) + "/"
                        + c.ToHex(String.valueOf(tenantCount));
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
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        LeaseDB ldb = new LeaseDB();
        Lease lease = ldb.GetLease(leaseID);

        if (lease != null) {
            TenantDB tdb = new TenantDB();
            UserDB udb = new UserDB();
            ArrayList<Tenant> tenantList = tdb.GetAllTenant(leaseID);

            if (!tenantList.isEmpty()) {
                payload += c.ToHex(tenantList.size() + "") + "/" + c.ToHex(leaseID);
                for (int i = 0; i < tenantList.size(); i++) {
                    User u = udb.GetUser(tenantList.get(i).getUserID());
                    payload += "$" + c.ToHex(tenantList.get(i).getTenantID()) + "/" + c.ToHex(tenantList.get(i).getRoomType()) + "/"
                            + c.ToHex(tenantList.get(i).getRole()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseStart())) + "/"
                            + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseEnd())) + "/" + c.ToHex(String.valueOf(tenantList.get(i).getRent())) + "/"
                            + c.ToHex(String.valueOf(tenantList.get(i).getDeposit())) + "/" + c.ToHex(tenantList.get(i).getStatus()) + "/";

                    if (tenantList.get(i).getStatus().equals("Terminated")) {
                        payload += c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getBreakDate())) + "/" + c.ToHex(tenantList.get(i).getReason()) + "/";
                    }
                    
                    if(tenantList.get(i).getStatus().equals("Rejected"))
                        payload += c.ToHex(tenantList.get(i).getReason()) + "/";

                    payload += c.ToHex(tenantList.get(i).getUserID()) + "/" + c.ToHex(tenantList.get(i).getLeaseID()) + "/" + c.ToHex(u.getName()) + "/"
                            + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getImage());
                }
            } else {
                payload += c.ToHex("0");
            }
        } else {
            payload += c.ToHex("0");
        }
        Publish(payload);
    }

    public void UpdateLease(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        LeaseDB ldb = new LeaseDB();
        Lease l = new Lease();
        l.setLeaseID(c.ToString(datas[4]));
        l.setDueDay(c.ToString(datas[5]));
        l.setStatus(c.ToString(datas[6]));

        if (ldb.UpdateLease(l)) {
            payload += c.ToHex("Success");
        } else {
            payload += c.ToHex("Failed");
        }

        Publish(payload);
    }

    public void UpdateTenant(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

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
        if (c.ToString(datas[11]).equals("Terminated")) {
            Date breakDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[12]));
            java.sql.Date sqlBreakDate = new java.sql.Date(breakDate.getTime());
            t.setBreakDate(sqlBreakDate);
            t.setReason(c.ToString(datas[13]));
        }

        if (tdb.UpdateTenant(t)) {
            payload += c.ToHex("Success");
            String notifyData = c.convertToHex(new String[]{"Lodging Service System", "Lease was terminated by owner.[", "LEASE TERMINATED", c.ToString(datas[4])});
            String resource = c.ToHex("leaseID") + "@" + c.ToHex("tenantID");
            CreateNotification(serverData + "$" + notifyData + "$" + resource);
        } else {
            payload += c.ToHex("Failed");
        }

        Publish(payload);
    }

    public void CreateNewRental(String message) throws Exception {
        String[] datas = message.split("\\$");
        String[] head = datas[0].split("/");
        String command = head[0];
        String reserve = head[1];
        String senderClientId = head[3];
        String receiverClientId = head[2];
        String leaseID = c.ToString(head[4]);
        int expenseSize = Integer.parseInt(c.ToString(head[5]));
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cc = Calendar.getInstance();
        cc.setTime(new Date());
        String sysDate = sdf.format(cc.getTime());
        Date date = sdf.parse(sysDate);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());   //current date

        RentalDB rdb = new RentalDB();
        TenantDB tdb = new TenantDB();
        LeaseDB ldb = new LeaseDB();
        List<Tenant> tenantList = tdb.GetActiveTenant(leaseID);
        Lease l = ldb.GetLease(leaseID);
        Rental r = new Rental();
        String newRentalID = rdb.NewRentalID();
        double totalExpense = 0, totalAmt = 0;

        ExpenseDB edb = new ExpenseDB();
        for (int i = 1; i <= expenseSize; i++) {
            String[] body = datas[i].split("/");
            Expense e = new Expense();
            e.setExpenseID(edb.NewExpenseID());
            e.setCategory(c.ToString(body[0]));
            e.setAmount(Double.parseDouble(c.ToString(body[1])));

            Date payDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(body[2]));
            java.sql.Date sqlPayDate = new java.sql.Date(payDate.getTime());
            e.setPayDate(sqlPayDate);

            e.setDescription(c.ToString(body[3]));
            e.setStatus("Active");
            e.setRentalID(newRentalID);

            edb.AddNewExpense(e);
            totalExpense += Double.parseDouble(c.ToString(body[1]));
        }

        for (int i = 0; i < tenantList.size(); i++) {
            totalAmt += tenantList.get(i).getRent();
        }

        r.setRentalID(newRentalID);
        r.setIssueDate(sqlDate);
        r.setTotalAmount(totalAmt + totalExpense);
        r.setStatus("Active");
        r.setLeaseID(leaseID);

        if (l.getDueDay().equals("Middle of the month")) {
            cc.set(Calendar.DAY_OF_MONTH, 1);
            cc.add(Calendar.DATE, 14);

            Date middleOfMonth = sdf.parse(sdf.format(cc.getTime()));
            if (middleOfMonth.compareTo(date) <= 0) {
                cc.add(Calendar.MONTH, 1);
                middleOfMonth = cc.getTime();
            }
            java.sql.Date dueDate = new java.sql.Date(middleOfMonth.getTime());
            r.setDueDate(dueDate);
        } else if (l.getDueDay().equals("End of the month")) {
            cc.add(Calendar.MONTH, 1);
            cc.set(Calendar.DAY_OF_MONTH, 1);
            cc.add(Calendar.DATE, -1);

            Date lastOfMonth = sdf.parse(sdf.format(cc.getTime()));
            if (lastOfMonth.compareTo(date) <= 0) {
                cc.add(Calendar.MONTH, 1);
                lastOfMonth = cc.getTime();
            }
            java.sql.Date dueDate = new java.sql.Date(lastOfMonth.getTime());
            r.setDueDate(dueDate);
        } else {
            cc.set(Calendar.DAY_OF_MONTH, 1);
            cc.add(Calendar.DATE, 4);

            Date fifthOfMonth = sdf.parse(sdf.format(cc.getTime()));
            if (fifthOfMonth.compareTo(date) <= 0) {
                cc.add(Calendar.MONTH, 1);
                fifthOfMonth = cc.getTime();
            }
            java.sql.Date dueDate = new java.sql.Date(fifthOfMonth.getTime());
            r.setDueDate(dueDate);
        }

        if (rdb.CreateNewRental(r)) {
            ReceiptDB rcdb = new ReceiptDB();
            double utilityFee = totalExpense / tenantList.size();
            for (int i = 0; i < tenantList.size(); i++) {
                Receipt rc = new Receipt();
                rc.setReceiptID(rcdb.NewReceiptID());
                rc.setAmount(tenantList.get(i).getRent() + utilityFee);
                rc.setTenantID(tenantList.get(i).getTenantID());
                rc.setRentalID(r.getRentalID());
                rcdb.AddNewReceipt(rc);
            }

            payload += c.ToHex("0");
            
            String notifyData = c.convertToHex(new String[]{"Lodging Service System", "New rental was uploaded.", "RENTAL UPLOADED", "LS00000"});
            String resource = c.ToHex("leaseID") + "@" + c.ToHex("tenantID");
            CreateNotification(serverData + "$" + notifyData + "$" + resource);
        } else {
            payload += c.ToHex("1");
        }

        Publish(payload);
    }

    public void GetAllRental(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String leaseID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        RentalDB rdb = new RentalDB();
        ArrayList<Rental> rentalList = rdb.GetAllRental(leaseID);

        if (!rentalList.isEmpty()) {
            payload += c.ToHex(rentalList.size() + "");
            for (int i = 0; i < rentalList.size(); i++) {
                payload += "$" + c.ToHex(rentalList.get(i).getRentalID()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(rentalList.get(i).getIssueDate())) + "/"
                        + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(rentalList.get(i).getDueDate())) + "/" + c.ToHex(String.valueOf(rentalList.get(i).getTotalAmount())) + "/"
                        + c.ToHex(rentalList.get(i).getStatus());
            }
        } else {
            payload += c.ToHex("0");
        }
        Publish(payload);

        String notifyData = c.convertToHex(new String[]{"Lodging Service System", "Rental is going to reach the due date. Please make payment.", "RENTAL EXPIRED", ""});
        String resource = c.ToHex("leaseID") + "@" + c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
    }

    public void GetRental(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String rentalID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        RentalDB rdb = new RentalDB();
        ExpenseDB edb = new ExpenseDB();
        Rental rental = rdb.GetRental(rentalID);
        ArrayList<Expense> expenseList = edb.GetAllExpenses(rental.getRentalID());

        if (!expenseList.isEmpty()) {
            payload += c.ToHex("1") + "/" + c.ToHex(rental.getRentalID()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(rental.getIssueDate())) + "/"
                    + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(rental.getDueDate())) + "/" + c.ToHex(String.valueOf(rental.getTotalAmount())) + "/"
                    + c.ToHex(rental.getStatus()) + "/" + c.ToHex(rental.getLeaseID()) + "/" + c.ToHex(expenseList.size() + "");
            for (int i = 0; i < expenseList.size(); i++) {
                payload += "$" + c.ToHex(expenseList.get(i).getExpenseID()) + "/" + c.ToHex(expenseList.get(i).getCategory()) + "/"
                        + c.ToHex(String.valueOf(expenseList.get(i).getAmount())) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(expenseList.get(i).getPayDate())) + "/"
                        + c.ToHex(expenseList.get(i).getDescription()) + "/" + c.ToHex(expenseList.get(i).getStatus()) + "/" + c.ToHex(expenseList.get(i).getRentalID());
            }
        } else {
            payload += c.ToHex("0");
        }
        Publish(payload);
    }

    public void GetAllReceipt(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String rentalID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        RentalDB rdb = new RentalDB();
        TenantDB tdb = new TenantDB();
        UserDB udb = new UserDB();
        ReceiptDB rcdb = new ReceiptDB();
        Rental rental = rdb.GetRental(rentalID);
        ArrayList<Tenant> tenantList = tdb.GetActiveTenant(rental.getLeaseID());

        if (!tenantList.isEmpty()) {
            payload += c.ToHex(tenantList.size() + "");
            for (int i = 0; i < tenantList.size(); i++) {
                User u = udb.GetUser(tenantList.get(i).getUserID());
                if (tenantList.get(i).getStatus().equals("Active")) {
                    Receipt rc = rcdb.GetReceipt(tenantList.get(i).getTenantID(), rentalID);
                    payload += "$" + c.ToHex(tenantList.get(i).getTenantID()) + "/" + c.ToHex(tenantList.get(i).getRoomType()) + "/"
                            + c.ToHex(tenantList.get(i).getRole()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseStart())) + "/"
                            + c.ToHex(new SimpleDateFormat("dd-MM-yyyy").format(tenantList.get(i).getLeaseEnd())) + "/" + c.ToHex(String.valueOf(tenantList.get(i).getRent())) + "/"
                            + c.ToHex(String.valueOf(tenantList.get(i).getDeposit())) + "/" + c.ToHex(tenantList.get(i).getStatus()) + "/"
                            + c.ToHex(tenantList.get(i).getUserID()) + "/" + c.ToHex(tenantList.get(i).getLeaseID()) + "/" + c.ToHex(u.getName()) + "/"
                            + c.ToHex(u.getContactNo()) + "/" + c.ToHex(u.getEmail()) + "/" + c.ToHex(u.getImage()) + "/" + c.ToHex(rc.getReceiptID()) + "/"
                            + c.ToHex(String.valueOf(rc.getAmount())) + "/" + c.ToHex(rc.getPayStatus()) + "/";
                    if (rc.getPayStatus().equals("Paid") || rc.getStatus().equals("Reject")) {
                        payload += c.ToHex(rc.getImage()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(rc.getDateTime())) + "/";
                    }
                    payload += c.ToHex(rc.getReceiveStatus()) + "/" + c.ToHex(rc.getReason()) + "/" + c.ToHex(rc.getStatus()) + "/"
                            + c.ToHex(rc.getTenantID()) + "/" + c.ToHex(rc.getRentalID());
                }
            }
        } else {
            payload += c.ToHex("0");
        }
        Publish(payload);
    }

    public void UpdateExpense(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        ExpenseDB edb = new ExpenseDB();
        Expense e = new Expense();

        e.setExpenseID(c.ToString(datas[4]));
        e.setCategory(c.ToString(datas[5]));
        e.setAmount(Double.parseDouble(c.ToString(datas[6])));

        Date payDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[7]));
        java.sql.Date sqlPayDate = new java.sql.Date(payDate.getTime());
        e.setPayDate(sqlPayDate);

        e.setDescription(c.ToString(datas[8]));
        e.setStatus(c.ToString(datas[9]));
        e.setRentalID(c.ToString(datas[10]));

        if (edb.UpdateExpense(e)) {
            double sumExpense = edb.GetTotalExpense(e.getRentalID());
            RentalDB rdb = new RentalDB();
            TenantDB tdb = new TenantDB();
            ReceiptDB rcdb = new ReceiptDB();
            Rental r = rdb.GetRental(e.getRentalID());
            ArrayList<Tenant> tenantList = tdb.GetActiveTenant(r.getLeaseID());
            r.setTotalAmount(rcdb.UpdateAmount(tenantList, sumExpense, r));

            if (rdb.UpdateRental(r)) {
                payload += c.ToHex("0");
            }
        } else {
            payload += c.ToHex("1");
        }

        Publish(payload);
              
        String notifyData = c.convertToHex(new String[] {"Lodging Service System", "Rental was edited.", "RENTAL EDITED", e.getRentalID()});
        String resource = c.ToHex("leaseID") +"@"+c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
    }

    public void UpdateReceipt(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        ReceiptDB rcdb = new ReceiptDB();
        Receipt rc = new Receipt();

        rc.setReceiptID(c.ToString(datas[4]));
        rc.setAmount(Double.parseDouble(c.ToString(datas[5])));
        rc.setImage(c.ToString(datas[6]));
        rc.setPayStatus(c.ToString(datas[7]));
        rc.setReceiveStatus(c.ToString(datas[8]));
        rc.setReason(c.ToString(datas[9]));
        rc.setStatus(c.ToString(datas[10]));

        Date payDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(c.ToString(datas[11]));
        java.sql.Timestamp sqlPayDate = new java.sql.Timestamp(payDate.getTime());

        rc.setDateTime(sqlPayDate);
        rc.setTenantID(c.ToString(datas[12]));
        rc.setRentalID(c.ToString(datas[13]));

        if (rcdb.UpdateReceipt(rc)) {
            payload += c.ToHex("0");
        } else {
            payload += c.ToHex("1");
        }

        Publish(payload);
        
        String notifyData = "";
        if(c.ToString(datas[8]).equals("Received"))
            notifyData = c.convertToHex(new String[] {"Lodging Service System", "Rental was received by owner.", "RENTAL ACCEPTED", c.ToString(datas[12])});
        else
            notifyData = c.convertToHex(new String[] {"Lodging Service System", "Rental was rejected by owner.", "RENTAL REJECTED", c.ToString(datas[12])});
        String resource = c.ToHex("leaseID") +"@"+c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
    }

    public void AddVisitTime(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        int sizeOfTimeList = Integer.parseInt(c.ToString(datas[4]));
        String startDate = c.ToString(datas[5]);
        String visitTime = c.ToString(datas[6]);
        String userID = c.ToString(datas[7]);
        String lodgingID = c.ToString(datas[8]);
        String payload = "";

        VisitTimeDB vtdb = new VisitTimeDB();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();

        for (int i = 1; i <= sizeOfTimeList; i++) {
            VisitTime vt = new VisitTime();

            vt.setTimeID(vtdb.NewTimeID());
            Date strDate = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(startDate + " " + visitTime);
            java.sql.Timestamp sqlStrDate = new java.sql.Timestamp(strDate.getTime());
            vt.setVisitDateTime(sqlStrDate);
            vt.setUserID(userID);
            vt.setLodgingID(lodgingID);

            if (!vtdb.isTimeExist(vt)) {
                vtdb.AddVisitTime(vt);
            }

            cal.setTime(sdf.parse(startDate));
            cal.add(Calendar.DATE, 1);
            startDate = sdf.format(cal.getTime());
        }
        payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/" + c.ToHex("0");
        Publish(payload);
    }

    public void GetAllVisitTime(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        VisitTimeDB vtdb = new VisitTimeDB();
        ArrayList<VisitTime> timeList = vtdb.GetAllVisitTime(lodgingID);

        if (!timeList.isEmpty()) {
            AppointmentDB db = new AppointmentDB();
            payload += c.ToHex(timeList.size() + "") + "/";
            for (int i = 0; i < timeList.size(); i++) {
                String[] date = (new SimpleDateFormat("dd-MM-yyyy HH:mm a").format(timeList.get(i).getVisitDateTime())).split(" ");
                String visitDate = date[0] + "AND" + date[1] + " " + date[2];
                int appCount = 0;
                List<Appointment> appList = db.GetAllAppointment(visitDate, lodgingID);
                for (Appointment app : appList) {
                    if (!app.getStatus().equals("rejected")) {
                        appCount += 1;
                    }
                }
                payload += "$" + c.ToHex(timeList.get(i).getTimeID()) + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy HH:mm a").format(timeList.get(i).getVisitDateTime())) + "/"
                        + c.ToHex(timeList.get(i).getStatus()) + "/" + c.ToHex(timeList.get(i).getUserID()) + "/" + c.ToHex(timeList.get(i).getLodgingID()) + "/" + c.ToHex(appCount + "");
            }
        } else {
            payload += c.ToHex("0");
        }

        Publish(payload);
    }

    public void GetVisitTime(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String timeID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        UserDB udb = new UserDB();
        VisitTimeDB vtdb = new VisitTimeDB();
        VisitTime vt = vtdb.GetVisitTime(timeID);

        if (vt != null) {
            AppointmentDB db = new AppointmentDB();
            String[] date = (new SimpleDateFormat("dd-MM-yyyy HH:mm a").format(vt.getVisitDateTime())).split(" ");
            String visitDate = date[0] + "AND" + date[1] + " " + date[2];
            List<Appointment> appList = db.GetAllAppointment(visitDate, vt.getLodgingID());

            payload += c.ToHex("Exist") + "/" + c.ToHex(new SimpleDateFormat("dd-MM-yyyy HH:mm a").format(vt.getVisitDateTime())) + "/"
                    + c.ToHex(vt.getStatus()) + "/" + c.ToHex(vt.getUserID()) + "/" + c.ToHex(vt.getLodgingID()) + "/"
                    + c.ToHex(appList.size() + "") + "/" + c.ToHex(appList.size() + "");

            if (!appList.isEmpty()) {
                for (Appointment app : appList) {
                    User u = udb.GetUser(app.getTenantID());
                    payload += "$" + c.convertToHex(new String[]{app.getAppointmentID(), app.getDateTime(), app.getReason(), app.getState(),
                        app.getPriority(), app.getComment(), app.getStatus(), app.getLodgingID(), app.getTenantID(), app.getOwnerID(), u.getName(),
                        u.getImage(), u.getContactNo(), u.getEmail()});
                }
            }
        } else {
            payload += c.ToHex("NotExist");
        }

        Publish(payload);
    }

    public void UpdateVisitTime(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String instruction = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        VisitTimeDB vtdb = new VisitTimeDB();
        VisitTime vt = new VisitTime();

        vt.setTimeID(c.ToString(datas[5]));
        if (instruction.equals("Update")) {
            Date strDate = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(c.ToString(datas[6]) + " " + c.ToString(datas[7]));
            java.sql.Timestamp sqlStrDate = new java.sql.Timestamp(strDate.getTime());
            vt.setVisitDateTime(sqlStrDate);
            vt.setStatus("Active");
            vt.setLodgingID(c.ToString(datas[8]));
        } else if (instruction.equals("Delete")) {
            vt.setStatus("Inactive");
            vt.setLodgingID(c.ToString(datas[6]));
        }

        if (!vtdb.isTimeExist(vt)) {
            if (vtdb.UpdateVisitTime(vt)) {
                payload += c.ToHex("Success");
            } else {
                payload += c.ToHex("Failed");
            }
        } else {
            payload += c.ToHex("TimeExist");
        }

        Publish(payload);
    }

    public void RenewLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        LodgingDB ldb = new LodgingDB();
        Lodging l = new Lodging();
        l.setLodgingId(c.ToString(datas[4]));
        Date strDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[5]));
        java.sql.Date sqlStrDate = new java.sql.Date(strDate.getTime());
        l.setExpireDate(sqlStrDate);

        if (ldb.RenewLodging(l)) {
            payload += c.ToHex("Success");
        } else {
            payload += c.ToHex("Failed");
        }

        Publish(payload);
    }

    public void RemoveLodging(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String lodgingID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        LodgingDB ldb = new LodgingDB();
        if (ldb.RemoveLodging(lodgingID)) {
            payload += c.ToHex("Success");
        } else {
            payload += c.ToHex("Failed");
        }
        Publish(payload);
    }

    public void AddExpense(String message) throws Exception {
        String[] datas = message.split("/");
        String command = datas[0];
        String reserve = datas[1];
        String senderClientId = datas[3];
        String receiverClientId = datas[2];
        String rentalID = c.ToString(datas[4]);
        String payload = command + "/" + reserve + "/" + senderClientId + "/" + receiverClientId + "/";

        ExpenseDB edb = new ExpenseDB();
        Expense e = new Expense();
        e.setExpenseID(edb.NewExpenseID());
        e.setCategory(c.ToString(datas[5]));
        e.setAmount(Double.parseDouble(c.ToString(datas[6])));

        Date payDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.ToString(datas[7]));
        java.sql.Date sqlPayDate = new java.sql.Date(payDate.getTime());
        e.setPayDate(sqlPayDate);

        e.setDescription(c.ToString(datas[8]));
        e.setStatus("Active");
        e.setRentalID(rentalID);

        if (edb.AddNewExpense(e)) {
            double sumExpense = edb.GetTotalExpense(e.getRentalID());
            RentalDB rdb = new RentalDB();
            TenantDB tdb = new TenantDB();
            ReceiptDB rcdb = new ReceiptDB();
            Rental r = rdb.GetRental(e.getRentalID());
            ArrayList<Tenant> tenantList = tdb.GetActiveTenant(r.getLeaseID());
            r.setTotalAmount(rcdb.UpdateAmount(tenantList, sumExpense, r));

            if (rdb.UpdateRental(r)) {
                payload += c.ToHex("Success");
            }
        } else {
            payload += c.ToHex("Failed");
        }

        Publish(payload);
        
        String notifyData = c.convertToHex(new String[] {"Lodging Service System", "Rental was edited.", "RENTAL EDITED", rentalID});
        String resource = c.ToHex("leaseID") +"@"+c.ToHex("tenantID");
        CreateNotification(serverData + "$" + notifyData + "$" + resource);
    }
}
