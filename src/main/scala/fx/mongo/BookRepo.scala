package fx.mongo

import java.util.UUID

import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BookRepo(db: Db) {

  private val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Book]), DEFAULT_CODEC_REGISTRY)
  private val collection: MongoCollection[Book] = db.database.getCollection[Book]("book").withCodecRegistry(codecRegistry)

  def findBook(id: UUID): Future[Option[Book]] = collection.find(equal("_id", id)).toFuture().map(_.headOption)

  def findBooks(): Future[Seq[Book]] = collection.find().toFuture()

  def addBook(book: Book): Future[Option[Book]] = collection.insertOne(book).toFuture().flatMap(_ => findBook(book._id))

  def deleteBook(id: UUID): Future[Book] = findBook(id)
    .collect { case Some(b) => b }
    .flatMap(b => {
      collection.deleteOne(equal("_id", id)).toFuture().map(_ => b)
    })

}
