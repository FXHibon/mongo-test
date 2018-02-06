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
        val testBook = Book(
          _id = UUID.randomUUID(),
          name = "Lord of the rings",
          author = "J.K Rowling",
          createdAt = Instant.now(),
          n = Nested("lol"))

        for {
          _ <- bookRepo.addBook(testBook)
          optBook <- bookRepo.findBook(testBook._id)
        } yield {
          optBook must contain(testBook)
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
              .map(i => Book(UUID.randomUUID(), s"name $i", s"author $i", Instant.now(), Nested(s"n$i")))
              .map(bookRepo.addBook))
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

        val testBook = Book(
          _id = UUID.randomUUID(),
          name = "Lord of the rings",
          author = "J.K Rowling",
          createdAt = Instant.now(),
          Nested("lol"))

        for {
          _ <- bookRepo.addBook(testBook)
          deletedBook <- bookRepo.deleteBook(testBook._id)
        } yield {
          deletedBook mustBe testBook
        }
      }
    }
  }

}
