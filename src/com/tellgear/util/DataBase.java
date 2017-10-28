package com.tellgear.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import com.tellgear.net.User;

public class DataBase {

    public String path;

    private static final String NAME = "name";
    private static final String USER = "user";
    private static final String PASSWD = "passwd";
    private static final String PERMISSIONS = "permissions";
    private static final String LAST_CONNECTION = "last_connection";
    private static final String PATH = "path";

    public DataBase(String filepath){
        this.path = filepath;

        File file = new File(filepath);
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                if(!file.getParentFile().getParentFile().exists()){
                    file.getParentFile().getParentFile().mkdir();
                }
                file.getParentFile().mkdir();
            }
            try {
                file.createNewFile();
                saveUsers();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({ "unchecked", "null" })
    public List<User> readUsers(){
        List<User> users = new ArrayList<>();
        try {

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(path);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            User user = null;

            while(eventReader.hasNext()){
                XMLEvent event = eventReader.nextEvent();

                if(event.isStartElement()){
                    StartElement startElement = event.asStartElement();
                    if(startElement.getName().getLocalPart().equals(USER)){
                        user = new User();
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NAME)) {
                            event = eventReader.nextEvent();
                            user.setName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PASSWD)) {
                        event = eventReader.nextEvent();
                        user.setPasswd(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PERMISSIONS)) {
                        event = eventReader.nextEvent();
                        user.setPermissions(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(LAST_CONNECTION)) {
                        event = eventReader.nextEvent();
                        user.setLastConnection(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PATH)) {
                        event = eventReader.nextEvent();
                        user.setPath(event.asCharacters().getData());
                        continue;
                    }
                }

                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(USER)) {
                        users.add(user);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void saveUsers() throws Exception {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(path));

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        StartElement usersStartElement = eventFactory.createStartElement("", "", "data");
        eventWriter.add(end);
        eventWriter.add(usersStartElement);
        eventWriter.add(end);

        for(User user:User.users){
            eventWriter.add(tab);
            StartElement newUserElement = eventFactory.createStartElement("", "", "user");
            eventWriter.add(newUserElement);
            eventWriter.add(end);

            createNode(eventWriter, NAME, user.getName());
            createNode(eventWriter, PASSWD, user.getPasswd());
            createNode(eventWriter, PERMISSIONS, user.getPermissions());
            createNode(eventWriter, LAST_CONNECTION, user.getLastConnection());
            createNode(eventWriter, PATH, user.getPath());

            eventWriter.add(tab);
            eventWriter.add(eventFactory.createEndElement("", "", "user"));
            eventWriter.add(end);
        }

        eventWriter.add(eventFactory.createEndElement("", "", "data"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }
}
