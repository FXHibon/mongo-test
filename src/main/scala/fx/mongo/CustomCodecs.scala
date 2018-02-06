package fx.mongo

import java.time.Instant

import org.bson.codecs.{ Codec, DecoderContext, EncoderContext }
import org.bson.{ BsonReader, BsonWriter }

/**
 * few custom codes (bson -> scala / scala -> bson)
 */
trait CustomCodecs {

  /**
   * serialize Instant to epoch ms
   */
  val instantCodec: Codec[Instant] = new Codec[Instant] {

    override def decode(reader: BsonReader, decoderContext: DecoderContext): Instant = Instant.ofEpochMilli(reader.readInt64())

    override def encode(writer: BsonWriter, value: Instant, encoderContext: EncoderContext): Unit = writer.writeInt64(value.toEpochMilli)

    override def getEncoderClass: Class[Instant] = classOf[Instant]
  }

}
