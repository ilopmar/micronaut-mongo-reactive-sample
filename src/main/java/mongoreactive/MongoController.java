package mongoreactive;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;

@Controller("/")
public class MongoController {

    private final MongoClient mongoClient;

    public MongoController(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Post("/users")
    public Single<User> save(String name, Integer age) {
        User user = new User(name, age);
        return Single
                .fromPublisher(getCollection().insertOne(user))
                .map(success -> user);
    }

    private MongoCollection<User> getCollection() {
        return mongoClient
                .getDatabase("micronaut")
                .getCollection("users", User.class);
    }
}
