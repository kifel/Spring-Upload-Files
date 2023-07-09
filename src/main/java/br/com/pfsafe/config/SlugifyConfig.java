package br.com.pfsafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.slugify.Slugify;

@Configuration
public class SlugifyConfig {

  @Bean
  public Slugify slugify() {
    return new Slugify();
  }

}
