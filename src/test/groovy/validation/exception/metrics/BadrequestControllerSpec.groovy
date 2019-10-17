package validation.exception.metrics

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Shared

import javax.inject.Inject

@MicronautTest
class BadrequestControllerSpec extends Specification {

  @Shared
  @Inject
  EmbeddedServer embeddedServer

  @Shared
  @AutoCleanup
  @Inject
  @Client("/")
  RxHttpClient client

  void "test index"() {
    given:
    HttpResponse response = client.toBlocking().exchange("/badrequest")

    expect:
    response.status == HttpStatus.OK
  }

  void "post should only generate 400 response code metrics not 500"() {
    when:
    client.toBlocking().exchange(HttpRequest.POST("/badrequest", new PostBody()))

    then:
    HttpClientResponseException t = thrown(HttpClientResponseException)
    t.getStatus().code == 400

    and:
    def response = client.toBlocking().exchange("/prometheus", String)
    !response.body().contains('http_server_requests_seconds_count{exception="ConstraintViolationException",method="POST",status="500",uri="/badrequest",}')
  }
}
