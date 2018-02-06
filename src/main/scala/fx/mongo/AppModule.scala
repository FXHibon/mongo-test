package fx.mongo
import com.softwaremill.macwire._
import com.typesafe.config.{ Config, ConfigFactory }

/**
 * wire components
 */
trait AppModule {

  implicit val config: Config = ConfigFactory.load()

  lazy val db: Db = wire[Db]

  lazy val bookRepo: BookRepo = wire[BookRepo]
}
