package fx.mongo.fixtures

import java.time.Instant
import java.util.UUID

import org.bson.codecs.UuidCodecHelper
import org.bson.{ BSONException, BsonBinary, BsonBinarySubType, UuidRepresentation }
import org.mongodb.scala.bson.{ BsonBinary, BsonInt64, BsonString, BsonTransformer }
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

/**
 * just useful for the test, as we don't want to use the 'out of the box' (un)serializer from mongo scala driver
 */
trait BsonHelper {

  private def writeLongToArrayBigEndian(bytes: Array[Byte], offset: Int, x: Long): Unit = {
    bytes(offset + 7) = (0xFFL & x).toByte
    bytes(offset + 6) = (0xFFL & (x >> 8)).toByte
    bytes(offset + 5) = (0xFFL & (x >> 16)).toByte
    bytes(offset + 4) = (0xFFL & (x >> 24)).toByte
    bytes(offset + 3) = (0xFFL & (x >> 32)).toByte
    bytes(offset + 2) = (0xFFL & (x >> 40)).toByte
    bytes(offset + 1) = (0xFFL & (x >> 48)).toByte
    bytes(offset) = (0xFFL & (x >> 56)).toByte
  }

  // this is sh*t
  implicit object TransformUUID extends BsonTransformer[UUID] {
    def apply(value: UUID): BsonBinary = {
      val binaryData = new Array[Byte](16)
      writeLongToArrayBigEndian(binaryData, 0, value.getMostSignificantBits)
      writeLongToArrayBigEndian(binaryData, 8, value.getLeastSignificantBits)
      // changed the default subtype to STANDARD since 3.0
      new BsonBinary(BsonBinarySubType.UUID_STANDARD, binaryData)
    }
  }

  implicit object TransformInstant extends BsonTransformer[Instant] {
    def apply(value: Instant): BsonInt64 = BsonInt64(value.toEpochMilli)
  }

}
