package me.exzibyte.Utilities;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.exzibyte.Singularity;
import org.bson.Document;

public class  Database {

    private final Singularity singularity;
    private final MongoClientURI clientURI;
    private MongoClient client;
    private MongoDatabase db;

    public Database(Singularity singularity){
        clientURI = new MongoClientURI(singularity.getConfig().get("dbURI"));
        this.singularity = singularity;
    }

    public void connect() {
        client = new MongoClient(clientURI);
        db = client.getDatabase("Singularity");
    }

    public MongoCollection<Document> getCollection(String collection) {
        return db.getCollection(collection);
    }

    public void close() {
        client.close();
    }
}
