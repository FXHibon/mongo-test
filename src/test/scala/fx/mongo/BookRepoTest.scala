package fx.mongo

import java.time.Instant
import java.util.UUID

import fx.mongo.fixtures.DbAwareTest
import org.mongodb.scala.bson.collection.immutable.Document

import scala.concurrent.Future

class BookRepoTest extends DbAwareTest {

  "BookRepo" when {
    "findBook" must {

      "have no result" in {
        bookRepo.findBook(UUID.randomUUID()).map { optBook =>
          optBook mustBe None
        }
      }

      "have one result" in {
        val id = UUID.randomUUID()
        val now = Instant.now()
        val bookDocument = Document(
          "_id" -> id,
          "name" -> "Lord of the rings",
          "author" -> "J.K Rowling",
          "createdAt" -> now)

        for {
          _ <- insert(bookDocument)
          optBook <- bookRepo.findBook(id)
        } yield {
          optBook must contain(Book(id, "Lord of the rings", "J.K Rowling", now))
        }
      }

    }

    "findBooks" must {

      "have no result" in {
        bookRepo.findBooks().map { books =>
          books mustBe empty
        }
      }

      "have multiple result" in {
        for {
          _ <- Future.sequence(
            (1 to 100)
              .map(i => Document("_id" -> UUID.randomUUID(), "name" -> s"Book $i", "author" -> s"author $i", "createdAt" -> Instant.now()))
              .map(insert))
          books <- bookRepo.findBooks()
        } yield books must have size 100
      }
    }

    "deleteBook" must {
      "do nothing" in {
        bookRepo.deleteBook(UUID.randomUUID()).map(b => fail(s"should not have return $b")).recover {
          case err => err mustBe a[NoSuchElementException]
        }
      }

      "delete one document" in {

        val id = UUID.randomUUID()
        val now = Instant.now()
        val bookDocument = Document(
          "_id" -> id,
          "name" -> "Lord of the rings",
          "author" -> "J.K Rowling",
          "createdAt" -> now)

        for {
          _ <- insert(bookDocument)
          deletedBook <- bookRepo.deleteBook(id)
        } yield {
          deletedBook mustBe Book(id, "Lord of the rings", "J.K Rowling", now)
        }
      }
    }
  }

}
