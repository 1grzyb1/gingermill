package ovh.snet.grzybek.gingermill.config

import mu.KLogger
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KLoggerConfig {

  @Bean
  fun logger(): KLogger {
    return KotlinLogging.logger {}
  }
}