package fx.mongo
import com.typesafe.config.Config
import org.mongodb.scala._

class Db(implicit conf: Config) {

  private val mongoClient: MongoClient = MongoClient(conf.getString("db.uri"))
  val database: MongoDatabase = mongoClient.getDatabase(conf.getString("db.name"))

  sys.addShutdownHook { mongoClient.close() }

}
