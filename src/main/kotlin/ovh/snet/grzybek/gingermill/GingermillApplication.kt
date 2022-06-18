package ovh.snet.grzybek.gingermill

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GingermillApplication

fun main(args: Array<String>) {
  runApplication<GingermillApplication>(*args)
}
