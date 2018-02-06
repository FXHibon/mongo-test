package fx.mongo

import java.time.Instant
import java.util.UUID

case class Book(_id: UUID, name: String, author: String, createdAt: Instant)
