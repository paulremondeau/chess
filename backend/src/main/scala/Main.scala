import scala.io.StdIn.readLine
import java.time.LocalDateTime

import cats.effect._

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.ember.server._

import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder

import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
import com.comcast.ip4s.ipv4
import com.comcast.ip4s.port

import org.http4s.server.middleware.CORS
import org.http4s.headers.Origin

/** The main function of the chess game.
  *
  * Creates a chess game and it's white turn to play. The game continue until
  * one player win or a stalemate occurs
  */
// @main def playChess: Unit =

//   val game: Game = Game()

//   while (game.winner == "") {
//     game.play()
//   }

//   println(game.winner + " won!")
//   println(game.board.visualizeBoard(game.winner))

case class PieceMovement(
    selectedSquare: String,
    targetSquare: String
)

case class PieceInformation(
    pieceType: String,
    pieceColor: String,
    availableMovements: List[String],
    isChecked: Boolean
)

case class DataOutputFormat(
    winner: String,
    turn: String,
    board: Map[String, PieceInformation],
    timesPlay: scala.collection.mutable.Map[String, Array[Long]],
    gameClock: Long
)

object Main extends IOApp {

  var blackLastPlay: Long = -1
  var whiteLastPlay: Long = -1

  val game: GameAPI = GameAPI()

  val helloWorldService = HttpRoutes
    .of[IO] {
      case req @ GET -> Root / "play" =>
        println(req.params)
        val board: Map[String, PieceInformation] = game.play(
          req.params.apply("selectedSquare"),
          req.params.apply("targetSquare"),
          req.params.apply("promotion")
        )

        println(game.board)

        Ok(
          DataOutputFormat(
            game.winner.toLowerCase(),
            game.turn.toLowerCase(),
            board,
            game.timesPlay,
            System.currentTimeMillis()
          )
        )

      case req @ GET -> Root / "initialize" =>
        game.initialize()
        blackLastPlay = -1
        whiteLastPlay = -1

        println(game.board)
        Ok(
          DataOutputFormat(
            game.winner.toLowerCase(),
            game.turn.toLowerCase(),
            game.convertForFrontend(),
            game.timesPlay,
            System.currentTimeMillis()
          )
        )

      case req @ GET -> Root / "board" =>
        Ok(
          DataOutputFormat(
            game.winner.toLowerCase(),
            game.turn.toLowerCase(),
            game.convertForFrontend(),
            game.timesPlay,
            System.currentTimeMillis()
          )
        )

      case req @ GET -> Root / "lost" =>
        game.setWinner(req.params.apply("loser"))
        Ok(
          DataOutputFormat(
            game.winner.toLowerCase(),
            game.turn.toLowerCase(),
            game.convertForFrontend(),
            game.timesPlay,
            System.currentTimeMillis()
          )
        )

      case GET -> Root =>
        Ok()

    }
    .orNotFound

  /** @todo
    *   remove allow origin all and make it safer by making only frontend able
    *   to share ressources
    */
  val corsService = CORS.policy
    // .withAllowOriginHost(
    //   Set(
    //     Origin
    //       .Host(Uri.Scheme.http, Uri.RegName(frontEndUrl), Some(frontEndPort))
    //   )
    // )
    .withAllowOriginAll
    .apply(helloWorldService)

  val server = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(corsService)
    .build

  def run(args: List[String]): IO[ExitCode] = server
    .use(_ => IO.never)
    .as(ExitCode.Success)
}
