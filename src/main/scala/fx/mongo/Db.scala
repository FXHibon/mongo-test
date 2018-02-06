package fx.mongo

import com.typesafe.config.Config
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries, fromCodecs }
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

import scala.reflect.ClassTag

/**
 * db accessor
 * @param conf app configuration
 */
class Db(implicit conf: Config) extends CustomCodecs {

  // configure our regisry: tell mongo how to handle java/scala types
  private val codecRegistry: CodecRegistry = fromRegistries(
    fromCodecs(instantCodec),
    DEFAULT_CODEC_REGISTRY,
    fromProviders(classOf[Book]))

  // connect to server
  private val mongoClient: MongoClient = MongoClient(conf.getString("db.uri"))
  // retrieve our database
  private val database: MongoDatabase = mongoClient.getDatabase(conf.getString("db.name")).withCodecRegistry(codecRegistry)

  /**
   * get a collection instance, typed to the asked type
   * @param name collection name
   * @tparam T business data structure, or Document
   * @return
   */
  def collection[T](name: String)(implicit ct: ClassTag[T]): MongoCollection[T] = database.getCollection[T](name)

  // kill connection on jvm shudown
  sys.addShutdownHook {
    mongoClient.close()
  }

}
