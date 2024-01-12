package todo.todoapi

import cats.Applicative
import cats.effect.Concurrent
import cats.syntax.all._
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.circe.*

enum Priority:
  case High, Medium, Low

case class Todo(name: String, prio: Priority)

object Todo:
  given Decoder[Todo] = Decoder.derived[Todo]
  given [F[_]: Concurrent]: EntityDecoder[F, Todo] = jsonOf
  given Encoder[Todo] = Encoder.AsObject.derived[Todo]
  given [F[_]]: EntityEncoder[F, Todo] = jsonEncoderOf

trait Todos[F[_]]:
  def get(): F[Todo]

object Todos:
  def impl[F[_]: Applicative]: Todos[F] = new Todos[F]:
    def get(): F[Todo] =
      Todo("test_todo", Priority.High).pure[F]
