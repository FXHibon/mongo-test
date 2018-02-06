package fx.mongo.fixtures

import com.typesafe.config.{ Config, ConfigFactory }
import fx.mongo.AppModule
import org.mongodb.scala.{ Completed, MongoCollection }
import org.mongodb.scala.bson.collection.immutable.Document
import org.scalatest.{ AsyncWordSpec, BeforeAndAfterEach, MustMatchers }

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

abstract class DbAwareTest extends AsyncWordSpec with AppModule with MustMatchers with BeforeAndAfterEach with BsonHelper {

  override val config: Config = ConfigFactory.load("test.application")

  val col: MongoCollection[Document] = db.database.getCollection("book")

  override def beforeEach(): Unit = {
    Await.ready(db.database.drop().toFuture(), 1 second)
  }

  def insert(d: Document): Future[Completed] = col.insertOne(d).toFuture()
}
