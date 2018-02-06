package fx.mongo

import java.time.Instant
import java.util.UUID

/**
 * business data structure
 * @param _id
 * @param name
 * @param author
 * @param createdAt
 */
case class Book(_id: UUID, name: String, author: String, createdAt: Instant, n: Nested)

case class Nested(_id: String)