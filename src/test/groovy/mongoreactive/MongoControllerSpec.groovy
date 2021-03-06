package mongoreactive

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class MongoControllerSpec extends Specification {

    @Shared
    MongoDBContainer mongo = new MongoDBContainer("mongo:4.1.1")
            .withExposedPorts(27017)

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer

    @Shared
    @AutoCleanup
    RxHttpClient rxClient

    def setupSpec() {
        embeddedServer = ApplicationContext.run(EmbeddedServer,
                ['mongodb.uri': mongo.getReplicaSetUrl("micronaut")])
        rxClient = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())
    }

    void 'test it is running'() {
        expect:
        mongo.running
        embeddedServer.running
        rxClient.running
    }

    void 'test save a document from a POJO'() {
        given:
            User u = new User('Iván', 38)

        when:
            HttpRequest request = HttpRequest.POST('/users', u)
            String response = rxClient.toBlocking().retrieve(request)

        then:
            response == '{"name":"Iván","age":38}'
    }
}
