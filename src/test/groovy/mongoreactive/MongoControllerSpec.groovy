package mongoreactive

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class MongoControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient rxClient = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

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
