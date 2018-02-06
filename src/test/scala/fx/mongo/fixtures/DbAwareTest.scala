package fx.mongo.fixtures

import com.typesafe.config.{ Config, ConfigFactory }
import fx.mongo.AppModule
import org.mongodb.scala.{ Completed, MongoCollection }
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._
import org.scalatest.{ AsyncWordSpec, BeforeAndAfterEach, MustMatchers }

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

/**
 * clean db between each tests
 */
abstract class DbAwareTest extends AsyncWordSpec with AppModule with MustMatchers with BeforeAndAfterEach with BsonHelper {

  override val config: Config = ConfigFactory.load("test.application")

  val col: MongoCollection[Document] = db.collection[Document]("book")

  override def beforeEach(): Unit = {
    Await.ready(col.deleteMany(notEqual("_id", 1)).toFuture(), 1 second)
  }

  def insert(d: Document): Future[Completed] = col.insertOne(d).toFuture()
}
