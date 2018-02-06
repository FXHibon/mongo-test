package fx.mongo.fixtures

import java.time.Instant
import java.util.UUID

import org.mongodb.scala.bson.{ BsonInt64, BsonString, BsonTransformer }

/**
 * just useful for the test, as we don't want to use the 'out of the box' (un)serializer from mongo scala driver
 */
trait BsonHelper {

  implicit object TransformUUID extends BsonTransformer[UUID] {
    def apply(value: UUID): BsonString = BsonString(value.toString)
  }

  implicit object TransformInstant extends BsonTransformer[Instant] {
    def apply(value: Instant): BsonInt64 = BsonInt64(value.toEpochMilli)
  }

}
