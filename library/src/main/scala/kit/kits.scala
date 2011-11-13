package unfiltered.kit

import unfiltered.request._
import unfiltered.response._
import unfiltered.{Cycle,Async}

/** A kit that conditionally prepends a response function */
trait Prepend { self =>
  def intent: Cycle.Intent[Any,Any]
  /** The produced intent is defined for all inputs, is Pass
   *  where the given intent parameter is not defined. */
  def apply[A,B](intent: unfiltered.Cycle.Intent[A,B]) = {
    intent.fold(
      (_) => Pass,
      (req, rf) =>
        self.intent.fold(
          (_) => rf,
          (_, kitRf) => kitRf ~> rf
        )(req)
    )
  }
  def async[A,B](intent: Async.Intent[A,B]) =
    Async.Intent[A,B] {
      case req =>
        val dreq = new DelegatingRequest(req) with Async.Responder[B] {
          def respond(rf: unfiltered.response.ResponseFunction[B]) {
            import unfiltered.PassingIntent
            val pi = self.intent.fold[ResponseFunction[B]](
              (_) => rf,
              (_, kitRf) => kitRf ~> rf
            )
            req.respond(pi(req))
          }
        }
        intent(dreq)
    }
}

object NoOpResponder extends Responder[Any] {
  def respond(res: HttpResponse[Any]) { }
}
