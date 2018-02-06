package fx.mongo

import java.util.UUID

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * sample of repository
 * @param db db accessor
 */
class BookRepo(db: Db) {

  // get our mongo collection
  private val collection: MongoCollection[Book] = db.collection[Book]("book")

  /**
   * find one book by id, returning None if not found
   * @param id
   * @return
   */
  def findBook(id: UUID): Future[Option[Book]] = collection.find(equal("_id", id)).toFuture().map(_.headOption)

  /**
   * find all books
   * @return
   */
  def findBooks(): Future[Seq[Book]] = collection.find().toFuture()

  /**
   * add one book, returning the new one if it's ok
   * @param book
   * @return
   */
  def addBook(book: Book): Future[Option[Book]] = collection.insertOne(book).toFuture().flatMap(_ => findBook(book._id))

  /**
   * delete one book, returning the deleted one if it's ok
   * @param id
   * @return
   */
  def deleteBook(id: UUID): Future[Book] = findBook(id)
    .collect { case Some(b) => b }
    .flatMap(b => {
      collection.deleteOne(equal("_id", id)).toFuture().map(_ => b)
    })

}
