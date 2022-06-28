package ovh.snet.grzybek.gingermill.model

data class FindPathRequest(
  val startTitle: String?,
  val endTitle: String?,
  val moveStart: Boolean = false
)