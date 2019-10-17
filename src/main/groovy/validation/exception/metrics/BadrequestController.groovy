package validation.exception.metrics

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post

import javax.validation.Valid
import javax.validation.constraints.NotEmpty


@Controller("/badrequest")
class BadrequestController {

  @Get("/")
  HttpStatus index() {
    return HttpStatus.OK
  }

  @Post("/")
  HttpStatus post(@Body @Valid PostBody postBody) {
    HttpStatus.OK
  }
}

@Introspected
class PostBody {
  @NotEmpty
  String body
}
